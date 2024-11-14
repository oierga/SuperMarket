package gui;
import javax.swing.*;
import javax.swing.border.CompoundBorder;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;

import domain.CarritoCompras;
import domain.Producto;
import domain.TipoUsuario;
import domain.Usuario;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class VentanaSupermarketDespues extends JFrame {
	 private static final long serialVersionUID = 1L;
	    private Usuario usuario;
	    private static TipoUsuario tipoUsuario;
	    private double totalCarrito;
	    protected ArrayList<Producto> productosCarrito = new ArrayList<>();
	    protected HashMap<Producto, Integer> productosCarritoUnidad = new HashMap<>();
	    private VentanaCarrito ventanaCarrito;
	    private VentanaCategorias ventanaCategorias;
	    private Color colorPrimario = new Color(76, 175, 80);
	    private Color colorSecundario = new Color(245, 245, 245);
	    private Color colorAccent = new Color(33, 33, 33);

    public VentanaSupermarketDespues(Usuario usuario) {
    	//Configuracion ventana
    	ventanaCarrito = new VentanaCarrito(ventanaCategorias,usuario);
        setTitle("Supermercado - Página Inicial");
        setSize(1600, 1000);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
       
        //Panel principal
        JPanel mainPanel = new JPanel(new BorderLayout());

        //Top panel, panel del banner
        JPanel topPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        topPanel.setBackground(new Color(242, 243, 244));
        ImageIcon bannerIcon = cargarImagen("banner.png",1200,500);
        Image bannerImage = bannerIcon.getImage().getScaledInstance(1200, 130, Image.SCALE_SMOOTH);; // Obtener la imagen de la imagen cargada
        JLabel bannerLabel = new JLabel(new ImageIcon(bannerImage));
        bannerLabel.addMouseListener(new java.awt.event.MouseAdapter() {
           public void mouseClicked(java.awt.event.MouseEvent evt) {
            	hacerSocio();
            }
        });
        JPanel bannerPanel = new JPanel();
        bannerPanel.setLayout(new FlowLayout(FlowLayout.LEADING));  // Centrar la imagen
        bannerPanel.setBackground(Color.LIGHT_GRAY);  // Color de fondo opcional
        bannerPanel.add(bannerLabel);
        bannerPanel.setBackground(new Color(76, 175, 80));
       
       //Panel botones navegacion
        JButton verTodoButton = new JButton("Ver todos los productos");
        JButton verDestacadosButton = new JButton("Ver productos destacados");
        JPanel panelBotonesNavegacion = new JPanel(new BorderLayout());
        JLabel labelProductos = new JLabel();
        verDestacadosButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				labelProductos.setText("Productos Destacados");
			}
        	
        });
        verTodoButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				labelProductos.setText("Todos los productos");
				
			}
        	
        });
        JLabel categoria = new JLabel("Categoría: ");
        String[] categorias = {"Seleccionar","Frutas", "Verduras", "Lácteos", "Bebidas", "Carnes"};
        JComboBox<String> comboBox = new JComboBox<>(categorias);
        
        labelProductos.setFont(new Font("Arial", Font.BOLD, 16));
        JPanel panelBotones = new JPanel();
        JButton botonCarrito = new JButton("Ver Carrito");
        botonCarrito.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				abrirVentanaCarrito(productosCarrito,productosCarritoUnidad);
			}
        	
        });
        panelBotones.setBackground(new Color(76, 175, 80));
        panelBotones.add(categoria);
        panelBotones.add(comboBox);
        comboBox.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				labelProductos.setText((String) comboBox.getSelectedItem());
			}
        	
        });
        panelBotones.add(verDestacadosButton);
        panelBotones.add(verTodoButton);
        panelBotones.add(botonCarrito);
        panelBotonesNavegacion.add(labelProductos,BorderLayout.WEST);
        panelBotonesNavegacion.add(panelBotones, BorderLayout.CENTER);
       
        panelBotonesNavegacion.setBackground(new Color(76, 175, 80));
      
        
        
        //Configuracion del logo en el top panel
        ImageIcon logoIcon = new ImageIcon(VentanaSupermarketDespues.class.getResource("/images/crema.png"));
        Image logoImage = logoIcon.getImage().getScaledInstance(100, 70, Image.SCALE_SMOOTH); // Cambia 100, 100 por el tamaño que desees
        JButton logoButton = new JButton(new ImageIcon(logoImage));
        logoButton.setBorderPainted(false);
        logoButton.setContentAreaFilled(false);
        logoButton.setFocusable(false);
        topPanel.add(logoButton);
        
        mainPanel.setBackground(new Color(76, 175, 80));
        topPanel.setBackground(new Color(76, 175, 80));
        
        JPanel auxPanel = new JPanel(new BorderLayout());
        auxPanel.setBackground(new Color(76, 175, 80));
        auxPanel.add(bannerPanel,BorderLayout.NORTH);
        auxPanel.add(panelBotonesNavegacion, BorderLayout.SOUTH);
       

        // Panel de productos con ScrollPane
        JPanel productGridPanel = new JPanel(new GridLayout(0, 5, 10, 10));
        productGridPanel.setBackground(Color.WHITE);
        String[] imagenes = { "agua.png", "bacon.png", "bagel.png", "baguette.png", "banana.png", "bebidas.png", "cafe.png", 
                "carnes.png", "cebolla.png", "cerveza.png", "cordero.png", "crema.png", "croissant.png", 
                "deustowallpaper.jpg", "donut.png", "empanada.png", "espinaca.png", "fresa.png", "frutas.png", 
                "helado.png", "kefir.png", "kiwi.png", "lacteos.png", "leche.png", "lechuga.png", "lomo.png", 
                "magdalenas.png", "mango.png", "mantequilla.png", "manzana.png", "naranja.png", "pan.png", 
                "panaderia.png", "pavo.png", "pepino.png", "pera.png", "pimiento.png", "pollo.png", "queso.png", 
                "refresco.png", "salchicha.png", "te.png", "ternera.png", "tomate.png", "verduras.png", "vino.png", 
                "yogur.png", "zanahoria.png", "zumo_naranja.png" };
        
        for (String imagen: imagenes) {  
            String imagePath = "/images/"+imagen;
            productGridPanel.add(createProductPanel(
                imagePath,  
                imagen.substring(0,imagen.length()-4),
                "Descripción breve del producto.",
                String.format("%.2f €", (Math.random() * 10) + 1)
            ));
            
        }
        JScrollPane scrollPane = new JScrollPane(productGridPanel);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        
        crearMenu();
        mainPanel.add(crearPanelCentral(), BorderLayout.CENTER);
        mainPanel.add(crearPanelContacto(), BorderLayout.SOUTH);
        mainPanel.add(auxPanel, BorderLayout.NORTH);
        mainPanel.add(scrollPane, BorderLayout.CENTER);

        add(mainPanel);
        setVisible(true);
    }
    private JPanel createProductPanel(String imagePath, String productName, String productDescription, String price) {
        JPanel productPanel = new JPanel();
        
        productPanel.setLayout(new BoxLayout(productPanel, BoxLayout.Y_AXIS));
        productPanel.setBackground(Color.WHITE);
        productPanel.setBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY));

        // Tamaño deseado para las imágenes
        int imageWidth = 100;
        int imageHeight = 100;

        // Imagen del producto
        JLabel productImage = new JLabel();
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
        }

        // Nombre del producto
        JLabel nameLabel = new JLabel(productName);
        nameLabel.setFont(new Font("Arial", Font.BOLD, 14));
        nameLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        // Descripción del producto
        JLabel descriptionLabel = new JLabel("<html><p style='text-align:center;'>" + productDescription + "</p></html>");
        descriptionLabel.setFont(new Font("Arial", Font.PLAIN, 12));
        descriptionLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        // Precio
        JLabel priceLabel = new JLabel(price);
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
			        String precioAux = price.substring(0,price.length()-1);
			        double precio = Double.parseDouble(precioAux);
					Producto producto = new Producto (productName, precio);
					System.out.print(precio);
					ventanaCarrito.agregarProductoAlCarrito(producto, unidadesSeleccionadas);
					productosCarritoUnidad.put(producto, unidadesSeleccionadas);
					productosCarrito.add(producto);
					
				}
				
			}
        	
        });
        // Añadir componentes al panel del producto
        productPanel.add(productImage);
        productPanel.add(Box.createVerticalStrut(5));
        productPanel.add(nameLabel);
        productPanel.add(descriptionLabel);
        productPanel.add(priceLabel);
        productPanel.add(Box.createVerticalStrut(5));
        productPanel.add(addButton);
        productPanel.add(comboUnidades);
        return productPanel;
    }
    private JPanel crearPanelCentral() {
        JPanel panel = new JPanel(new BorderLayout(0, 20));
        JPanel productosPanel = new JPanel();
        JButton verDestacadosButton = new JButton("Productos destacados");
        JButton verTodosButton = new JButton("Ver todos los productos");
        productosPanel.setBackground(new Color(76, 175, 80));
        productosPanel.add(verTodosButton);
        productosPanel.add(verDestacadosButton);
        panel.add(productosPanel, BorderLayout.NORTH);
        panel.setBackground(colorSecundario);
        
        JPanel caracteristicasPanel = new JPanel(new GridLayout(1, 3, 20, 0));
        caracteristicasPanel.setBackground(colorSecundario);
        
        caracteristicasPanel.add(crearPanelCaracteristica("Envío Gratis", "En pedidos superiores a 50€", "🚚"));
        caracteristicasPanel.add(crearPanelCaracteristica("Productos Frescos", "Calidad garantizada", "🥬"));
        caracteristicasPanel.add(crearPanelCaracteristica("Atención 24/7", "Estamos para ayudarte", "💬"));
        
        JPanel botonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        botonPanel.setBackground(colorSecundario);
        
       
        
        panel.add(caracteristicasPanel, BorderLayout.NORTH);
        panel.add(botonPanel, BorderLayout.CENTER);
        
        return panel;
    }
    private JPanel crearPanelCaracteristica(String titulo, String descripcion, String emoji) {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBackground(Color.WHITE);
        panel.setBorder(new CompoundBorder(
            new LineBorder(Color.LIGHT_GRAY, 1, true),
            new EmptyBorder(20, 15, 20, 15)
        ));
        
        JLabel lblEmoji = new JLabel(emoji);
        lblEmoji.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 36));
        lblEmoji.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        JLabel lblTitulo = new JLabel(titulo);
        lblTitulo.setFont(new Font("Segoe UI", Font.BOLD, 18));
        lblTitulo.setForeground(colorAccent);
        lblTitulo.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        JLabel lblDescripcion = new JLabel(descripcion);
        lblDescripcion.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        lblDescripcion.setForeground(Color.GRAY);
        lblDescripcion.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        panel.add(lblEmoji);
        panel.add(Box.createRigidArea(new Dimension(0, 10)));
        panel.add(lblTitulo);
        panel.add(Box.createRigidArea(new Dimension(0, 5)));
        panel.add(lblDescripcion);
        
        return panel;
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
    private void crearMenu() {
        JMenuBar menuBar = new JMenuBar();
        menuBar.setBackground(Color.WHITE);

        JMenu menuCuenta = new JMenu("Mi Cuenta");
        menuCuenta.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        
        JMenuItem itemCarrito = new JMenuItem("Ver Carrito");
        itemCarrito.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        itemCarrito.addActionListener(e -> abrirVentanaCarrito(productosCarrito,productosCarritoUnidad));
        
        if (tipoUsuario == TipoUsuario.USUARIO) {
            JMenuItem itemHacerseSocio = new JMenuItem("Hacerse Socio");
            itemHacerseSocio.setFont(new Font("Segoe UI", Font.PLAIN, 14));
            itemHacerseSocio.addActionListener(e -> hacerSocio());
            menuCuenta.add(itemHacerseSocio);
        }

        if (tipoUsuario == TipoUsuario.SOCIO) {
            JMenuItem itemSocio = new JMenuItem("Zona Socio");
            itemSocio.setFont(new Font("Segoe UI", Font.PLAIN, 14));
            itemSocio.addActionListener(e -> abrirVentanaSocio());
            menuCuenta.add(itemSocio);
        }
        
        JMenuItem itemSalir = new JMenuItem("Salir");
        itemSalir.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        itemSalir.addActionListener(e -> System.exit(0));
        
        menuCuenta.add(itemCarrito);
        menuCuenta.addSeparator();
        menuCuenta.add(itemSalir);
        
        menuBar.add(menuCuenta);

        if (tipoUsuario == TipoUsuario.ADMIN) {
            JMenu menuAdmin = new JMenu("Administración");
            menuAdmin.setFont(new Font("Segoe UI", Font.PLAIN, 14));
            
            JMenuItem itemAdminPanel = new JMenuItem("Panel de Administración");
            itemAdminPanel.setFont(new Font("Segoe UI", Font.PLAIN, 14));
            itemAdminPanel.addActionListener(e -> abrirVentanaAdmin());
            
            menuAdmin.add(itemAdminPanel);
            menuBar.add(menuAdmin);
        }
        
        setJMenuBar(menuBar);
    }
    
    private void abrirVentanaCarrito(ArrayList<Producto> productosCarrito2, HashMap<Producto,Integer> productosCarritoUnidad) {
        if (ventanaCarrito == null) {
            ventanaCarrito = new VentanaCarrito(ventanaCategorias, usuario);
        } else {
            if (!productosCarrito2.isEmpty()) {
                CarritoCompras carrito = ventanaCarrito.getCarrito();
                Map<Producto, Integer> carritoProductos = carrito.getProductos();
          
                // Añadir productos que aún no están en el carrito
                for (Producto producto : productosCarrito2) {
                    if (!carritoProductos.containsKey(producto)) {
                    	int unidades = productosCarritoUnidad.get(producto);
                        carrito.agregarProducto(producto, unidades);
                    }
                }
                ventanaCarrito.actualizarListaProductos();
                //ventanaCarrito.recalcularTotal(productosCarritoUnidad,productosCarrito);// Asegura que la lista visual se actualice
            }
        }
        ventanaCarrito.setVisible(true);
    }

    
    private void hacerSocio() {
        //usuario.setTipo(TipoUsuario.SOCIO);
    	int option = JOptionPane.showConfirmDialog(null, "¿Está seguro de que quiere hacerse socio?");
    	if (option==0) {
    		String codigoOferta = "ABC123";
        new VentanaSocio(codigoOferta).setVisible(true);
    	}
        
    }
    
    private void abrirVentanaSocio() {
        new VentanaSocio("ABC123").setVisible(true);
    }
    
    private void abrirVentanaAdmin() {
        new VentanaAdmin(usuario).setVisible(true);
    }
    
    public void actualizarTotalCarrito(double nuevoTotal) {
        totalCarrito = nuevoTotal;
        if (ventanaCategorias != null) {
            ventanaCategorias.actualizarTotalCarrito(nuevoTotal);
        }
    }
    // Método para crear el panel con el banner y los productos
    protected void vaciarCarrito() {
    	productosCarrito.removeAll(productosCarrito);
    	productosCarritoUnidad.clear();
    }
   public static void main(String[] args) {
       Usuario usuario = new Usuario("admin", "123", true, TipoUsuario.ADMIN.USUARIO);

	   new VentanaSupermarketDespues(usuario);
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