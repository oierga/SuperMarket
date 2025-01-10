package gui;

import javax.swing.*;

import domain.Producto;
import domain.Usuario;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Arrays;

public class VentanaProducto extends JFrame {
    private String nombre;
    private double precio;
    private String rutaImagen;
    private int[] idEnCarrito;
    private Usuario usuario;
    
    public VentanaProducto(Producto producto, int[] idEnCarritoGeneral, Usuario usuario) {
    	this.usuario = usuario;
    	
        this.nombre = "Nombre prueba";
        this.precio = 789.00;
        this.rutaImagen = "ruta/a/la/imagen"; 
        this.idEnCarrito = Arrays.copyOf(idEnCarritoGeneral, idEnCarritoGeneral.length + 1);

        setTitle("Detalles del Producto");
        setSize(400, 600);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        JPanel panelPrincipal = new JPanel();
        panelPrincipal.setLayout(new BorderLayout());
        panelPrincipal.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        ImageIcon icono = new ImageIcon(rutaImagen);
        Image imagenRedimensionada = icono.getImage().getScaledInstance(300, 300, Image.SCALE_SMOOTH);
        JLabel labelImagen = new JLabel(new ImageIcon(imagenRedimensionada));
        labelImagen.setHorizontalAlignment(SwingConstants.CENTER);
        panelPrincipal.add(labelImagen, BorderLayout.NORTH);

        JPanel panelDetalles = new JPanel();
        panelDetalles.setLayout(new BoxLayout(panelDetalles, BoxLayout.Y_AXIS));

        JLabel labelNombre = new JLabel(nombre);
        labelNombre.setFont(new Font("Arial", Font.BOLD, 18));
        labelNombre.setAlignmentX(Component.CENTER_ALIGNMENT);
        panelDetalles.add(labelNombre);

        panelDetalles.add(Box.createRigidArea(new Dimension(0, 10)));

        JLabel labelPrecio = new JLabel("Precio: $" + precio);
        labelPrecio.setFont(new Font("Arial", Font.BOLD, 16));
        labelPrecio.setAlignmentX(Component.CENTER_ALIGNMENT);
        panelDetalles.add(labelPrecio);

        panelDetalles.add(Box.createRigidArea(new Dimension(0, 20)));

        JButton btnProducto = new JButton("Añadir al carrito");
        btnProducto.setAlignmentX(Component.CENTER_ALIGNMENT);
        btnProducto.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int respuesta = JOptionPane.showConfirmDialog(null,
                        "¿Deseas añadir " + nombre + " al carrito?",
                        "Añadir al carrito",
                        JOptionPane.YES_NO_OPTION);

                if (respuesta == JOptionPane.YES_OPTION) {
                    JOptionPane.showMessageDialog(null, nombre + " añadido al carrito.");
                    idEnCarrito[idEnCarritoGeneral.length] = 1;
                    new VentanaSupermarket().setVisible(true);
                    dispose();
                } else {
                    JOptionPane.showMessageDialog(null, "No se añadió el producto al carrito.");
                }
            }
        });
        panelDetalles.add(btnProducto);

     
        JButton btnCerrar = new JButton("Cerrar");
        btnCerrar.setAlignmentX(Component.CENTER_ALIGNMENT);
        btnCerrar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
            	new VentanaSupermarket().setVisible(true);;
                dispose(); 
            }
        });
        panelDetalles.add(Box.createRigidArea(new Dimension(0, 10)));
        panelDetalles.add(btnCerrar);

        panelPrincipal.add(panelDetalles, BorderLayout.CENTER);

        add(panelPrincipal);

        setVisible(true); 
    }

    public int[] getIdEnCarrito() {
        return idEnCarrito;
    }
}
