package gui;

import javax.swing.*;
import db.ServicioPersistenciaBD;
import domain.Usuario;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
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
        setSize(1290, 800);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);

        totalCarrito = 0.0;
        totalCarritoLabel = new JLabel("Total: €0.00");
        totalCarritoLabel.setFont(new Font("Arial", Font.BOLD, 16));
        totalCarritoLabel.setForeground(Color.DARK_GRAY);
        totalCarritoLabel.setHorizontalAlignment(SwingConstants.RIGHT);

        JPanel panelSuperior = new JPanel(new BorderLayout());
        panelSuperior.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        panelSuperior.add(totalCarritoLabel, BorderLayout.EAST);

        JButton btnVerCarrito = new JButton("Ver Carrito");
        btnVerCarrito.setFont(new Font("Arial", Font.BOLD, 16));
        btnVerCarrito.setBackground(new Color(70, 130, 180));
        btnVerCarrito.setForeground(Color.BLACK);
        btnVerCarrito.setFocusPainted(false);
        btnVerCarrito.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                VentanaCarrito ventanaCarrito = VentanaCarrito.getInstance(VentanaCategorias.this);
                ventanaCarrito.setVisible(true);
                VentanaCategorias.this.setVisible(false);
            }
        });

        panelSuperior.add(btnVerCarrito, BorderLayout.WEST);
        add(panelSuperior, BorderLayout.NORTH);

        categoriaImagenMap = new HashMap<>();
        categoriaImagenMap.put("Frutas", "frutas.png");
        categoriaImagenMap.put("Verduras", "verduras.png");
        categoriaImagenMap.put("Lacteos", "lacteos.png");
        categoriaImagenMap.put("Carnes", "carnes.png");
        categoriaImagenMap.put("Bebidas", "bebidas.png");
        categoriaImagenMap.put("Panaderia", "panaderia.png");

        JPanel panelCategorias = new JPanel();
        panelCategorias.setLayout(new GridLayout(0, 3, 20, 20));
        panelCategorias.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

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

                g2d.setColor(new Color(240, 240, 240));
                g2d.fillRoundRect(0, 0, getWidth(), getHeight(), 20, 20);

                if (getModel().isRollover()) {
                    g2d.setColor(new Color(220, 220, 220));
                    g2d.fillRoundRect(0, 0, getWidth(), getHeight(), 20, 20);
                }

                g2d.dispose();
                super.paintComponent(g);
            }
        };

        btnCategoria.setLayout(new BorderLayout());
        btnCategoria.setOpaque(false);
        btnCategoria.setContentAreaFilled(false);
        btnCategoria.setBorder(BorderFactory.createEmptyBorder());

        ImageIcon iconoColor = cargarImagen(categoria + ".png", 300, 240);
        ImageIcon iconoGrayscale = convertirEscalaDeGrises(iconoColor);

        JLabel labelImagen = new JLabel(iconoGrayscale);
        labelImagen.setHorizontalAlignment(SwingConstants.CENTER);

        btnCategoria.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseEntered(java.awt.event.MouseEvent e) {
                labelImagen.setIcon(iconoColor);
            }

            @Override
            public void mouseExited(java.awt.event.MouseEvent e) {
                labelImagen.setIcon(iconoGrayscale);
            }
        });

        btnCategoria.add(labelImagen, BorderLayout.CENTER);

        JLabel labelTexto = new JLabel(categoria, SwingConstants.CENTER);
        labelTexto.setFont(new Font("Arial", Font.BOLD, 18));
        labelTexto.setForeground(new Color(80, 80, 80));
        btnCategoria.add(labelTexto, BorderLayout.SOUTH);

        btnCategoria.setPreferredSize(new Dimension(200, 150));
        btnCategoria.addActionListener(e -> {
            VentanaProductosDeCategoria ventanaProductos = new VentanaProductosDeCategoria(categoria);
            ventanaProductos.setVisible(true);
            dispose();
        });

        return btnCategoria;
    }

    private ImageIcon convertirEscalaDeGrises(ImageIcon iconoOriginal) {
        Image imagenOriginal = iconoOriginal.getImage();
        BufferedImage bufferedImagen = new BufferedImage(imagenOriginal.getWidth(null), imagenOriginal.getHeight(null), BufferedImage.TYPE_INT_ARGB);

        Graphics2D g2d = bufferedImagen.createGraphics();
        g2d.drawImage(imagenOriginal, 0, 0, null);
        g2d.dispose();

        for (int x = 0; x < bufferedImagen.getWidth(); x++) {
            for (int y = 0; y < bufferedImagen.getHeight(); y++) {
                int rgba = bufferedImagen.getRGB(x, y);
                Color col = new Color(rgba, true);
                int gris = (int) (col.getRed() * 0.3 + col.getGreen() * 0.59 + col.getBlue() * 0.11);
                Color nuevoColor = new Color(gris, gris, gris, col.getAlpha());
                bufferedImagen.setRGB(x, y, nuevoColor.getRGB());
            }
        }

        return new ImageIcon(bufferedImagen);
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
