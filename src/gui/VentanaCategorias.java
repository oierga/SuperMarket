package gui;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.HashMap;
import java.util.Map;

public class VentanaCategorias extends JFrame {
	
    private Map<String, String> categoriaImagenMap;
    private JLabel totalCarritoLabel;
    private double totalCarrito;
    private VentanaCarrito ventanaCarrito;
	
    public VentanaCategorias() {
        setTitle("Categorías");
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
	    
        totalCarrito = 0.0;
        totalCarritoLabel = new JLabel("Total: €0.00");
        totalCarritoLabel.setFont(new Font("Arial", Font.BOLD, 16));
        totalCarritoLabel.setForeground(Color.BLACK);
        totalCarritoLabel.setHorizontalAlignment(SwingConstants.RIGHT);
	
        JPanel panelSuperior = new JPanel(new BorderLayout());
        panelSuperior.add(totalCarritoLabel, BorderLayout.EAST);
        
        JButton btnVerCarrito = new JButton("Ver Carrito");
        btnVerCarrito.setFont(new Font("Arial", Font.BOLD, 16));
        btnVerCarrito.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Mostrar la ventana del carrito
                if (ventanaCarrito == null) {
                    ventanaCarrito = new VentanaCarrito(VentanaCategorias.this);
                }
                ventanaCarrito.setVisible(true);
            }
        });
        
        panelSuperior.add(btnVerCarrito, BorderLayout.WEST);
        add(panelSuperior, BorderLayout.NORTH);
	    
        categoriaImagenMap = new HashMap<>();
        categoriaImagenMap.put("Frutas", "frutas.png");
        categoriaImagenMap.put("Verduras", "verduras.png");
        categoriaImagenMap.put("Lácteos", "lacteos.png");
        categoriaImagenMap.put("Carnes", "carnes.png");
        categoriaImagenMap.put("Bebidas", "bebidas.png");
        categoriaImagenMap.put("Panadería", "panaderia.png");

        JPanel panelCategorias = new JPanel();
        panelCategorias.setLayout(new GridLayout(0, 3)); 
        String[] categorias = {"Frutas", "Verduras", "Lácteos", "Carnes", "Bebidas", "Panadería"};

        for (String categoria : categorias) {
        	JButton btnCategoria = crearBotonCategoria(categoria);
            panelCategorias.add(btnCategoria);
        }
        
        JScrollPane scrollPane = new JScrollPane(panelCategorias);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        add(scrollPane, BorderLayout.CENTER);
    }
    
    private JButton crearBotonCategoria(String categoria) {
        JButton btnCategoria = new JButton();
        btnCategoria.setLayout(new BorderLayout());            
            String nombreImagen = categoriaImagenMap.get(categoria);
            if (nombreImagen != null) {
                ImageIcon icono = cargarImagen(nombreImagen, 250, 200);
                if (icono != null) {
                    btnCategoria.setIcon(icono);
                    btnCategoria.setHorizontalTextPosition(JButton.CENTER);
                    btnCategoria.setVerticalTextPosition(JButton.CENTER);
                }
            }

            JLabel labelTexto = new JLabel(categoria, SwingConstants.CENTER);
            labelTexto.setForeground(Color.WHITE);
            labelTexto.setFont(new Font("Arial", Font.BOLD, 16));
            btnCategoria.add(labelTexto, BorderLayout.SOUTH);
            
            btnCategoria.setPreferredSize(new Dimension(250, 200));
            btnCategoria.setHorizontalAlignment(SwingConstants.CENTER);
            btnCategoria.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    // Aquí tenemos que cargar los productos de esa categoría
                    VentanaProductosDeCategoria ventanaProductos = new VentanaProductosDeCategoria(categoria, VentanaCategorias.this, totalCarrito);
                    ventanaProductos.setVisible(true);
                }
            });

            return btnCategoria; 
        }
   public void actualizarTotalCarrito(double nuevoTotal) {
        totalCarrito = nuevoTotal;
        totalCarritoLabel.setText(String.format("Total: €%.2f", totalCarrito));
    }
    private ImageIcon cargarImagen(String nombreImagen, int ancho, int alto) {
        java.net.URL imageUrl = getClass().getResource("/images/" + nombreImagen);        
        if (imageUrl != null) {
            ImageIcon iconoOriginal = new ImageIcon(imageUrl);
            Image imagenEscalada = iconoOriginal.getImage().getScaledInstance(ancho, alto, Image.SCALE_SMOOTH);
            return new ImageIcon(imagenEscalada);
        } else {
            System.err.println("No se pudo encontrar la imagen: " + nombreImagen);
            return null;
        }
    }
}