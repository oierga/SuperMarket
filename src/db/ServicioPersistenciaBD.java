package db;

import domain.Producto;
import domain.TipoCategoria;
import domain.Usuario;
import domain.Categoria;
import domain.TipoUsuario;

import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.logging.FileHandler;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ServicioPersistenciaBD {

    private Logger logger = null;

    private Connection connection;
    private Statement statement;
    private static Exception lastError = null;

    public ServicioPersistenciaBD() {
    }

    public boolean init(String nombrePersistencia, String configPersistencia) {
        try {
            Class.forName("org.sqlite.JDBC");
            connection = DriverManager.getConnection("jdbc:sqlite:" + configPersistencia + nombrePersistencia);
            log(Level.INFO, "Conectada base de datos " + nombrePersistencia, null);
            statement = connection.createStatement();
            statement.setQueryTimeout(10);

            statement.executeUpdate("CREATE TABLE IF NOT EXISTS usuario (" +
                    "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    "nombre STRING, " +
                    "nombreDeUsuario STRING UNIQUE, " +
                    "contraseña STRING, " +
                    "activo BOOLEAN)");

            statement.executeUpdate("CREATE TABLE IF NOT EXISTS producto (" +
                    "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    "nombre STRING, " +
                    "precio DOUBLE, " +
                    "categoriaNombre STRING, " +
                    "rutaImagen STRING)");

            statement.executeUpdate("CREATE TABLE IF NOT EXISTS categoria (" +
                    "nombre STRING PRIMARY KEY, " +
                    "imagen STRING)");

            statement.executeUpdate("CREATE TABLE IF NOT EXISTS carrito (" +
                    "idUsuario INTEGER, " +
                    "idProducto INTEGER, " +
                    "FOREIGN KEY(idUsuario) REFERENCES usuario(id), " +
                    "FOREIGN KEY(idProducto) REFERENCES producto(id))");

            log(Level.INFO, "Tablas creadas correctamente", null);
            return true;

        } catch (ClassNotFoundException | SQLException e) {
            lastError = e;
            connection = null;
            log(Level.SEVERE, "Error en conexión de base de datos " + nombrePersistencia, e);
            return false;
        }
    }

    public boolean initDatosTest(String nombrePersistencia, String configPersistencia) {
        if (!init(nombrePersistencia, configPersistencia)) {
            return false;
        }
        try {
        	statement.executeUpdate("INSERT OR IGNORE INTO usuario (nombre, nombreDeUsuario, contraseña, activo) VALUES " +
                    "('Admin', 'admin', '1234', 1), " +
                    "('Cliente', 'cliente', '5678', 1)");

            statement.executeUpdate("INSERT OR IGNORE INTO categoria (nombre, imagen) VALUES " +
                    "('Frutas', 'ruta/frutas.png'), " +
                    "('Panadería', 'ruta/panaderia.png'), " +
                    "('Lácteos', 'ruta/lacteos.png')");

            statement.executeUpdate("INSERT OR IGNORE INTO producto (nombre, precio, categoriaNombre, rutaImagen) VALUES " +
                    "('Manzana', 0.5, 'Frutas', 'ruta/manzana.png'), " +
                    "('Pan', 1.0, 'Panadería', 'ruta/pan.png'), " +
                    "('Leche', 1.2, 'Lácteos', 'ruta/leche.png')");

            log(Level.INFO, "Datos de prueba inicializados correctamente", null);
            return true;

        } catch (SQLException e) {
            lastError = e;
            log(Level.SEVERE, "Error al inicializar datos de prueba", e);
            return false;
        }
    }
    /*
    public void guardarUsuario(Usuario usuario) {
        try {
            String sql = "INSERT INTO usuario (nombre, nombreDeUsuario, contraseña, activo) VALUES (?, ?, ?, ?)";
            PreparedStatement stmt = connection.prepareStatement(sql);
            stmt.setString(1, usuario.getNombreDeUsuario());
            stmt.setString(2, usuario.getNombreDeUsuario());
            stmt.setString(3, usuario.getContraseña());
            stmt.setBoolean(4, usuario.isActivo());
            stmt.executeUpdate();
            stmt.close();
            log(Level.INFO, "Usuario guardado correctamente: " + usuario.getNombreDeUsuario(), null);
        } catch (SQLException e) {
            lastError = e;
            log(Level.SEVERE, "Error al guardar usuario: " + usuario.getNombreDeUsuario(), e);
        }
    }
    */

    public ArrayList<Usuario> cargarTodosUsuarios() {
        ArrayList<Usuario> usuarios = new ArrayList<>();
        try {
            ResultSet rs = statement.executeQuery("SELECT * FROM usuario");
            while (rs.next()) {
                Usuario usuario = new Usuario(
                        rs.getString("nombreDeUsuario"),
                        rs.getString("contraseña"),
                        rs.getBoolean("activo")
                );
                usuarios.add(usuario);
            }
            rs.close();
        } catch (SQLException e) {
            lastError = e;
            log(Level.SEVERE, "Error al cargar todos los usuarios", e);
        }
        return usuarios;
    }

    public ArrayList<Producto> cargarTodosProductos() {
        ArrayList<Producto> productos = new ArrayList<>();
        try {
            ResultSet rs = statement.executeQuery("SELECT * FROM producto");
            while (rs.next()) {
            	Categoria categoria = cargarCategoria(rs.getString("categoriaNombre"));

                Producto producto = new Producto(
                        rs.getInt("id"),
                        rs.getString("nombre"),
                        rs.getDouble("precio"),
                        rs.getString("rutaImagen"),
                        categoria
                );

                productos.add(producto);
            }
            rs.close();
        } catch (SQLException e) {
            lastError = e;
            log(Level.SEVERE, "Error al cargar todos los productos", e);
        }
        return productos;
    }
    
    public Categoria cargarCategoria(String nombreCategoria) {
        try {
            PreparedStatement stmt = connection.prepareStatement("SELECT * FROM categoria WHERE nombre = ?");
            stmt.setString(1, nombreCategoria);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return new Categoria(
                        rs.getString("nombre"),
                        rs.getString("imagen")
                );
            }
            rs.close();
        } catch (SQLException e) {
            lastError = e;
            log(Level.SEVERE, "Error al cargar la categoría " + nombreCategoria, e);
        }
        return null;
    }
    
    public void guardarProducto(Producto producto) {
        try {
        	String sql = "INSERT INTO producto (nombre, precio, categoriaNombre, rutaImagen) VALUES (?, ?, ?, ?)";
            PreparedStatement stmt = connection.prepareStatement(sql);
            stmt.setString(1, producto.getNombre());
            stmt.setDouble(2, producto.getPrecio());
            stmt.setString(3, producto.getCategoria().getNombre());
            stmt.setString(4, producto.getRutaImagen()); 
            stmt.executeUpdate();
            stmt.close();
        } catch (SQLException e) {
            lastError = e;
            log(Level.SEVERE, "Error al guardar producto", e);
        }
    }


    public void close() {
        try {
            if (statement != null) statement.close();
            if (connection != null) connection.close();
            log(Level.INFO, "Conexión a base de datos cerrada", null);
        } catch (SQLException e) {
            lastError = e;
            log(Level.SEVERE, "Error al cerrar conexión de base de datos", e);
        }
    }

    private void log(Level level, String msg, Throwable excepcion) {
        if (logger == null) {
            logger = Logger.getLogger("BD-log");
            logger.setLevel(Level.ALL);
            try {
                logger.addHandler(new FileHandler("bd.log.xml", true));
            } catch (Exception e) {
                logger.log(Level.SEVERE, "No se pudo crear fichero de log", e);
            }
        }
        if (excepcion == null) {
            logger.log(level, msg);
        } else {
            logger.log(level, msg, excepcion);
        }
    }
}
