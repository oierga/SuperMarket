package classes;

//la idea va un poco por aqui


public class Main {
    public static void main(String[] args) {
       
        ServicioPersistenciaBD servicioPersistencia = new ServicioPersistenciaBD();
        
        VentanaInicio ventanaInicio = new VentanaInicio(servicioPersistencia);
    
        
        /*
        if (ventanaInicio.isAuthenticated()) {
            // Usuario autenticado, abrir ventana principal
            Supermarket supermarket = new Supermarket(servicioPersistencia);
            Usuario usuarioAutenticado = ventanaInicio.getUsuarioAutenticado();
            
            // Aquí abres la Ventana Principal (por diseñar)
            new VentanaSupermarket(supermarket, usuarioAutenticado, servicioPersistencia);
        } else {
            // Si la autenticación falla o se cierra, mensaje de error o salida
            System.out.println("Autenticación fallida o cancelada.");
        }
        
        */
        
        
        //meter funcion para lo de tipo de usuario para boton editar en supermarket/tienda o si no crear las funciones en la propia clase usuario
   
        
       
    }
}

