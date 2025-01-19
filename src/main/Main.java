package main;

import javax.swing.SwingUtilities;


import db.ServicioPersistenciaBD;
import domain.CarritoCompras;
import domain.Producto;
import gui.VentanaSupermarket;

public class Main {
    public static ServicioPersistenciaBD servicioPersistenciaBD;

    public static void main(String[] args) {
        Thread initThread = new Thread(() -> {
            servicioPersistenciaBD = ServicioPersistenciaBD.getInstance();
            if (servicioPersistenciaBD.init("supermarket.db")) {
                System.out.println("Base de datos inicializada correctamente.");
            } else {
                System.err.println("Error al inicializar la base de datos.");
                return;
            }

         
        });

        initThread.start(); 

        SwingUtilities.invokeLater(() -> {
            try {
                initThread.join(); 
                manejarCarrito(); //lo pruebo segun acabe el hilo
                new VentanaSupermarket();
            } catch (InterruptedException e) {
                e.printStackTrace();
                System.err.println("Error al inicializar la interfaz gráfica.");
            }
        });
    }
    
    private static void manejarCarrito() {
        CarritoCompras carrito = new CarritoCompras();
        carrito.getProductos().put(new Producto(1, "Manzana", 1.2, "manzana.png", null), 5);
        carrito.getProductos().put(new Producto(2, "Pera", 0.8, "pera.png", null), 3);

        int totalProductos = carrito.contarProductosTotales();
        System.out.println("Cantidad total de productos en el carrito: " + totalProductos);
    }
}
