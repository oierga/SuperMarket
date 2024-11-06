package gui;

import db.ServicioPersistenciaBD;
import domain.Producto;

import javax.swing.*;
import java.awt.*;

public class VentanaCarrito extends JFrame {

    public VentanaCarrito(int[] idEnCarrito) {
        // Configuración de la ventana
        setTitle("Carrito de Compras");
        setSize(400, 300);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null); // Centrar la ventana

        // Panel principal para los productos del carrito
        JPanel panelCarrito = new JPanel();
        panelCarrito.setLayout(new BoxLayout(panelCarrito, BoxLayout.Y_AXIS));
        panelCarrito.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10)); // Margen alrededor

        // Título
        JLabel labelTitulo = new JLabel("Productos en el Carrito:");
        labelTitulo.setFont(new Font("Arial", Font.BOLD, 16));
        panelCarrito.add(labelTitulo);
        panelCarrito.add(Box.createRigidArea(new Dimension(0, 10))); // Espacio entre el título y la lista

        // Variable de control para verificar si se encontraron productos
        boolean productosEncontrados = false;

        // Cargar y mostrar cada producto basado en su ID en el carrito
        for (int idProducto : idEnCarrito) {
            Producto producto = ServicioPersistenciaBD.obtenerProductoPorId(idProducto);

            if (producto != null) {
                productosEncontrados = true;
                
                // Crear un panel para cada producto en el carrito
                JPanel productoPanel = new JPanel(new BorderLayout());
                JLabel labelNombre = new JLabel("Nombre: " + producto.getNombre());
                JLabel labelPrecio = new JLabel("Precio: $" + producto.getPrecio());

                productoPanel.add(labelNombre, BorderLayout.NORTH);
                productoPanel.add(labelPrecio, BorderLayout.SOUTH);
                productoPanel.setBorder(BorderFactory.createMatteBorder(0, 0, 1, 0, Color.LIGHT_GRAY)); // Línea separadora

                panelCarrito.add(productoPanel);
                panelCarrito.add(Box.createRigidArea(new Dimension(0, 10))); // Espacio entre productos
            } else {
                // Mensaje de depuración si no se encuentra el producto en la base de datos
                System.out.println("Producto con ID " + idProducto + " no encontrado en la base de datos.");
            }
        }

        // Mostrar mensaje si no hay productos en el carrito
        if (!productosEncontrados) {
            JLabel labelVacio = new JLabel("No hay productos en el carrito.");
            labelVacio.setHorizontalAlignment(SwingConstants.CENTER);
            panelCarrito.add(labelVacio);
        }

        // Añadir el panel principal a la ventana
        add(new JScrollPane(panelCarrito), BorderLayout.CENTER);
    }

    // Método principal para abrir la ventana de carrito en un ejemplo
    public static void main(String[] args) {
        // Ejemplo de IDs de productos en el carrito
        int[] idEnCarrito = {1, 2, 3}; // Ejemplo de IDs en el carrito

        SwingUtilities.invokeLater(() -> {
            VentanaCarrito ventanaCarrito = new VentanaCarrito(idEnCarrito);
            ventanaCarrito.setVisible(true); // Hacer visible la ventana del carrito
        });
    }
}
