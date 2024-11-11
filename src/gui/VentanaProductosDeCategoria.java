package gui;

import javax.swing.*;

import domain.Categoria;
import domain.Producto;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;
import java.util.List;
import java.util.ArrayList;

public class VentanaProductosDeCategoria extends JFrame {
    private String categoriaSeleccionada;
    private double totalCarrito = 0.0;
    private JLabel totalLabel;
    private List<Producto> carrito = new ArrayList<>();
    private List<Producto> productosFiltrados = new ArrayList<>();
    private VentanaCategorias ventanaCategorias;
    private VentanaCarrito ventanaCarrito;
    
    public VentanaProductosDeCategoria(String categoria, VentanaCategorias ventanaCategorias, double totalCarrito) {
        this.categoriaSeleccionada = categoria;
        this.ventanaCategorias = ventanaCategorias;
        this.totalCarrito = totalCarrito;
        
        setTitle("Productos en categoría: " + categoria);
        setSize(800, 600);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        
        totalLabel = new JLabel("Total: €" + String.format("%.2f", totalCarrito));
        totalLabel.setFont(new Font("Arial", Font.BOLD, 16));
        totalLabel.setForeground(Color.BLACK);
        totalLabel.setHorizontalAlignment(SwingConstants.CENTER);

        JPanel panelSuperior = new JPanel();
        panelSuperior.setLayout(new BoxLayout(panelSuperior, BoxLayout.Y_AXIS));

        JButton btnRegresar = new JButton("Regresar a Categorías");
        btnRegresar.addActionListener(e -> regresarAVentanaCategorias());
        panelSuperior.add(btnRegresar);
        
        JPanel panelBusqueda = new JPanel();
        panelBusqueda.setLayout(new FlowLayout(FlowLayout.CENTER, 10, 0));
        JLabel labelBusqueda = new JLabel("Buscar Producto:");
        panelBusqueda.add(labelBusqueda);
        
        JTextField campoBusqueda = new JTextField(20);
        panelBusqueda.add(campoBusqueda);
        
        JButton btnBuscar = new JButton("Buscar");
        btnBuscar.addActionListener(e -> buscarProducto(campoBusqueda.getText()));
        panelBusqueda.add(btnBuscar);
        
        panelSuperior.add(panelBusqueda);  
        panelSuperior.add(totalLabel);
        
        add(panelSuperior, BorderLayout.NORTH);
        
        List<Producto> productos = obtenerProductosDeCategoria(categoria); 
        productosFiltrados.addAll(productos);
        
        JPanel panelProductos = new JPanel();
        panelProductos.setLayout(new GridLayout(0, 3, 10, 10));

        for (Producto p : productos) {
            JPanel productoPanel = crearPanelProducto(p);
            panelProductos.add(productoPanel);
        }
        
        JScrollPane scrollPane = new JScrollPane(panelProductos);
        scrollPane.addMouseWheelListener(new MouseWheelListener() {
            @Override
            public void mouseWheelMoved(MouseWheelEvent e) {
                int rotation = e.getWheelRotation(); 
                if (rotation < 0) { 
                    scrollPane.getVerticalScrollBar().setValue(scrollPane.getVerticalScrollBar().getValue() - 30); 
                } else { 
                    scrollPane.getVerticalScrollBar().setValue(scrollPane.getVerticalScrollBar().getValue() + 30);
                }
            }
        });
        
        add(scrollPane, BorderLayout.CENTER);
        
        JButton btnCarrito = new JButton("Ver Carrito");
        btnCarrito.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                abrirCarrito();
            }
        });
        
        JPanel panelBotones = new JPanel();
        panelBotones.add(btnCarrito);
        add(panelBotones, BorderLayout.SOUTH);
    }    
    
    private JPanel crearPanelProducto(Producto producto) {

    	JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY, 1));
        panel.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel nombreLabel = new JLabel(producto.getNombre(), JLabel.CENTER);
        nombreLabel.setFont(new Font("Arial", Font.PLAIN, 14));
        nombreLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        panel.add(nombreLabel);

        ImageIcon imagen = cargarImagen(producto.getRutaImagen(), 80, 80);
        JLabel imagenLabel = new JLabel(imagen, JLabel.CENTER);
        imagenLabel.setAlignmentX(Component.CENTER_ALIGNMENT); 
        panel.add(imagenLabel);

        JLabel precioLabel = new JLabel("€" + producto.getPrecio(), JLabel.CENTER);
        precioLabel.setFont(new Font("Arial", Font.PLAIN, 12));
        precioLabel.setAlignmentX(Component.CENTER_ALIGNMENT); 
        panel.add(precioLabel);
        
        JPanel cantidadPanel = new JPanel();
        cantidadPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 5, 0));
        cantidadPanel.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        JLabel cantidadLabel = new JLabel("Cantidad:");
        cantidadLabel.setFont(new Font("Arial", Font.PLAIN, 12));
        cantidadPanel.add(cantidadLabel);
        
        JSpinner cantidadSpinner = new JSpinner(new SpinnerNumberModel(0, 0, 100, 1));  
        cantidadSpinner.setPreferredSize(new Dimension(50, 30));
        cantidadPanel.add(cantidadSpinner);
        
        panel.add(cantidadPanel);

        JButton btnAñadir = new JButton("Añadir producto");
        btnAñadir.setAlignmentX(Component.CENTER_ALIGNMENT);
        btnAñadir.addActionListener(e -> {
            int cantidad = (int) cantidadSpinner.getValue();
            if (cantidad > 0) {
            	if (ventanaCarrito == null) {
                    ventanaCarrito = new VentanaCarrito(ventanaCategorias);
                }
                ventanaCarrito.agregarProductoAlCarrito(producto, cantidad);
                for (int i = 0; i < cantidad; i++) {
                    carrito.add(producto);
                    totalCarrito += producto.getPrecio();
                }
                actualizarTotal();
                cantidadSpinner.setValue(0);
                JOptionPane.showMessageDialog(this, "Añadido " + cantidad + " " + producto.getNombre() + "(s) al carrito.");
            } else {
                JOptionPane.showMessageDialog(this, "Por favor, seleccione una cantidad mayor que 0.", "Cantidad inválida", JOptionPane.ERROR_MESSAGE);
            }
        });
        
        panel.add(btnAñadir);

        return panel;
    }
    
    private void actualizarTotal() {
        totalLabel.setText(String.format("Total: €%.2f", totalCarrito));
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
    
    private void buscarProducto(String nombreBusqueda) {
        productosFiltrados.clear();
        List<Producto> productos = obtenerProductosDeCategoria(categoriaSeleccionada);
        for (Producto p : productos) {
            if (p.getNombre().toLowerCase().contains(nombreBusqueda.toLowerCase())) {
                productosFiltrados.add(p);
            }
        }        
        actualizarVistaProductos();
    }
    
    private void actualizarVistaProductos() {
        JPanel panelProductos = new JPanel();
        panelProductos.setLayout(new GridLayout(0, 3, 10, 10));

        for (Producto p : productosFiltrados) {
            JPanel productoPanel = crearPanelProducto(p);
            panelProductos.add(productoPanel);
        }

        JScrollPane scrollPane = new JScrollPane(panelProductos);
        getContentPane().removeAll();  
        add(scrollPane, BorderLayout.CENTER);  
        add(totalLabel, BorderLayout.NORTH);  
        revalidate(); 
        repaint();
    }
    
    private List<Producto> obtenerProductosDeCategoria(String categoria) {
        List<Producto> productosFiltrados = new ArrayList<>();
        
        Categoria frutas = new Categoria("Frutas", null);
        Categoria verduras = new Categoria("Verduras", null);
        Categoria lacteos = new Categoria("Lácteos", null);
        Categoria carnes = new Categoria("Carnes", null);
        Categoria bebidas = new Categoria("Bebidas", null);
        Categoria panaderia = new Categoria("Panadería", null);
        
        List<Producto> todosLosProductos = new ArrayList<>();
        todosLosProductos.add(new Producto(1, "Manzana", 1.5, "manzana.png", frutas));
        todosLosProductos.add(new Producto(2, "Banana", 1.2, "banana.png", frutas));
        todosLosProductos.add(new Producto(3, "Naranja", 1.0, "naranja.png", frutas));
        todosLosProductos.add(new Producto(4, "Fresa", 2.5, "fresa.png", frutas));
        todosLosProductos.add(new Producto(5, "Kiwi", 1.8, "kiwi.png", frutas));
        todosLosProductos.add(new Producto(6, "Mango", 3.0, "mango.png", frutas));
        todosLosProductos.add(new Producto(7, "Pera", 1.4, "pera.png", frutas));
        todosLosProductos.add(new Producto(8, "Zanahoria", 0.8, "zanahoria.png", verduras));
        todosLosProductos.add(new Producto(9, "Lechuga", 1.0, "lechuga.png", verduras));
        todosLosProductos.add(new Producto(10, "Tomate", 1.2, "tomate.png", verduras));
        todosLosProductos.add(new Producto(43, "Mandarina", 1.4, "mandarina.png", frutas));
        todosLosProductos.add(new Producto(44, "Aguacate", 2.2, "aguacate.png", frutas));
        todosLosProductos.add(new Producto(45, "Papaya", 3.0, "papaya.png", frutas));
        todosLosProductos.add(new Producto(46, "Melón", 2.5, "melon.png", frutas));
        todosLosProductos.add(new Producto(47, "Cereza", 4.0, "cereza.png", frutas));
        todosLosProductos.add(new Producto(48, "Granada", 2.8, "granada.png", frutas));
        todosLosProductos.add(new Producto(11, "Pepino", 0.9, "pepino.png", verduras));
        todosLosProductos.add(new Producto(12, "Espinaca", 1.5, "espinaca.png", verduras));
        todosLosProductos.add(new Producto(13, "Pimiento", 2.0, "pimiento.png", verduras));
        todosLosProductos.add(new Producto(14, "Cebolla", 1.3, "cebolla.png", verduras));
        todosLosProductos.add(new Producto(49, "Berenjena", 1.4, "berenjena.png", verduras));
        todosLosProductos.add(new Producto(50, "Apio", 1.0, "apio.png", verduras));
        todosLosProductos.add(new Producto(51, "Col rizada", 2.2, "col_rizada.png", verduras));
        todosLosProductos.add(new Producto(52, "Brócoli", 1.6, "brocoli.png", verduras));
        todosLosProductos.add(new Producto(53, "Calabacín", 1.3, "calabacin.png", verduras));
        todosLosProductos.add(new Producto(54, "Puerro", 1.7, "puerro.png", verduras));
        todosLosProductos.add(new Producto(15, "Leche", 1.2, "leche.png", lacteos));
        todosLosProductos.add(new Producto(16, "Queso", 3.5, "queso.png", lacteos));
        todosLosProductos.add(new Producto(17, "Yogur", 1.0, "yogur.png", lacteos));
        todosLosProductos.add(new Producto(18, "Mantequilla", 2.8, "mantequilla.png", lacteos));
        todosLosProductos.add(new Producto(19, "Helado", 3.0, "helado.png", lacteos));
        todosLosProductos.add(new Producto(20, "Crema", 2.0, "crema.png", lacteos));
        todosLosProductos.add(new Producto(21, "Kéfir", 1.8, "kefir.png", lacteos));
        todosLosProductos.add(new Producto(55, "Cottage", 3.0, "cottage.png", lacteos));
        todosLosProductos.add(new Producto(56, "Ricotta", 3.2, "ricotta.png", lacteos));
        todosLosProductos.add(new Producto(57, "Crema agria", 2.0, "crema_agria.png", lacteos));
        todosLosProductos.add(new Producto(58, "Queso fresco", 2.8, "queso_fresco.png", lacteos));
        todosLosProductos.add(new Producto(59, "Yogur griego", 1.5, "yogur_griego.png", lacteos));
        todosLosProductos.add(new Producto(60, "Manteca", 3.5, "manteca.png", lacteos));
        todosLosProductos.add(new Producto(22, "Pollo", 5.0, "pollo.png", carnes));
        todosLosProductos.add(new Producto(23, "Estofado de ternera", 8.0, "ternera.png", carnes));
        todosLosProductos.add(new Producto(24, "lomo", 6.5, "lomo.png", carnes));
        todosLosProductos.add(new Producto(25, "Chuletillas de cordero", 10.0, "cordero.png", carnes));
        todosLosProductos.add(new Producto(26, "Pavo", 7.0, "pavo.png", carnes));
        todosLosProductos.add(new Producto(27, "Salchicha", 4.0, "salchicha.png", carnes));
        todosLosProductos.add(new Producto(28, "Bacon", 3.5, "bacon.png", carnes));
        todosLosProductos.add(new Producto(61, "Pechuga de pollo", 6.0, "pechuga_pollo.png", carnes));
        todosLosProductos.add(new Producto(62, "Costillas de cerdo", 7.0, "costillas.png", carnes));
        todosLosProductos.add(new Producto(63, "Cordero", 9.5, "cordero.png", carnes));
        todosLosProductos.add(new Producto(64, "Ternera", 8.5, "ternera.png", carnes));
        todosLosProductos.add(new Producto(65, "Hamburguesa de ternera", 4.5, "hamburguesa_ternera.png", carnes));
        todosLosProductos.add(new Producto(66, "Tocino", 4.2, "tocino.png", carnes));
        todosLosProductos.add(new Producto(29, "Agua", 0.5, "agua.png", bebidas));
        todosLosProductos.add(new Producto(30, "Refresco", 1.5, "refresco.png", bebidas));
        todosLosProductos.add(new Producto(31, "Zumo de naranja", 2.0, "zumo_naranja.png", bebidas));
        todosLosProductos.add(new Producto(32, "Cerveza", 1.8, "cerveza.png", bebidas));
        todosLosProductos.add(new Producto(33, "Vino", 10.0, "vino.png", bebidas));
        todosLosProductos.add(new Producto(34, "Café", 3.0, "cafe.png", bebidas));
        todosLosProductos.add(new Producto(35, "Té", 2.5, "te.png", bebidas));
        todosLosProductos.add(new Producto(67, "Tetra Pak de leche", 1.0, "tetra_leche.png", bebidas));
        todosLosProductos.add(new Producto(68, "Té helado", 2.0, "te_helado.png", bebidas));
        todosLosProductos.add(new Producto(69, "Agua mineral", 0.8, "agua_mineral.png", bebidas));
        todosLosProductos.add(new Producto(70, "Cerveza artesanal", 2.5, "cerveza_artesanal.png", bebidas));
        todosLosProductos.add(new Producto(71, "Zumo de manzana", 1.8, "zumo_manzana.png", bebidas));
        todosLosProductos.add(new Producto(72, "Agua tónica", 1.2, "agua_tonica.png", bebidas));
        todosLosProductos.add(new Producto(36, "Pan", 1.0, "pan.png", panaderia));
        todosLosProductos.add(new Producto(37, "Croissant", 2.0, "croissant.png", panaderia));
        todosLosProductos.add(new Producto(38, "Donut", 1.5, "donut.png", panaderia));
        todosLosProductos.add(new Producto(39, "Bagel", 2.2, "bagel.png", panaderia));
        todosLosProductos.add(new Producto(40, "Magdalenas", 2.5, "magdalenas.png", panaderia));
        todosLosProductos.add(new Producto(41, "Baguette", 1.8, "baguette.png", panaderia));
        todosLosProductos.add(new Producto(42, "Empanada", 3.0, "empanada.png", panaderia));
        todosLosProductos.add(new Producto(73, "Pan de centeno", 1.5, "pan_centeno.png", panaderia));
        todosLosProductos.add(new Producto(74, "Pan integral", 1.3, "pan_integral.png", panaderia));
        todosLosProductos.add(new Producto(75, "Brioche", 2.8, "brioche.png", panaderia));
        todosLosProductos.add(new Producto(76, "Pan de chocolate", 2.2, "pan_chocolate.png", panaderia));
        todosLosProductos.add(new Producto(77, "Bollo de leche", 1.6, "bollo_leche.png", panaderia));
        todosLosProductos.add(new Producto(78, "Panecillos", 1.0, "panecillos.png", panaderia));
        
        for (Producto producto : todosLosProductos) {
            if (producto.getCategoria().getNombre().equalsIgnoreCase(categoria)) {
                productosFiltrados.add(producto);
            }
        }

        return productosFiltrados;
    }
    private void regresarAVentanaCategorias() {
        ventanaCategorias.actualizarTotalCarrito(totalCarrito);
        this.dispose();
    }
    
    private void abrirCarrito() {
        if (ventanaCarrito == null) {
            ventanaCarrito = new VentanaCarrito(ventanaCategorias);
        }
        ventanaCarrito.setVisible(true);
    }
}