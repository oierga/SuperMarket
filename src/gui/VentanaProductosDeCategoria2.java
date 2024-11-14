package gui;

import javax.swing.*;
import javax.swing.border.CompoundBorder;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;

import domain.Categoria;
import domain.Producto;
import domain.Usuario;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;
import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.ArrayList;
import java.util.HashMap;

public class VentanaProductosDeCategoria2 extends JFrame {
    private String categoriaSeleccionada;
    private double totalCarrito = 0.0;
    private JLabel totalLabel;
    private List<Producto> carrito = new ArrayList<>();
    private List<Producto> productosFiltrados = new ArrayList<>();
    private HashMap<String,Producto> todosLosProductos;

    private VentanaCategorias ventanaCategorias;
    private VentanaCarrito ventanaCarrito;
    private Usuario usuario;
    
    public VentanaProductosDeCategoria2(String categoria, VentanaCategorias ventanaCategorias, double totalCarrito) {
        this.categoriaSeleccionada = categoria;
        this.ventanaCategorias = ventanaCategorias;
        this.totalCarrito = totalCarrito;
        todosLosProductos = new HashMap<String,Producto>();
        cargarProductos();
        cargarProductos();
        setTitle("Productos en categoría: " + categoria);
        setSize(1000, 800);
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
        
        JPanel panelProductos = crearPanelProducto();
        panelProductos.setLayout(new GridLayout(10, 5, 10, 10));

        
        
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
        getContentPane().add(crearPanelContacto(),BorderLayout.SOUTH);
    }    
    private JPanel crearPanelContacto() {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBackground(Color.WHITE);
        panel.setBorder(new CompoundBorder(
            new LineBorder(Color.LIGHT_GRAY, 1),
            new EmptyBorder(20, 30, 20, 30)
        ));
        
        JLabel lblContacto = new JLabel("Contacto y Atención al Cliente");
        lblContacto.setFont(new Font("Segoe UI", Font.BOLD, 20));
        lblContacto.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        String[][] contactInfo = {
            {"📍", "Calle Mansa, 42-6, Bilbao, España"},
            {"📞", "+34 666 111 222"},
            {"⏰", "Lunes a Viernes, 9:00 - 21:00"},
            {"✉️", "soporte@supermercado.com"}
        };
        
        JPanel infoPanel = new JPanel(new GridLayout(1, 1, 0, 6));
        infoPanel.setBackground(new Color(76, 175, 80));
        
        for (String[] info : contactInfo) {
            JPanel linePanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
            linePanel.setBackground(Color.WHITE);
            
            JLabel lblIcono = new JLabel(info[0]);
            lblIcono.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 16));
            
            JLabel lblTexto = new JLabel(info[1]);
            lblTexto.setFont(new Font("Segoe UI", Font.PLAIN, 14));
            
