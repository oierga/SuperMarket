package domain;

public class Producto {

    private int idProducto;   // Campo para el identificador único
    private String nombre;
    private int precio;

    // Constructor actualizado con el idProducto
    public Producto(int idProducto, String nombre, int precio) {
        this.idProducto = idProducto;
        this.nombre = nombre;
        this.precio = precio;
    }

    // Getters y Setters
    public int getIdProducto() {
        return idProducto;
    }

    public void setIdProducto(int idProducto) {
        this.idProducto = idProducto;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public int getPrecio() {
        return precio;
    }

    public void setPrecio(int precio) {
        this.precio = precio;
    }

    // Método toString para mostrar información del producto
    @Override
    public String toString() {
        return "Producto [idProducto=" + idProducto + ", nombre=" + nombre + ", precio=" + precio + "]";
    }

    // Método para parsear una línea CSV en un Producto
    public static Producto parseCSV(String line) {
        // Dividir la línea por comas y eliminar comillas innecesarias
        String[] parts = line.split(",");
        int idProducto = Integer.parseInt(parts[0].replaceAll("\"", ""));
        String nombre = parts[1].replaceAll("\"", "");
        int precio = Integer.parseInt(parts[2].replaceAll("\"", "")); // Asumiendo que el precio está en la tercera columna

        // Crear y retornar el objeto Producto con los datos obtenidos
        return new Producto(idProducto, nombre, precio);
    }
}
