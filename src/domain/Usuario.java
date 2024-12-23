package domain;

public class Usuario {
	private int idUsuario;
    private String nombreDeUsuario;
    private String contrasena;
    private boolean activo;
    private TipoUsuario tipo;

    public Usuario(String nombreDeUsuario, String contraseña, boolean activo) {
        this.nombreDeUsuario = nombreDeUsuario;
        this.contrasena = contraseña;
        this.activo = activo;
    }

  
    public Usuario(String nombreDeUsuario, String contraseña, boolean activo, TipoUsuario tipo) {
        this.nombreDeUsuario = nombreDeUsuario;
        this.contrasena = contraseña;
        this.activo = activo;
        this.tipo = tipo;
    }

    public Usuario(int int1, String usuario, String contra, int i) {
    	this.idUsuario = int1;
		// TODO Auto-generated constructor stub
    	this.nombreDeUsuario=usuario;
    	this.contrasena = contra;
    	this.activo = true;
	}


	public Usuario() {
		// TODO Auto-generated constructor stub
	}


	public String getNombreDeUsuario() {
        return nombreDeUsuario;
    }

    public String getContrasena() {
        return contrasena;
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


	public void setContrasena(String contrasena) {
		this.contrasena = contrasena;
	}


	public void setActivo(boolean activo) {
		this.activo = activo;
	}
    
}
