package domain;

public class Usuario {
	private int idUsuario;
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
    public void setTipo(TipoUsuario tipo) {
        this.tipo = tipo;
    }


	public int getIdUsuario() {
		return idUsuario;
	}


	public void setIdUsuario(int idUsuario) {
		this.idUsuario = idUsuario;
	}


	public void setNombreDeUsuario(String nombreDeUsuario) {
		this.nombreDeUsuario = nombreDeUsuario;
	}


	public void setContraseña(String contraseña) {
		this.contraseña = contraseña;
	}


	public void setActivo(boolean activo) {
		this.activo = activo;
	}
    
}
