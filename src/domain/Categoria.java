package domain;

public class Categoria {
    private String nombre;
    //imagen asociada a categoria
    private String imagen;

    public Categoria(String nombre, String imagen) {
        this.nombre = nombre;
        this.imagen = imagen;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }
    
    public String getImagen() {
        return imagen;
    }

    public void setImagen(String imagen) {
        this.imagen = imagen;
    }

    @Override
    public String toString() {
        return "Categoria{" + "nombre='" + nombre + '\'' + ", imagen='" + imagen + '\'' +'}';
    }
}
