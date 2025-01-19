package main;

import javax.swing.SwingUtilities;

import db.ServicioPersistenciaBD;
import domain.Producto;
import domain.TipoUsuario;
import domain.Usuario;
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
                new VentanaSupermarket();
            } catch (InterruptedException e) {
                e.printStackTrace();
                System.err.println("Error al inicializar la interfaz gráfica.");
            }
        });
    }
}
