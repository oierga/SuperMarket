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
                    "tipoUsuario STRING)");

            statement.executeUpdate("CREATE TABLE IF NOT EXISTS producto (" +
                    "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    "nombre STRING, " +
                    "precio DOUBLE, " +
                    "categoria STRING)");

            statement.executeUpdate("CREATE TABLE IF NOT EXISTS carrito (" +
                    "idUsuario INTEGER, " +
                    "idProducto INTEGER, " +
                    "cantidad INTEGER, " +
                    "FOREIGN KEY(idUsuario) REFERENCES usuario(id), " +
                    "FOREIGN KEY(idProducto) REFERENCES producto(id))");

            statement.executeUpdate("CREATE TABLE IF NOT EXISTS compra (" +
                    "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    "idUsuario INTEGER, " +
                    "fecha STRING, " +
                    "total DOUBLE, " +
                    "FOREIGN KEY(idUsuario) REFERENCES usuario(id))");

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
            statement.executeUpdate("INSERT OR IGNORE INTO usuario (nombre, nombreDeUsuario, contraseña, tipoUsuario) VALUES " +
                    "('Admin', 'admin', '1234', 'ADMIN'), " +
                    "('Cliente', 'cliente', '5678', 'USUARIO')");

            statement.executeUpdate("INSERT OR IGNORE INTO producto (nombre, precio, categoria) VALUES " +
                    "('Manzana', 0.5, 'Frutas'), " +
                    "('Pan', 1.0, 'Panadería'), " +
                    "('Leche', 1.2, 'Lácteos')");

            log(Level.INFO, "Datos de prueba inicializados correctamente", null);
            return true;

        } catch (SQLException e) {
            lastError = e;
            log(Level.SEVERE, "Error al inicializar datos de prueba", e);
            return false;
        }
    }

    public ArrayList<Usuario> cargarTodosUsuarios() {
        ArrayList<Usuario> usuarios = new ArrayList<>();
        try {
            ResultSet rs = statement.executeQuery("SELECT * FROM usuario");
            while (rs.next()) {
                TipoUsuario tipo = TipoUsuario.valueOf(rs.getString("tipoUsuario").toUpperCase());
                Usuario usuario = new Usuario(rs.getString("nombreDeUsuario"), rs.getString("contraseña"), true, tipo);
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
                String categoriaNombre = rs.getString("categoria").toUpperCase();

                TipoCategoria tipoCategoria = TipoCategoria.valueOf(categoriaNombre);
                
                Categoria categoria = new Categoria(tipoCategoria, categoriaNombre, "ruta/por/defecto.png");

                Producto producto = new Producto(
                    rs.getInt("id"),
                    rs.getString("nombre"),
                    rs.getDouble("precio"),
                    rs.getString("rutaImagen"),
                    categoria,
                    tipoCategoria
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
    
    public void guardarProducto(Producto producto) {
        try {
            String sql = "INSERT INTO producto (id, nombre, precio, rutaImagen, categoria) VALUES (?, ?, ?, ?, ?)";
            PreparedStatement stmt = connection.prepareStatement(sql);
            stmt.setInt(1, producto.getIdProducto());
            stmt.setString(2, producto.getNombre());
            stmt.setDouble(3, producto.getPrecio());
            stmt.setString(4, producto.getRutaImagen());
            stmt.setString(5, producto.getTipoCategoria().name()); 
            stmt.executeUpdate();
            stmt.close();
        } catch (SQLException e) {
            lastError = e;
            log(Level.SEVERE, "Error al guardar producto", e);
        }
    }

    public boolean añadirProductoACarrito(Producto producto, Usuario usuario, int cantidad) {
        try {
            String query = "INSERT INTO carrito (idUsuario, idProducto, cantidad) VALUES " +
                    "((SELECT id FROM usuario WHERE nombreDeUsuario = ?), ?, ?)";
            PreparedStatement ps = connection.prepareStatement(query);
            ps.setString(1, usuario.getNombreDeUsuario());
            ps.setInt(2, producto.getIdProducto());
            ps.setInt(3, cantidad);
            ps.executeUpdate();
            ps.close();
            log(Level.INFO, "Producto añadido al carrito", null);
            return true;
        } catch (SQLException e) {
            lastError = e;
            log(Level.SEVERE, "Error al añadir producto al carrito", e);
            return false;
        }
    }

    public boolean generarCompra(Usuario usuario) {
        try {
            connection.setAutoCommit(false);
            String insertCompra = "INSERT INTO compra (idUsuario, fecha, total) VALUES " +
                    "((SELECT id FROM usuario WHERE nombreDeUsuario = ?), datetime('now'), (SELECT SUM(p.precio * c.cantidad) FROM carrito c " +
                    "JOIN producto p ON c.idProducto = p.id WHERE c.idUsuario = (SELECT id FROM usuario WHERE nombreDeUsuario = ?)))";
            PreparedStatement ps = connection.prepareStatement(insertCompra);
            ps.setString(1, usuario.getNombreDeUsuario());
            ps.setString(2, usuario.getNombreDeUsuario());
            ps.executeUpdate();

            String vaciarCarrito = "DELETE FROM carrito WHERE idUsuario = (SELECT id FROM usuario WHERE nombreDeUsuario = ?)";
            ps = connection.prepareStatement(vaciarCarrito);
            ps.setString(1, usuario.getNombreDeUsuario());
            ps.executeUpdate();

            connection.commit();
            ps.close();
            log(Level.INFO, "Compra generada con éxito", null);
            return true;

        } catch (SQLException e) {
            try {
                connection.rollback();
            } catch (SQLException rollbackEx) {
                log(Level.SEVERE, "Error al hacer rollback", rollbackEx);
            }
            lastError = e;
            log(Level.SEVERE, "Error al generar compra", e);
            return false;
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
