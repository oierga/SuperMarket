package classes;

public class Usuario {
    private String nombreDeUsuario;
    private String contraseña;
    private boolean activo;
    private TipoUsuario tipo;

    public Usuario(String nombreDeUsuario, String contraseña, boolean activo) {
        this.nombreDeUsuario = nombreDeUsuario;
        this.contraseña = contraseña;
        this.activo = activo;
    }

  
    public Usuario(String nombreDeUsuario, String contraseña, boolean activo, TipoUsuario tipo) {
        this.nombreDeUsuario = nombreDeUsuario;
        this.contraseña = contraseña;
        this.activo = activo;
        this.tipo = tipo;
    }

    public String getNombreDeUsuario() {
        return nombreDeUsuario;
    }

    public String getContraseña() {
        return contraseña;
    }

    public boolean getActivo() {
        return activo;
    }

    public TipoUsuario getTipo() {
        return tipo;
    }
}
