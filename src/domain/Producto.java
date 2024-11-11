package domain;

import java.util.Map;

public class Producto {

    private int idProducto;   // Campo para el identificador único
    private String nombre;
    private double precio;
    private String rutaImagen;
    private Categoria categoria;
    
    public Producto(int idProducto, String nombre, double precio, String rutaImagen, Categoria categoria) {
        this.idProducto = idProducto;
        this.nombre = nombre;
        this.precio = precio;
        this.rutaImagen = rutaImagen;
        this.categoria = categoria;
    }

    public Producto(String nombre2, String precio2) {
    	this.nombre = nombre2;
    	String precioAux = precio2.substring(0, precio2.length() - 1);
    	double precioAux2 = Double.parseDouble(precioAux);
    	this.precio = precioAux2;
		// TODO Auto-generated constructor stub
	}

	public Producto(String nombre2, double precio2) {
		// TODO Auto-generated constructor stub
		this.nombre = nombre2;
		this.precio = precio2;
	}

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

    public double getPrecio() {
        return precio;
    }

    public void setPrecio(double precio) {
        this.precio = precio;
    }
    
    public String getRutaImagen() {
        return rutaImagen;
    }

    public void setRutaImagen(String rutaImagen) {
        this.rutaImagen = rutaImagen;
    }

    public Categoria getCategoria() {
        return categoria;
    }

    public void setCategoria(Categoria categoria) {
        this.categoria = categoria;
    }


    @Override
    public String toString() {
        return "Producto [idProducto=" + idProducto + ", nombre=" + nombre + ", precio=" + precio + ", categoria=" + categoria.getNombre() + "]";
    }

    //esto habra q borrar
    public static Producto parseCSV(String line, Map<String, Categoria> categorias) {
        String[] parts = line.split(",");
        
        
        int idProducto = Integer.parseInt(parts[0].replaceAll("\"", ""));
        String nombre = parts[1].replaceAll("\"", "");
        double precio = Double.parseDouble(parts[2].replaceAll("\"", ""));
        String categoriaNombre = parts[3].replaceAll("\"", "");
        String rutaImagen = parts[4].replaceAll("\"", ""); 

        Categoria categoria = categorias.get(categoriaNombre);
        
        return new Producto(idProducto, nombre, precio, rutaImagen, categoria);
    }

}
