package db;

import domain.Producto;
import domain.Categoria;
import domain.Usuario;

import java.io.File;
import java.sql.*;
import java.util.ArrayList;
import java.util.logging.FileHandler;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ServicioPersistenciaBD {
    private static final String DB_PATH = "data/supermarket.db";  // Ruta del archivo de la base de datos
    private static final String JDBC_URL = "jdbc:sqlite:" + DB_PATH;
    private static Exception lastError = null;
    private static ServicioPersistenciaBD instance; 
    private Connection connection;
    private Logger logger = null;

    // Constructor para establecer la conexión con la base de datos
    private ServicioPersistenciaBD() {
        try {
            File dataDir = new File("data");
            if (!dataDir.exists() && !dataDir.mkdir()) {
                log(Level.SEVERE, "No se pudo crear la carpeta 'data'", null);
            }
            connection = DriverManager.getConnection(JDBC_URL); // Conexión a la base de datos ya existente
            log(Level.INFO, "Conexión establecida a la base de datos.", null);
        } catch (SQLException e) {
            e.printStackTrace();
            log(Level.SEVERE, "Error al establecer la conexión", e);
        }
    }

    // Método estático para acceder a la instancia
    public static synchronized ServicioPersistenciaBD getInstance() {
        if (instance == null) {
            instance = new ServicioPersistenciaBD();
        }
        return instance;
    }

    // Método para obtener la conexión
    public Connection getConnection() {
        return connection;
    }

    // Método para cerrar la conexión
    public void closeConnection() {
        try {
            if (connection != null) {
                connection.close();
            }
        } catch (SQLException e) {
            e.printStackTrace();
            log(Level.SEVERE, "Error al cerrar la conexión", e);
        }
    }

    // Método para inicializar la base de datos y crear tablas si no existen
    public boolean init(String nombrePersistencia) {
        String dbPath = "data/" + nombrePersistencia;
        File dbFile = new File(dbPath);

        try (Connection connection = DriverManager.getConnection("jdbc:sqlite:" + dbPath);
             Statement statement = connection.createStatement()) {

            if (!dbFile.exists()) {
                log(Level.INFO, "Base de datos no encontrada, creando...", null);
                // Si la base de datos no existe, creamos las tablas
                createTables(statement);
            } else {
                log(Level.INFO, "Base de datos ya existe, conexión establecida.", null);
            }
            return true;
        } catch (SQLException e) {
            lastError = e;
            log(Level.SEVERE, "Error en conexión o creación de la base de datos", e);
            return false;
        }
    }

    // Método para crear tablas si la base de datos no existe
    private void createTables(Statement statement) throws SQLException {
        statement.executeUpdate("CREATE TABLE IF NOT EXISTS categoria (" +
                "idCategoria INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "nombre TEXT, " +
                "imagen TEXT)");

        statement.executeUpdate("CREATE TABLE IF NOT EXISTS usuario (" +
                "idUsuario INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "nombreDeUsuario TEXT UNIQUE, " +
                "contraseña TEXT, " +
                "activo INTEGER)");

        statement.executeUpdate("CREATE TABLE IF NOT EXISTS producto (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "nombre TEXT, " +
                "precio DOUBLE, " +
                "categoria INTEGER, " +
                "rutaImagen TEXT, " +
                "FOREIGN KEY(categoria) REFERENCES categoria(idCategoria))");

        statement.executeUpdate("CREATE TABLE IF NOT EXISTS carrito (" +
                "idUsuario INTEGER, " +
                "idProducto INTEGER, " +
                "FOREIGN KEY(idUsuario) REFERENCES usuario(idUsuario), " +
                "FOREIGN KEY(idProducto) REFERENCES producto(id))");

        log(Level.INFO, "Tablas creadas correctamente", null);
    }

    // Método para inicializar datos de prueba
    public boolean initDatosTest() {
        try (Statement statement = connection.createStatement()) {
            log(Level.INFO, "Cargando datos de prueba...", null);
            statement.executeUpdate("INSERT INTO usuario (nombreDeUsuario, contraseña, activo) VALUES ('admin', '1234', 1),('cliente', '5678', 1)");

            log(Level.INFO, "Usuarios insertados correctamente", null);

            statement.executeUpdate("INSERT INTO categoria (nombre, imagen) VALUES ('Frutas', 'images/frutas.png'), ('Panadería', 'images/panaderia.png'), ('Lácteos', 'images/lacteos.png')");

            log(Level.INFO, "Categorías insertadas correctamente", null);

            statement.executeUpdate("INSERT INTO producto (nombre, precio, categoria, rutaImagen) VALUES ('Manzana', 0.5, 1, 'images/manzana.png'),('Pan', 1.0, 2, 'images/pan.png'), ('Leche', 1.2, 3, 'images/leche.png')");

            log(Level.INFO, "Productos insertados correctamente", null);
            return true;

        } catch (SQLException e) {
            lastError = e;
            log(Level.SEVERE, "Error al inicializar datos de prueba", e);
            return false;
        }
    }

    // Método para cargar todos los usuarios desde la base de datos
    public ArrayList<Usuario> cargarTodosUsuarios() {
        ArrayList<Usuario> usuarios = new ArrayList<>();
        try (Statement statement = connection.createStatement()) {
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
    public void conectar() {
        try {
            String dbUrl = "jdbc:sqlite:data/supermarket.db";
            connection = DriverManager.getConnection(dbUrl);
            if (connection != null) {
                System.out.println("Conexión exitosa a la base de datos.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
            log(Level.SEVERE, "Error al conectar a la base de datos", e);
        }
    }
    public ArrayList<Producto> cargarTodosProductos() {
    	conectar();
        ArrayList<Producto> productos = new ArrayList<>();
        try (Statement statement = connection.createStatement()) {
            ResultSet rs = statement.executeQuery("SELECT * FROM producto");
            while (rs.next()) {
                Categoria categoria = cargarCategoria(rs.getInt("idCategoria"));
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
    public boolean verificarCredenciales(String usuario, String contra) {
        // Consulta para buscar el usuario por su nombre de usuario
        String query = "SELECT * FROM usuario WHERE nombreDeUsuario = ? AND contrasena = ?";

        try (Connection connection = getConnection();
             PreparedStatement stmt = connection.prepareStatement(query)) {

            // Establecer los parámetros de la consulta
            stmt.setString(1, usuario);
            stmt.setString(2, contra);

            // Ejecutar la consulta
            ResultSet rs = stmt.executeQuery();

            // Verificar si se encuentra un usuario que coincida con las credenciales
            if (rs.next()) {
                // Si se encontró un usuario, las credenciales son válidas
                return true;
            }

        } catch (SQLException e) {
            log(Level.SEVERE, "Error al verificar las credenciales", e);
        }

        // Si no se encontró el usuario o las credenciales no coinciden
        return false;
    }


    // Método para cargar una categoría desde la base de datos
    public Categoria cargarCategoria(int idCategoria) {
        try (PreparedStatement stmt = connection.prepareStatement("SELECT * FROM categoria WHERE idCategoria = ?")) {
            stmt.setInt(1, idCategoria);
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
            log(Level.SEVERE, "Error al cargar la categoría con ID: " + idCategoria, e);
        }
        return null;
    }

    // Método para guardar un usuario en la base de datos
    public void guardarUsuario(Usuario usuario) {
        try (PreparedStatement stmt = connection.prepareStatement("INSERT INTO usuario (nombreDeUsuario, contraseña, activo) VALUES (?, ?, ?)")) {
            stmt.setString(1, usuario.getNombreDeUsuario());
            stmt.setString(2, usuario.getContraseña());
            stmt.setBoolean(3, usuario.getActivo());
            stmt.executeUpdate();
            log(Level.INFO, "Usuario guardado correctamente: " + usuario.getNombreDeUsuario(), null);
        } catch (SQLException e) {
            lastError = e;
            log(Level.SEVERE, "Error al guardar usuario: " + usuario.getNombreDeUsuario(), e);
        }
    }

    // Método para registrar eventos en el log
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
