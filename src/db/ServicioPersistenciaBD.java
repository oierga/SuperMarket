package db;

import domain.Producto;

import domain.TipoUsuario;
import domain.Categoria;
import domain.Usuario;
import domain.Venta;

import java.io.File;
import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.FileHandler;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ServicioPersistenciaBD {
    private static final String DB_PATH = "data/supermarket.db";  // Ruta del archivo de la base de datos
    private static final String JDBC_URL = "jdbc:sqlite:" + DB_PATH;
    @SuppressWarnings("unused")
	private static Exception lastError = null;
    private static ServicioPersistenciaBD instance; 
    private Connection connection;
    private Logger logger = null;
    private Usuario usuario ;

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
                "contrasena TEXT, " +
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
                "cantidad INTEGER, " +
                "FOREIGN KEY(idUsuario) REFERENCES usuario(idUsuario), " +
                "FOREIGN KEY(idProducto) REFERENCES producto(id))");
        
        statement.executeUpdate("CREATE TABLE IF NOT EXISTS clientes (" +
        	    "idCliente INTEGER PRIMARY KEY AUTOINCREMENT, " +
        	    "nombre TEXT NOT NULL, " +
        	    "tipoUsuario INTEGER NOT NULL CHECK(tipoUsuario IN (0, 1, 2)) -- 0: Usuario, 1: Socio, 2: Admin" +
        	");");


        log(Level.INFO, "Tablas creadas correctamente", null);
    }

    // Método para inicializar datos de prueba
    public boolean initDatosTest() {
        try (Statement statement = connection.createStatement()) {
            log(Level.INFO, "Cargando datos de prueba...", null);
            statement.executeUpdate("INSERT INTO usuario (nombreDeUsuario, contrasena, activo) VALUES ('admin', '1234', 1),('cliente', '5678', 1)");

            log(Level.INFO, "Usuarios insertados correctamente", null);

            statement.executeUpdate("INSERT INTO categoria (nombre, imagen) VALUES ('Frutas', 'images/frutas.png'), ('Panadería', 'images/panaderia.png'), ('Lácteos', 'images/lacteos.png')");

            log(Level.INFO, "Categorías insertadas correctamente", null);

            statement.executeUpdate("INSERT INTO producto (nombre, precio, categoria, rutaImagen) VALUES ('Manzana', 0.5, 1, 'images/manzana.png'),('Pan', 1.0, 2, 'images/pan.png'), ('Leche', 1.2, 3, 'images/leche.png')");

            log(Level.INFO, "Productos insertados correctamente", null);
            
            statement.executeUpdate("INSERT INTO clientes (nombre, tipoUsuario) VALUES " +
            	    "('Usuario Básico', 0), " +
            	    "('Socio', 1), " +
            	    "('Administrador', 2);");

            	log(Level.INFO, "Clientes insertados correctamente", null);

            	
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
                		rs.getInt("id"),
                        rs.getString("nombreDeUsuario"),
                        rs.getString("contrasena"),
                        rs.getBoolean("activo"),
                        TipoUsuario.USUARIO
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
    
    //metodo carga de clientes, es decir, usuarios
    public List<Map<String, Object>> cargarClientes() {
        List<Map<String, Object>> clientes = new ArrayList<>();
        String sql = "SELECT * FROM clientes";
        try (Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                Map<String, Object> cliente = new HashMap<>();
                cliente.put("idCliente", rs.getInt("idCliente"));
                cliente.put("nombre", rs.getString("nombre"));
                cliente.put("tipoUsuario", rs.getInt("tipoUsuario"));
                clientes.add(cliente);
            }
        } catch (SQLException e) {
            lastError = e;
            log(Level.SEVERE, "Error al cargar los clientes", e);
        }
        return clientes;
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
        conectar();
        try (
             PreparedStatement stmt = connection.prepareStatement(query)) {

            // Establecer los parámetros de la consulta
            stmt.setString(1, usuario);
            stmt.setString(2, contra);
            // Ejecutar la consulta
            ResultSet rs = stmt.executeQuery();
            // Verificar si se encuentra un usuario que coincida con las credenciales
            if (rs.next()) {
                // Si se encontró un usuario, las credenciales son válidas
                System.out.print("Usuario verificado");

            	instance.setUsuario(new Usuario(usuario,contra,true,TipoUsuario.SOCIO));
                return true;
            }else {

            	
            	return false;
            }

        } catch (SQLException e) {
            log(Level.SEVERE, "Error al verificar las credenciales", e);
            return false;
        }

        // Si no se encontró el usuario o las credenciales no coinciden
        
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
    public void actualizarUsuario(String nombre,Usuario usuario) {
        String sql = "UPDATE usuario SET nombreDeUsuario = ?, contrasena = ?, activo = ? WHERE nombreDeUsuario = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            // Establecer los valores para la actualización
            stmt.setString(1, usuario.getNombreDeUsuario());
            stmt.setString(2, usuario.getContrasena());
            stmt.setBoolean(3, usuario.getActivo());
            stmt.setString(4, nombre); // Suponiendo que el nombre anterior es la clave para buscar

            int filasActualizadas = stmt.executeUpdate();
            if (filasActualizadas > 0) {
                log(Level.INFO, "Usuario actualizado correctamente: " + usuario.getNombreDeUsuario(), null);
            } else {
                log(Level.WARNING, "No se encontró el usuario para actualizar: " + usuario.getNombreDeUsuario(), null);
            }
        } catch (SQLException e) {
            lastError = e;
            log(Level.SEVERE, "Error al actualizar usuario: " + usuario.getNombreDeUsuario(), e);
        }
    }

    public void eliminarUsuario(Usuario usuario) {
        try (PreparedStatement stmt = connection.prepareStatement("DELETE FROM usuario (nombreDeUsuario, contrasena, activo) VALUES (?, ?, ?)")) {
            stmt.setString(1, usuario.getNombreDeUsuario());
            stmt.setString(2, usuario.getContrasena());
            stmt.setBoolean(3, usuario.getActivo());
            stmt.executeUpdate();
            log(Level.INFO, "Usuario eliminado correctamente: " + usuario.getNombreDeUsuario(), null);
        } catch (SQLException e) {
            lastError = e;
            log(Level.SEVERE, "Error al eliminar usuario: " + usuario.getNombreDeUsuario(), e);
        }
    }
    // Método para guardar un usuario en la base de datos
    public void guardarUsuario(Usuario usuario) {
        try (PreparedStatement stmt = connection.prepareStatement("INSERT INTO usuario (nombreDeUsuario, contrasena, activo) VALUES (?, ?, ?)")) {
            stmt.setString(1, usuario.getNombreDeUsuario());
            stmt.setString(2, usuario.getContrasena());
            stmt.setBoolean(3, usuario.getActivo());
            stmt.executeUpdate();
            log(Level.INFO, "Usuario guardado correctamente: " + usuario.getNombreDeUsuario(), null);
        } catch (SQLException e) {
            lastError = e;
            log(Level.SEVERE, "Error al guardar usuario: " + usuario.getNombreDeUsuario(), e);
        }
    }
    
    //metodo pa guardar cliente
    public void guardarCliente(String nombre, int tipoUsuario) {
        String sql = "INSERT INTO clientes (nombre, tipoUsuario) VALUES (?, ?)";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, nombre);
            stmt.setInt(2, tipoUsuario);
            stmt.executeUpdate();
            log(Level.INFO, "Cliente guardado correctamente: " + nombre, null);
        } catch (SQLException e) {
            lastError = e;
            log(Level.SEVERE, "Error al guardar cliente: " + nombre, e);
        }
    }
    
    public int guardarClienteYObtenerId(String nombre, int tipoUsuario) {
        String sql = "INSERT INTO clientes (nombre, tipoUsuario) VALUES (?, ?)";
        try (PreparedStatement stmt = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            stmt.setString(1, nombre);
            stmt.setInt(2, tipoUsuario);
            stmt.executeUpdate();

            //obtiene el id generado
            try (ResultSet generatedKeys = stmt.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    int idCliente = generatedKeys.getInt(1);
                    log(Level.INFO, "Cliente guardado con ID: " + idCliente, null);
                    return idCliente;
                }
            }
        } catch (SQLException e) {
            lastError = e;
            log(Level.SEVERE, "Error al guardar cliente: " + nombre, e);
        }
        return -1; //en caso de error
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
    public boolean guardarVenta(int idUsuario, int idProducto, int cantidad, String fecha) {
        try (PreparedStatement stmt = connection.prepareStatement("INSERT INTO carrito(idUsuario, idProducto, cantidad, fecha) VALUES (?,?,?,?)")){
        	stmt.setInt(1, idUsuario);
        	stmt.setInt(2, idProducto);
        	stmt.setInt(3, cantidad);
        	stmt.setString(4, ""+LocalDate.now());
        	stmt.executeUpdate();
        	System.out.print("Compra guardada");
        	return true;
        } catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

    	
    	
    	return false;
    }
    public Producto productoPorNombre(String nombre) {
    	ArrayList<Producto> productos = instance.cargarTodosProductos();
    	Producto productoEncontrado = null;
    	for (Producto producto: productos) {
    		if (producto.getNombre().contains(nombre.toLowerCase())) {
    			productoEncontrado = producto;
    		}
    	}
    	return productoEncontrado;
    }

	public Usuario getUsuario() {
		return usuario;
	}

	public void setUsuario(Usuario usuario) {
		this.usuario = usuario;
	}

	public Map<String, Double> obtenerNombresConDescuento() {
		// TODO Auto-generated method stub
		Map<String, Double> nombresConDescuento = new HashMap<>();
		try(Statement stmt = connection.createStatement()){
			
			ResultSet rs = stmt.executeQuery("SELECT * FROM descuentos");
			while (rs.next()) {
				String nombre = rs.getString(1).toLowerCase();
				double descuento = rs.getDouble(2);
				nombresConDescuento.put(nombre, descuento);
			}
		
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return nombresConDescuento;
	}
	public List<Venta> cargarVentas() {
	    List<Venta> ventas = new ArrayList<>();
	    String query = "SELECT idUsuario, idProducto, cantidad, fecha FROM carrito";

	    try (PreparedStatement stmt = connection.prepareStatement(query);
	         ResultSet rs = stmt.executeQuery()) {

	        while (rs.next()) {
	            int idUsuario = rs.getInt("idUsuario");
	            int idProducto = rs.getInt("idProducto");
	            int cantidad = rs.getInt("cantidad");
	            String fecha = rs.getString("fecha");

	            Venta venta = new Venta(idUsuario, idProducto, cantidad, fecha);
	            ventas.add(venta);
	        }
	    } catch (SQLException e) {
	        e.printStackTrace();
	        log(Level.SEVERE, "Error al cargar las ventas de la base de datos.", e);
	    }

	    return ventas;
	}
	public Producto obtenerProductoPorId(int id) {
    	conectar();
        Producto producto = null;
        try (Statement statement = connection.createStatement()) {
            ResultSet rs = statement.executeQuery("SELECT * FROM producto WHERE id = "+id);
            while (rs.next()) {
                Categoria categoria = cargarCategoria(rs.getInt("idCategoria"));
                producto = new Producto(
                        rs.getInt("id"),
                        rs.getString("nombre"),
                        rs.getDouble("precio"),
                        rs.getString("rutaImagen"),
                        categoria
                );
               
            }
            rs.close();
        } catch (SQLException e) {
            lastError = e;
            log(Level.SEVERE, "Error al cargar todos los productos", e);
        }
        return producto;
    }
}
