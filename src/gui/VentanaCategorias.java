package gui;

import javax.swing.*;

import db.ServicioPersistenciaBD;
import domain.Usuario;

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
    private Usuario usuario;
	
    public VentanaCategorias() {
    	this.usuario = ServicioPersistenciaBD.getInstance().getUsuario();
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
            	
            	//obtener la instacia unica de carrito
            	VentanaCarrito ventanaCarrito = VentanaCarrito.getInstance(VentanaCategorias.this);
            	
            	ventanaCarrito.setVisible(true);
            	
            	//y pa ocultar la ventana actual
            	VentanaCategorias.this.setVisible(false);
            }
        });
        
        panelSuperior.add(btnVerCarrito, BorderLayout.WEST);
        add(panelSuperior, BorderLayout.NORTH);
	    
        categoriaImagenMap = new HashMap<>();
        categoriaImagenMap.put("Frutas", "frutas.png");
        categoriaImagenMap.put("Verduras", "verduras.png");
        categoriaImagenMap.put("lacteos", "lacteos.png");
        categoriaImagenMap.put("Carnes", "carnes.png");
        categoriaImagenMap.put("Bebidas", "bebidas.png");
        categoriaImagenMap.put("Panaderia", "panaderia.png");

        JPanel panelCategorias = new JPanel();
        panelCategorias.setLayout(new GridLayout(0, 3)); 
        String[] categorias = {"Frutas", "Verduras", "Lacteos", "Carnes", "Bebidas", "Panaderia"};

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
        JButton btnCategoria = new JButton() {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2d = (Graphics2D) g.create();
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

                // Dibujar fondo con bordes redondeados
                g2d.setColor(new Color(240, 240, 240)); // Fondo base
                g2d.fillRoundRect(0, 0, getWidth(), getHeight(), 20, 20);

                if (getModel().isRollover()) {
                    // Efecto hover: brillo sutil
                    g2d.setColor(new Color(255, 255, 255, 50));
                    g2d.fillRoundRect(0, 0, getWidth(), getHeight(), 20, 20);
                }

                g2d.dispose();
                super.paintComponent(g); // Dibujar contenido del botón
            }
        };

        btnCategoria.setLayout(new BorderLayout());
        btnCategoria.setOpaque(false);
        btnCategoria.setContentAreaFilled(false);
        btnCategoria.setBorder(BorderFactory.createEmptyBorder());

        // Crear un JLabel para la imagen con borde
        ImageIcon icono = cargarImagen(categoria + ".png", 200, 150);
        if (icono != null) {
            JLabel labelImagen = new JLabel(icono) {
                @Override
                protected void paintComponent(Graphics g) {
                    super.paintComponent(g);

                    // Dibujar borde alrededor de la imagen
                    Graphics2D g2d = (Graphics2D) g.create();
                    g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                    g2d.setColor(new Color(220, 220, 220)); // Color verde
                    g2d.setStroke(new BasicStroke(4)); // Grosor del borde
                    g2d.drawRoundRect(0, 0, getWidth() - 1, getHeight() - 1, 15, 15);
                    g2d.dispose();
                }
            };
            labelImagen.setHorizontalAlignment(SwingConstants.CENTER);
            btnCategoria.add(labelImagen, BorderLayout.CENTER);
        }

        // Añadir texto debajo de la imagen
        JLabel labelTexto = new JLabel(categoria, SwingConstants.CENTER);
        labelTexto.setForeground(new Color(100, 100, 100)); // Color verde para el texto
        labelTexto.setFont(new Font("Arial", Font.BOLD, 18));
        btnCategoria.add(labelTexto, BorderLayout.SOUTH);

        // Ajustar tamaño del botón
        btnCategoria.setPreferredSize(new Dimension(250, 200));
        btnCategoria.setHorizontalAlignment(SwingConstants.CENTER);

        // Acción al hacer clic
        btnCategoria.addActionListener(e -> {
            VentanaProductosDeCategoria ventanaProductos = new VentanaProductosDeCategoria( categoria);
            ventanaProductos.setVisible(true);
        });

        return btnCategoria;
    }

   public void actualizarTotalCarrito(double nuevoTotal) {
        totalCarrito = nuevoTotal;
        totalCarritoLabel.setText(String.format("Total: €%.2f", totalCarrito));
    }
    ImageIcon cargarImagen(String nombreImagen, int ancho, int alto) {
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