            linePanel.add(lblIcono);
            linePanel.add(Box.createRigidArea(new Dimension(10, 0)));
            linePanel.add(lblTexto);
            linePanel.setBackground(new Color(76, 175, 80));
            infoPanel.add(linePanel);
        }
        
        panel.add(lblContacto);
        panel.add(Box.createRigidArea(new Dimension(0, 15)));
        panel.add(infoPanel);
        panel.setBackground(new Color(76, 175, 80));
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
            JPanel productoPanel = crearPanelProducto();
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
        HashMap<String,Producto> todosLosProductos = new HashMap<>();
        //todosLosProductos = new ArrayList<>();
       

        
        String[] imagenes = { "agua.png", "bacon.png", "bagel.png", "baguette.png", "banana.png", "bebidas.png", "cafe.png", 
                "carnes.png", "cebolla.png", "cerveza.png", "cordero.png", "crema.png", "croissant.png", 
                "deustowallpaper.jpg", "donut.png", "empanada.png", "espinaca.png", "fresa.png", "frutas.png", 
                "helado.png", "kefir.png", "kiwi.png", "lacteos.png", "leche.png", "lechuga.png", "lomo.png", 
                "magdalenas.png", "mango.png", "mantequilla.png", "manzana.png", "naranja.png", "pan.png", 
                "panaderia.png", "pavo.png", "pepino.png", "pera.png", "pimiento.png", "pollo.png", "queso.png", 
                "refresco.png", "salchicha.png", "te.png", "ternera.png", "tomate.png", "verduras.png", "vino.png", 
                "yogur.png", "zanahoria.png", "zumo_naranja.png" };
        
        for (String imagen: imagenes) {
            productosFiltrados.add(todosLosProductos.get(imagen.substring(0,imagen.length()-4)));
            	
                
            }
        

        return productosFiltrados;
    }
    private void regresarAVentanaCategorias() {
        ventanaCategorias.actualizarTotalCarrito(totalCarrito);
        this.dispose();
    }
    
    private void abrirCarrito() {
        if (ventanaCarrito == null) {
            ventanaCarrito = new VentanaCarrito(ventanaCategorias,usuario);
        }
        ventanaCarrito.setVisible(true);
    }
    private JPanel crearPanelProducto() {
        JPanel productPanel = new JPanel();
        String price = "1.50 €";
        String productName= "Nombre Prueba";
        productPanel.setLayout(new BoxLayout(productPanel, BoxLayout.Y_AXIS));
        productPanel.setBackground(Color.WHITE);
        productPanel.setBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY));
        List<Producto> productos = obtenerProductosDeCategoria("Frutas");
        // Tamaño deseado para las imágenes
        int imageWidth = 100;
        int imageHeight = 100;

        // Imagen del producto
        JLabel productImage = new JLabel();
     // Obtiene la ruta del directorio raíz del proyecto
        String rutaBase = System.getProperty("user.dir");
        String[] imagenes = { "agua.png", "bacon.png", "bagel.png", "baguette.png", "banana.png", "bebidas.png", "cafe.png", 
                "carnes.png", "cebolla.png", "cerveza.png", "cordero.png", "crema.png", "croissant.png", 
                "deustowallpaper.jpg", "donut.png", "empanada.png", "espinaca.png", "fresa.png", "frutas.png", 
                "helado.png", "kefir.png", "kiwi.png", "lacteos.png", "leche.png", "lechuga.png", "lomo.png", 
                "magdalenas.png", "mango.png", "mantequilla.png", "manzana.png", "naranja.png", "pan.png", 
                "panaderia.png", "pavo.png", "pepino.png", "pera.png", "pimiento.png", "pollo.png", "queso.png", 
                "refresco.png", "salchicha.png", "te.png", "ternera.png", "tomate.png", "verduras.png", "vino.png", 
                "yogur.png", "zanahoria.png", "zumo_naranja.png" };

        for (String imagen : imagenes) {
            try {
                ImageIcon originalIcon = cargarImagen(imagen,100,100);
                Image scaledImage = originalIcon.getImage().getScaledInstance(imageWidth, imageHeight, Image.SCALE_SMOOTH);
                productImage.setIcon(new ImageIcon(scaledImage));
            } catch (Exception e) {
                // Manejo de errores si la imagen no se puede cargar
                System.out.println("Error al cargar la imagen: " + imagen);
                productImage.setText("Imagen no disponible");
            }
            productImage.setAlignmentX(Component.CENTER_ALIGNMENT);
            cargarProductos();
            Producto producto = todosLosProductos.get(imagen.substring(0,imagen.length()-4));
            

             // Nombre del producto
            
            JLabel nameLabel = new JLabel("Prueba");
            nameLabel.setFont(new Font("Arial", Font.BOLD, 14));
            nameLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

            

             // Precio
             JLabel priceLabel = new JLabel("1.50€");
             priceLabel.setFont(new Font("Arial", Font.BOLD, 16));
             priceLabel.setForeground(new Color(0, 102, 204)); // Color similar al de Carrefour
             priceLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

             String[] unidades = {"1", "2", "3", "4", "5", "6", "7", "8", "9", "10"};
             JComboBox<String> comboUnidades = new JComboBox<>(unidades);
             comboUnidades.setSelectedIndex(0); // Seleccionar "1" como valor inicial
             
             // Agregar un ActionListener para manejar el cambio de selección
             
             
             
             // Botón de "Añadir"
             JButton addButton = new JButton("Añadir al carrito");
             addButton.setBackground(new Color(0, 102, 204));
             addButton.setForeground(Color.BLACK);
             addButton.setAlignmentX(Component.CENTER_ALIGNMENT);
             addButton.addActionListener(new ActionListener() {

     			@Override
     			public void actionPerformed(ActionEvent e) {
     				// TODO Auto-generated method stub
     				int opcion = JOptionPane.showConfirmDialog(null, "¿Desea añadir este producto a la cesta?");
     				if (opcion==0) {
     					String seleccion = (String) comboUnidades.getSelectedItem();
     			        int  unidadesSeleccionadas = Integer.parseInt(seleccion); 
     			        
     			        carrito.add(producto);
     					
     					
     					
     				}
     				
     			}
             	
             });
             // Añadir componentes al panel del producto
             productPanel.add(productImage);
             productPanel.add(Box.createVerticalStrut(5));
             productPanel.add(nameLabel);
             
             productPanel.add(priceLabel);
             productPanel.add(Box.createVerticalStrut(5));
             productPanel.add(addButton);
             productPanel.add(comboUnidades);
        }
        
       
        return productPanel;
    }
    private void cargarProductos() {
    	 
        Categoria frutas = new Categoria("Frutas", null);
        Categoria verduras = new Categoria("Verduras", null);
        Categoria lacteos = new Categoria("Lácteos", null);
        Categoria carnes = new Categoria("Carnes", null);
        Categoria bebidas = new Categoria("Bebidas", null);
        Categoria panaderia = new Categoria("Panadería", null);
        HashMap<String,Producto> todosLosProductos = new HashMap<>();
    	 todosLosProductos.put("Manzana", new Producto(1, "Manzana", 1.5, "manzana.png", frutas));
         todosLosProductos.put("Banana", new Producto(2, "Banana", 1.2, "banana.png", frutas));
         todosLosProductos.put("Naranja", new Producto(3, "Naranja", 1.0, "naranja.png", frutas));
         todosLosProductos.put("Fresa", new Producto(4, "Fresa", 2.5, "fresa.png", frutas));
         todosLosProductos.put("Kiwi", new Producto(5, "Kiwi", 1.8, "kiwi.png", frutas));
         todosLosProductos.put("Mango", new Producto(6, "Mango", 3.0, "mango.png", frutas));
         todosLosProductos.put("Pera", new Producto(7, "Pera", 1.4, "pera.png", frutas));
         todosLosProductos.put("Zanahoria", new Producto(8, "Zanahoria", 0.8, "zanahoria.png", verduras));
         todosLosProductos.put("Lechuga", new Producto(9, "Lechuga", 1.0, "lechuga.png", verduras));
         todosLosProductos.put("Tomate", new Producto(10, "Tomate", 1.2, "tomate.png", verduras));
         todosLosProductos.put("Mandarina", new Producto(43, "Mandarina", 1.4, "mandarina.png", frutas));
         todosLosProductos.put("Aguacate", new Producto(44, "Aguacate", 2.2, "aguacate.png", frutas));
         todosLosProductos.put("Papaya", new Producto(45, "Papaya", 3.0, "papaya.png", frutas));
         todosLosProductos.put("Melón", new Producto(46, "Melón", 2.5, "melon.png", frutas));
         todosLosProductos.put("Cereza", new Producto(47, "Cereza", 4.0, "cereza.png", frutas));
         todosLosProductos.put("Granada", new Producto(48, "Granada", 2.8, "granada.png", frutas));
         todosLosProductos.put("Pepino", new Producto(11, "Pepino", 0.9, "pepino.png", verduras));
         todosLosProductos.put("Espinaca", new Producto(12, "Espinaca", 1.5, "espinaca.png", verduras));
         todosLosProductos.put("Pimiento", new Producto(13, "Pimiento", 2.0, "pimiento.png", verduras));
         todosLosProductos.put("Cebolla", new Producto(14, "Cebolla", 1.3, "cebolla.png", verduras));
         todosLosProductos.put("Berenjena", new Producto(49, "Berenjena", 1.4, "berenjena.png", verduras));
         todosLosProductos.put("Apio", new Producto(50, "Apio", 1.0, "apio.png", verduras));
         todosLosProductos.put("Col rizada", new Producto(51, "Col rizada", 2.2, "col_rizada.png", verduras));
         todosLosProductos.put("Brócoli", new Producto(52, "Brócoli", 1.6, "brocoli.png", verduras));
         todosLosProductos.put("Calabacín", new Producto(53, "Calabacín", 1.3, "calabacin.png", verduras));
         todosLosProductos.put("Puerro", new Producto(54, "Puerro", 1.7, "puerro.png", verduras));
         todosLosProductos.put("Leche", new Producto(15, "Leche", 1.2, "leche.png", lacteos));
         todosLosProductos.put("Queso", new Producto(16, "Queso", 3.5, "queso.png", lacteos));
         todosLosProductos.put("Yogur", new Producto(17, "Yogur", 1.0, "yogur.png", lacteos));
         todosLosProductos.put("Mantequilla", new Producto(18, "Mantequilla", 2.8, "mantequilla.png", lacteos));
         todosLosProductos.put("Helado", new Producto(19, "Helado", 3.0, "helado.png", lacteos));
         todosLosProductos.put("Crema", new Producto(20, "Crema", 2.0, "crema.png", lacteos));
         todosLosProductos.put("Kéfir", new Producto(21, "Kéfir", 1.8, "kefir.png", lacteos));
         todosLosProductos.put("Cottage", new Producto(55, "Cottage", 3.0, "cottage.png", lacteos));
         todosLosProductos.put("Ricotta", new Producto(56, "Ricotta", 3.2, "ricotta.png", lacteos));
         todosLosProductos.put("Crema agria", new Producto(57, "Crema agria", 2.0, "crema_agria.png", lacteos));
         todosLosProductos.put("Queso fresco", new Producto(58, "Queso fresco", 2.8, "queso_fresco.png", lacteos));
         todosLosProductos.put("Yogur griego", new Producto(59, "Yogur griego", 1.5, "yogur_griego.png", lacteos));
         todosLosProductos.put("Manteca", new Producto(60, "Manteca", 3.5, "manteca.png", lacteos));
         todosLosProductos.put("Pollo", new Producto(22, "Pollo", 5.0, "pollo.png", carnes));
         todosLosProductos.put("Estofado de ternera", new Producto(23, "Estofado de ternera", 8.0, "ternera.png", carnes));
         todosLosProductos.put("Lomo", new Producto(24, "Lomo", 6.5, "lomo.png", carnes));
         todosLosProductos.put("Chuletillas de cordero", new Producto(25, "Chuletillas de cordero", 10.0, "cordero.png", carnes));
         todosLosProductos.put("Pavo", new Producto(26, "Pavo", 7.0, "pavo.png", carnes));
         todosLosProductos.put("Salchicha", new Producto(27, "Salchicha", 4.0, "salchicha.png", carnes));
         todosLosProductos.put("Bacon", new Producto(28, "Bacon", 3.5, "bacon.png", carnes));
         todosLosProductos.put("Pechuga de pollo", new Producto(61, "Pechuga de pollo", 6.0, "pechuga_pollo.png", carnes));
         todosLosProductos.put("Costillas de cerdo", new Producto(62, "Costillas de cerdo", 7.0, "costillas.png", carnes));
         todosLosProductos.put("Cordero", new Producto(63, "Cordero", 9.5, "cordero.png", carnes));
         todosLosProductos.put("Ternera", new Producto(64, "Ternera", 8.5, "ternera.png", carnes));
         todosLosProductos.put("Hamburguesa de ternera", new Producto(65, "Hamburguesa de ternera", 4.5, "hamburguesa_ternera.png", carnes));
         todosLosProductos.put("Tocino", new Producto(66, "Tocino", 4.2, "tocino.png", carnes));
         todosLosProductos.put("Agua", new Producto(29, "Agua", 0.5, "agua.png", bebidas));
         todosLosProductos.put("Refresco", new Producto(30, "Refresco", 1.5, "refresco.png", bebidas));
         todosLosProductos.put("Zumo de naranja", new Producto(31, "Zumo de naranja", 2.0, "zumo_naranja.png", bebidas));
         todosLosProductos.put("Cerveza", new Producto(32, "Cerveza", 1.8, "cerveza.png", bebidas));
         todosLosProductos.put("Vino", new Producto(33, "Vino", 10.0, "vino.png", bebidas));
         todosLosProductos.put("Café", new Producto(34, "Café", 3.0, "cafe.png", bebidas));
         todosLosProductos.put("Té", new Producto(35, "Té", 2.5, "te.png", bebidas));
         todosLosProductos.put("Tetra Pak de leche", new Producto(67, "Tetra Pak de leche", 1.0, "tetra_leche.png", bebidas));
         todosLosProductos.put("Té helado", new Producto(68, "Té helado", 2.0, "te_helado.png", bebidas));
         todosLosProductos.put("Agua mineral", new Producto(69, "Agua mineral", 0.8, "agua_mineral.png", bebidas));
         todosLosProductos.put("Cerveza artesanal", new Producto(70, "Cerveza artesanal", 2.5, "cerveza_artesanal.png", bebidas));
         todosLosProductos.put("Zumo de manzana", new Producto(71, "Zumo de manzana", 1.8, "zumo_manzana.png", bebidas));
         todosLosProductos.put("Cola light", new Producto(72, "Cola light", 1.6, "cola_light.png", bebidas));
    }
    
}
