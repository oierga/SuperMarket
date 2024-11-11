package gui;

import javax.swing.*;
import domain.Producto;
import domain.TipoUsuario;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Arrays;

public class VentanaProducto extends JFrame {
    private String nombre;
    private double precio;
    private String rutaImagen;
    private int[] idEnCarrito;
    
    public VentanaProducto(Producto producto, int[] idEnCarritoGeneral) {
        // Asignación de datos del producto
        this.nombre = "Nombre prueba";
        this.precio = 789.00;
        this.rutaImagen = "ruta/a/la/imagen"; // Coloca aquí la ruta predeterminada de imagen o específica del producto
        this.idEnCarrito = Arrays.copyOf(idEnCarritoGeneral, idEnCarritoGeneral.length + 1);

        // Configuración de la ventana
        setTitle("Detalles del Producto");
        setSize(400, 600);
        setLocationRelativeTo(null); // Centrar la ventana
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        // Crear el panel principal
        JPanel panelPrincipal = new JPanel();
        panelPrincipal.setLayout(new BorderLayout());
        panelPrincipal.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // Mostrar la imagen en tamaño grande
        ImageIcon icono = new ImageIcon(rutaImagen);
        Image imagenRedimensionada = icono.getImage().getScaledInstance(300, 300, Image.SCALE_SMOOTH);
        JLabel labelImagen = new JLabel(new ImageIcon(imagenRedimensionada));
        labelImagen.setHorizontalAlignment(SwingConstants.CENTER);
        panelPrincipal.add(labelImagen, BorderLayout.NORTH);

        // Panel para los detalles del producto
        JPanel panelDetalles = new JPanel();
        panelDetalles.setLayout(new BoxLayout(panelDetalles, BoxLayout.Y_AXIS));

        // Nombre del producto
        JLabel labelNombre = new JLabel(nombre);
        labelNombre.setFont(new Font("Arial", Font.BOLD, 18));
        labelNombre.setAlignmentX(Component.CENTER_ALIGNMENT);
        panelDetalles.add(labelNombre);

        // Espacio entre el nombre y el precio
        panelDetalles.add(Box.createRigidArea(new Dimension(0, 10)));

        // Precio del producto
        JLabel labelPrecio = new JLabel("Precio: $" + precio);
        labelPrecio.setFont(new Font("Arial", Font.BOLD, 16));
        labelPrecio.setAlignmentX(Component.CENTER_ALIGNMENT);
        panelDetalles.add(labelPrecio);

        // Espacio entre el precio y el botón
        panelDetalles.add(Box.createRigidArea(new Dimension(0, 20)));

        // Botón de Añadir al carrito
        JButton btnProducto = new JButton("Añadir al carrito");
        btnProducto.setAlignmentX(Component.CENTER_ALIGNMENT);
        btnProducto.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Mostrar mensaje de agregar al carrito
                int respuesta = JOptionPane.showConfirmDialog(null,
                        "¿Deseas añadir " + nombre + " al carrito?",
                        "Añadir al carrito",
                        JOptionPane.YES_NO_OPTION);

                if (respuesta == JOptionPane.YES_OPTION) {
                    JOptionPane.showMessageDialog(null, nombre + " añadido al carrito.");
                    idEnCarrito[idEnCarritoGeneral.length] = 1;
                    new VentanaSupermarket(TipoUsuario.USUARIO).setVisible(true);// Agregar producto al carrito
                    dispose();
                } else {
                    JOptionPane.showMessageDialog(null, "No se añadió el producto al carrito.");
                }
            }
        });
        panelDetalles.add(btnProducto);

        // Botón de Cerrar
        JButton btnCerrar = new JButton("Cerrar");
        btnCerrar.setAlignmentX(Component.CENTER_ALIGNMENT);
        btnCerrar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
            	new VentanaSupermarket(TipoUsuario.USUARIO).setVisible(true);;
                dispose(); // Cerrar la ventana actual
                
            }
        });
        panelDetalles.add(Box.createRigidArea(new Dimension(0, 10)));
        panelDetalles.add(btnCerrar);

        // Añadir el panel de detalles al panel principal
        panelPrincipal.add(panelDetalles, BorderLayout.CENTER);

        // Añadir el panel principal a la ventana
        add(panelPrincipal);

        setVisible(true); // Hacer visible la ventana
    }

    public int[] getIdEnCarrito() {
        return idEnCarrito;
    }
}
