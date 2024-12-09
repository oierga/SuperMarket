package main;

import javax.swing.SwingUtilities;


import db.ServicioPersistenciaBD;
import domain.Producto;
import domain.TipoUsuario;
import domain.Usuario;
import gui.VentanaLogin;
import gui.VentanaSupermarket;

//la idea va un poco por aqui
import db.ServicioPersistenciaBD;

public class Main {
	public static ServicioPersistenciaBD servicioPersistenciaBD;
    public static void main(String[] args) {
    	
    	/*
    	String nombrePersistencia = "supermarket.db";
        String configPersistencia = "resources/";
       
        ServicioPersistenciaBD servicioPersistencia = new ServicioPersistenciaBD();
        
        if (servicioPersistencia.initDatosTest(nombrePersistencia, configPersistencia)) {
            System.out.println("Base de datos inicializada con datos de prueba.");
        } else {
            System.err.println("Error al inicializar la base de datos.");
            return;
        }

        System.out.println("Usuarios en la base de datos:");
        for (Usuario usuario : servicioPersistencia.cargarTodosUsuarios()) {
            System.out.println(usuario);
        }

        System.out.println("\nProductos en la base de datos:");
        for (Producto producto : servicioPersistencia.cargarTodosProductos()) {
            System.out.println(producto);
        }

        Usuario cliente = new Usuario("cliente", "5678", true, TipoUsuario.USUARIO);
        Producto producto = new Producto(1, "Manzana", 0.5, "", null, TipoCategoria.FRUTAS);

        if (servicioPersistencia.añadirProductoACarrito(producto, cliente, 2)) {
            System.out.println("\nProducto añadido al carrito:");
            System.out.println(producto);
        }

        if (servicioPersistencia.generarCompra(cliente)) {
            System.out.println("\nCompra generada con éxito para el usuario: " + cliente.getNombreDeUsuario());
        }

        servicioPersistencia.close();
        */
    	ServicioPersistenciaBD servicioBD = ServicioPersistenciaBD.getInstance();
    	servicioBD.init("supermarket.db");

    	
        new VentanaLogin(ServicioPersistenciaBD.getInstance());
        
        Usuario usuario = new Usuario("admin", "123", true, TipoUsuario.USUARIO);
        SwingUtilities.invokeLater(() -> {
            VentanaSupermarket ventana = new VentanaSupermarket(usuario);
            ventana.setVisible(true);
        });
        
        
    
        
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

