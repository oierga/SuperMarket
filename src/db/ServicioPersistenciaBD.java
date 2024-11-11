package db;

import domain.Producto;

import java.sql.*;
import java.util.ArrayList;

public class ServicioPersistenciaBD {

}

    /*private static Connection getConnection() {
        //EStablecer conexión con DB. No tenemos DB todavia.
    }*/

   /* public static Producto[] cargarProductos() {
        String query = "SELECT idProducto, nombre, precio FROM PRODUCTO";
        ArrayList<Producto> listaProductos = new ArrayList<>();

        try (Connection conn = getConnection()) {
            if (conn != null) { // Verificar que la conexión no sea null
                try (Statement stmt = conn.createStatement();
                     ResultSet rs = stmt.executeQuery(query)) {

                    while (rs.next()) {
                        int idProducto = rs.getInt("idProducto");
                        String nombre = rs.getString("nombre");
                        int precio = rs.getInt("precio");

                        Producto producto = new Producto(idProducto, nombre, precio);
                        listaProductos.add(producto);
                    }
                }
            }
        } catch (SQLException e) {
            System.err.println("Error al cargar productos.");
            e.printStackTrace();
        }

        return listaProductos.toArray(new Producto[0]);
    }

    public static Usuario[] cargarUsuarios() {
        String query = "SELECT * FROM USUARIO";
        ArrayList<Usuario> listaUsuarios = new ArrayList<>();

        try (Connection conn = getConnection()) {
            if (conn != null) { // Verificar que la conexión no sea null
                try (Statement stmt = conn.createStatement();
                     ResultSet rs = stmt.executeQuery(query)) {

                    while (rs.next()) {
                        int idUsuario = rs.getInt("idUsuario");
                        String nombre = rs.getString("nombre");
                        int puntos = rs.getInt("puntos");

                        Usuario usuario = new Usuario(idUsuario, nombre, puntos);
                        listaUsuarios.add(usuario);
                    }
                }
            }
        } catch (SQLException e) {
            System.err.println("Error al cargar usuarios.");
            e.printStackTrace();
        }

        return listaUsuarios.toArray(new Usuario[0]);
    }

    public static class Usuario {
        private int idUsuario;
        private String nombre;
        private int puntos;

        public Usuario(int idUsuario, String nombre, int puntos) {
            this.idUsuario = idUsuario;
            this.nombre = nombre;
            this.puntos = puntos;
        }

        public int getIdUsuario() {
            return idUsuario;
        }

        public void setIdUsuario(int idUsuario) {
            this.idUsuario = idUsuario;
        }

        public String getNombre() {
            return nombre;
        }

        public void setNombre(String nombre) {
            this.nombre = nombre;
        }

        public int getPuntos() {
            return puntos;
        }

        public void setPuntos(int puntos) {
            this.puntos = puntos;
        }
    }

    public static void main(String[] args) {
        Producto[] productos = cargarProductos();
        Usuario[] usuarios = cargarUsuarios();

        System.out.println("Productos:");
        for (Producto producto : productos) {
            System.out.println("ID: " + producto.getIdProducto() + ", Nombre: " + producto.getNombre() + ", Precio: " + producto.getPrecio());
        }

        System.out.println("\nUsuarios:");
        for (Usuario usuario : usuarios) {
            System.out.println("ID: " + usuario.getIdUsuario() + ", Nombre: " + usuario.getNombre() + ", Puntos: " + usuario.getPuntos());
        }
    }
    public static Producto obtenerProductoPorId(int idProducto) {
        String query = "SELECT idProducto, nombre, precio FROM PRODUCTO WHERE idProducto = ?";
        
        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            
            stmt.setInt(1, idProducto);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                String nombre = rs.getString("nombre");
                int precio = rs.getInt("precio");
                return new Producto(idProducto, nombre, precio);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null; // Retorna null si el producto no existe
    }

}*/
