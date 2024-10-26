package classes;

import java.util.ArrayList;
import java.util.List;

//temporal

public class ServicioPersistenciaBD {
    private List<Usuario> usuarios;

    public ServicioPersistenciaBD() {
        
        usuarios = new ArrayList<>();
      
        usuarios.add(new Usuario("admin", "admin123", true, TipoUsuario.ADMIN));
    }

    public ArrayList<Usuario> cargarTodosUsuarios() {
        return new ArrayList<>(usuarios);
    }

    public void guardarUsuario(Usuario usuario) {
        usuarios.add(usuario);
    }
}
