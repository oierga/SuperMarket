package gui;
import javax.swing.*;
import javax.swing.border.CompoundBorder;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;

import db.ServicioPersistenciaBD;
import domain.CarritoCompras;
import domain.Producto;
import domain.TipoCategoria;
import domain.TipoUsuario;
import domain.Usuario;
import main.Main;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class VentanaProductosDeCategoria extends JFrame {
	private static final long serialVersionUID = 1L;
    private Usuario usuario;
    protected String categoriaSeleccionada;
    private static TipoUsuario tipoUsuario;
    private double totalCarrito;
    protected ArrayList<Producto> productosCarrito = new ArrayList<>();
    protected HashMap<Producto, Integer> productosCarritoUnidad = new HashMap<>();
    private VentanaCategorias ventanaCategorias;
    private Color colorPrimario = new Color(76, 175, 80);
    private Color colorSecundario = new Color(245, 245, 245);
    private Color colorAccent = new Color(33, 33, 33);
    private JPanel productGridPanel;
    private Map<String, String[]> productosPorCategoria;

	    
    public VentanaProductosDeCategoria(String categoriaSeleccionada) {
        this.usuario = ServicioPersistenciaBD.getInstance().getUsuario();
        this.categoriaSeleccionada = categoriaSeleccionada;
    	//Configuracion ventana
        setTitle("Supermercado - Página Inicial");
        setSize(1290, 800);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
       
        //Panel principal
        JPanel mainPanel = new JPanel(new BorderLayout());

        //Top panel, panel del banner
        JPanel topPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        topPanel.setBackground(new Color(242, 243, 244));
        ImageIcon bannerIcon = cargarImagen("/images/banner0.png",1200,500);
       JLabel carritoLabel = crearIcono("/images/cart.png",80,80);
        Image bannerImage = bannerIcon.getImage().getScaledInstance(1200, 130, Image.SCALE_SMOOTH);; // Obtener la imagen de la imagen cargada
        JLabel bannerLabel = new JLabel(new ImageIcon(bannerImage));
        
        bannerLabel.addMouseListener(new java.awt.event.MouseAdapter() {
           public void mouseClicked(java.awt.event.MouseEvent evt) {
            	hacerSocio();
            }
        });
        
        JPanel bannerPanel = crearBannerConCambioDeImagen(); 
        redimensionarBanner(bannerLabel,0);
        bannerPanel.setLayout(new FlowLayout(FlowLayout.LEADING));  // Centrar la imagen
        bannerPanel.setBackground(Color.LIGHT_GRAY);  // Color de fondo opcional
        bannerPanel.add(bannerLabel);
        bannerPanel.add(carritoLabel);
        bannerPanel.setBackground(new Color(76, 175, 80));
       
        

        // Llamamos a la nueva función para crear el banner con imágenes cambiantes
       
        topPanel.add(bannerPanel);

        // Añadir topPanel al contenedor principal
        mainPanel.add(topPanel, BorderLayout.NORTH);

       //Panel botones navegacion
        
       
      
     // Panel para los iconos
        JPanel panelIconos = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 10));
        panelIconos.setOpaque(false); // Hacer que el fondo sea transparente

        // Icono de perfil
        JLabel iconoPerfil = crearIcono("/images/profile.png", 32, 32);
        iconoPerfil.setToolTipText("Perfil");
        iconoPerfil.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                //abrirPerfil(); // Método que abrirá la ventana del perfil
            }
        });

        // Icono de carrito
        JLabel iconoCarrito = crearIcono("/images/cart.png", 32, 32);
        iconoCarrito.setToolTipText("Carrito");
        iconoCarrito.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                abrirVentanaCarrito();
            }
        });

        // Añadir los iconos al panel
        panelIconos.add(iconoPerfil);
        panelIconos.add(iconoCarrito);

        // Añadir el panelIconos al topPanel
        
        //Configuracion del logo en el top panel
        ImageIcon logoIcon = new ImageIcon(VentanaSupermarket.class.getResource("/images/crema.png"));
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
        JPanel espacioVerde = new JPanel();
        espacioVerde.setBackground(new Color(76, 175, 80)); // Color verde, igual que el fondo del menú
        espacioVerde.setPreferredSize(new Dimension(1000, 30));
        auxPanel.add(bannerPanel,BorderLayout.NORTH);
        JPanel espacioVerdeBanner = new JPanel();
        espacioVerdeBanner.setBackground(new Color(76, 175, 80)); 
        espacioVerdeBanner.setPreferredSize(new Dimension(1000, 30));
        auxPanel.add(espacioVerdeBanner, BorderLayout.CENTER);

       
     // Panel de productos con scroll

        // Panel de productos con ScrollPane
        String[] frutas = { "banana.png", "fresa.png", "kiwi.png", "mango.png", "manzana.png", "naranja.png", "pera.png" };
        String[] verduras = { "cebolla.png", "espinaca.png", "lechuga.png", "pepino.png", "pimiento.png", "tomate.png", "zanahoria.png" };
        String[] lacteos = { "kefir.png", "leche.png", "queso.png", "yogur.png" };
        String[] carnes = { "bacon.png", "cordero.png", "lomo.png", "pavo.png", "pollo.png", "salchicha.png", "ternera.png" };
        String[] bebidas = { "agua.png", "cafe.png", "cerveza.png", "refresco.png", "te.png", "vino.png", "zumo_naranja.png" };
        String[] panaderia = { "bagel.png", "baguette.png", "croissant.png", "donut.png", "empanada.png", "magdalenas.png", "pan.png" };
        
        productosPorCategoria = new HashMap<>();
        productosPorCategoria.put("Frutas", frutas);
        productosPorCategoria.put("Verduras", verduras);
        productosPorCategoria.put("Lacteos", lacteos);
        productosPorCategoria.put("Carnes", carnes);
        productosPorCategoria.put("Bebidas", bebidas);
        productosPorCategoria.put("Panadería", panaderia);
        
     // Panel de productos con FlowLayout (4 productos por fila)
        productGridPanel = new JPanel();
        productGridPanel.setLayout(new GridLayout(0, 3, 15, 15));
        productGridPanel.setBorder(BorderFactory.createEmptyBorder(40, 0, 0, 0));
// 3 columnas, filas dinámicas, espaciado de 15px
        productGridPanel.setBackground(Color.WHITE);

        // Añadir productos al panel
        String[] productosCategoriaSeleccionada = productosPorCategoria.getOrDefault(this.categoriaSeleccionada, new String[0]);
        ArrayList<Producto> productos = ServicioPersistenciaBD.getInstance().cargarTodosProductos();

        for (String imagen : productosCategoriaSeleccionada) {
            for (Producto producto : productos) {
                if (producto.getRutaImagen().contains(imagen)) {
                    productGridPanel.add(createProductPanel(
                        "/" + producto.getRutaImagen(),
                        producto.getNombre().substring(0, 1).toUpperCase() + producto.getNombre().substring(1).replace("_", " "),
                        "Categoría: " + this.categoriaSeleccionada + " | " +
                        "Producto: " + producto.getNombre(),
                       producto.getPrecio())
                    );
                }
            }
        }

        // Hacer scrollable el panel de productos
        JScrollPane scrollPane = new JScrollPane(productGridPanel);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        scrollPane.getVerticalScrollBar().setUnitIncrement(16);

     // Crear el panel del menú
        JPanel menuPanel = crearMenuNavegacion();

        // Incorporar el menú debajo del banner
        auxPanel.add(menuPanel, BorderLayout.CENTER); // Añadir el menú al área debajo del banner
        crearMenu();
     // Añadir un panel vacío o espacio verde adicional debajo del menú
     // Panel para la barra de búsqueda
        JPanel barraBusquedaPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 10));
        barraBusquedaPanel.setBackground(new Color(76, 175, 80)); // Color de fondo, igual que el resto del diseño

        
        JTextField textBuscar = new JTextField();
        textBuscar.setPreferredSize(new Dimension(300, 30));
        textBuscar.getDocument().addDocumentListener(new javax.swing.event.DocumentListener() {
            @Override
            public void insertUpdate(javax.swing.event.DocumentEvent e) {
                actualizarProductosPorCategoria("",false,textBuscar.getText());
            }

            @Override
            public void removeUpdate(javax.swing.event.DocumentEvent e) {
                actualizarProductosPorCategoria("",false,textBuscar.getText());
                if (textBuscar.getText().length()==0) {
                	actualizarProductosPorCategoria(getCategoriaSeleccionada(),false,"");
                }
            }

            @Override
            public void changedUpdate(javax.swing.event.DocumentEvent e) {
                actualizarProductosPorCategoria("",false,textBuscar.getText());
            }
        });

        JButton botonBuscar = new JButton("Buscar");
        botonBuscar.setPreferredSize(new Dimension(100, 30)); // Ajusta el tamaño del botón
        botonBuscar.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				actualizarProductosPorCategoria("",false,textBuscar.getText().toLowerCase());
			}
        	
        });
        // Añadir componentes al panel
        barraBusquedaPanel.add(textBuscar,FlowLayout.LEFT);
        barraBusquedaPanel.add(botonBuscar);
        barraBusquedaPanel.setBackground(colorPrimario.darker());
        // Añadir la barra de búsqueda al panel principal
        auxPanel.add(barraBusquedaPanel, BorderLayout.SOUTH);

       

        mainPanel.add(crearPanelCentral(), BorderLayout.CENTER);
        mainPanel.add(crearPanelContacto(), BorderLayout.SOUTH);
        mainPanel.add(auxPanel, BorderLayout.NORTH);
        mainPanel.add(scrollPane, BorderLayout.CENTER);

        add(mainPanel);
        setVisible(true);
    }
	protected String getCategoriaSeleccionada() {
		// TODO Auto-generated method stub
		return this.categoriaSeleccionada;
	}
	private JPanel crearMenuNavegacion() {
        JPanel menuPanel = new JPanel();
       
        menuPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 20, 10)); // Centrado con espacios
        menuPanel.setBackground(new Color(76, 175, 80)); // Fondo color primario

        // Opciones del menú con íconos
        String[][] opcionesMenu = {
            {"Categorías", "/images/categorias.png"},
            {"Ofertas", "/images/ofertas.png"},
            {"Carrito", "/images/carrito.png"},
            {"Zona socios", "/images/socios.png"},
            {"Mi cuenta", "/images/cuenta.png"}
        };

        for (String[] opcion : opcionesMenu) {
            String texto = opcion[0];
            String rutaIcono = opcion[1];

            // Crear un botón estilizado
            JButton boton = new JButton(texto);
            boton.setFocusPainted(false);
            boton.setContentAreaFilled(true);
            boton.setOpaque(true);
            boton.setBackground(new Color(76, 175, 80)); // Color verde
            boton.setForeground(Color.WHITE); // Texto blanco
            boton.setFont(new Font("Segoe UI", Font.BOLD, 14));
            boton.setHorizontalTextPosition(SwingConstants.CENTER);
            boton.setVerticalTextPosition(SwingConstants.BOTTOM);
            boton.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(Color.WHITE, 2),
                BorderFactory.createEmptyBorder(10, 15, 10, 15)
            ));

            // Añadir interactividad al botón
            boton.addMouseListener(new java.awt.event.MouseAdapter() {
                private JPopupMenu categoriasMenu;

                @Override
                public void mouseEntered(java.awt.event.MouseEvent e) {
                    boton.setBackground(new Color(56, 142, 60));
                    if ("Categorías".equals(texto)) {
                        if (categoriasMenu == null) {
                            categoriasMenu = crearMenuCategorias(boton);
                        }
                        categoriasMenu.show(boton, 0, boton.getHeight());
                    }
                }

                @Override
                public void mouseExited(java.awt.event.MouseEvent e) {
                    boton.setBackground(new Color(76, 175, 80));
                    if (categoriasMenu != null) {
                        verificarCierreMenu(categoriasMenu, boton);
                    }
                }
            });

            // Añadir acción según la opción
            boton.addActionListener(e -> {
                switch (texto) {
                    case "Ofertas":
                        mostrarOfertas();
                        break;
                    case "Carrito":
                        abrirVentanaCarrito();
                        break;
                    case "Zona socios":
                        mostrarZonaSocios();
                        break;
                    case "Mi cuenta":
                        mostrarPerfil();
                        break;
                }
            });

            // Agregar botón al panel del menú
            menuPanel.add(boton);
        }

        return menuPanel;
    }

    // Método para crear el menú de categorías
    private JPopupMenu crearMenuCategorias(JButton boton) {
        JPopupMenu categoriasMenu = new JPopupMenu();

        // Listado de categorías (puedes cargar estas categorías dinámicamente)
        String[] categorias = {"Frutas", "Verduras", "Lacteos", "Carnes", "Bebidas", "Panadería"};
        for (String categoria : categorias) {
            JMenuItem item = new JMenuItem(categoria);
            item.setFont(new Font("Segoe UI", Font.PLAIN, 14));
            item.addActionListener(e -> {
                // Lógica para cada categoría
            	categoriaSeleccionada = categoria;
                actualizarProductosPorCategoria(categoria,false,"");
            });
            categoriasMenu.add(item);
        }

        return categoriasMenu;
    }

    // Método para verificar si el ratón no está en el menú y cerrarlo
    private void verificarCierreMenu(JPopupMenu menu, JButton boton) {
        Timer timer = new Timer(1000, e -> {
            PointerInfo pointerInfo = MouseInfo.getPointerInfo();
            if (pointerInfo != null) {
                Point location = pointerInfo.getLocation();
                SwingUtilities.convertPointFromScreen(location, boton);

                // Cerrar el menú si el ratón no está sobre el botón ni sobre el menú
                if (!boton.getBounds().contains(location) && !menu.getBounds().contains(location)) {
                    menu.setVisible(false);
                    ((Timer) e.getSource()).stop();
                }
            }
        });
        timer.setRepeats(true);
        timer.start();
    }


    private JPanel createProductPanel(String imagePath, String productName, String productDescription, double price) {
        JPanel productPanel = new JPanel();
        productPanel.setLayout(new BoxLayout(productPanel, BoxLayout.Y_AXIS)); // Alineación vertical para los componentes
        productPanel.setPreferredSize(new Dimension(180, 260)); // Tamaño para mantener diseño limpio
        productPanel.setBackground(Color.WHITE);

        // Tamaño deseado para las imágenes más pequeñas
        int imageWidth = 120;
        int imageHeight = 120;

        // Imagen del producto
        JLabel productImage = new JLabel();
        try {
            // Ajustar la imagen
            ImageIcon originalIcon = cargarImagen(imagePath, imageWidth, imageHeight);
            Image scaledImage = originalIcon.getImage().getScaledInstance(imageWidth, imageHeight, Image.SCALE_SMOOTH);
            productImage.setIcon(new ImageIcon(scaledImage));
        } catch (Exception e) {
            // Manejo de errores si la imagen no se puede cargar
            System.out.println("Error al cargar la imagen: " + imagePath);
            productImage.setText("Imagen no disponible");
        }
        productImage.setAlignmentX(Component.CENTER_ALIGNMENT); // Centrar la imagen

        // Nombre del producto
        JLabel nameLabel = new JLabel(productName);
        nameLabel.setFont(new Font("Arial", Font.BOLD, 14));
        nameLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        // Descripción del producto
        JLabel descriptionLabel = new JLabel(productDescription );
        descriptionLabel.setFont(new Font("Arial", Font.PLAIN, 12));
        descriptionLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        // Precio
        JLabel priceLabel = new JLabel(""+price+" €");
        priceLabel.setFont(new Font("Arial", Font.BOLD, 16));
        priceLabel.setForeground(new Color(0, 102, 204)); // Color similar al de Carrefour
        JLabel discountLabel = new JLabel();
        discountLabel.setPreferredSize(new Dimension(60, 25));  // Tamaño pequeño del label
        JPanel discountPanel = new JPanel();

        Map<String,Double> productosConDescuento = ServicioPersistenciaBD.getInstance().obtenerNombresConDescuento();
        // Si el producto está en oferta, mostrar el porcentaje de descuento
        if (productosConDescuento.containsKey(productName.toLowerCase())) {
            // Crear el label de descuento
        	discountLabel.setText(String.format("-%.0f%%", productosConDescuento.get(productName.toLowerCase())));
            discountLabel.setBackground(Color.RED);  // Fondo rojo
            discountLabel.setForeground(Color.WHITE);  // Texto rojo
            discountLabel.setFont(new Font("Arial", Font.BOLD, 12));
            discountLabel.setOpaque(true);  // Asegura que el fondo se vea
            discountLabel.setHorizontalAlignment(SwingConstants.CENTER);

            // Calcular el precio descontado
            double precioDescontado = price * (1 - (productosConDescuento.get(productName.toLowerCase()) * 0.01));
            String priceText = "<html><strike>" + price + " €</strike>  " + String.format("%.2f", precioDescontado) + " € </html>";
            priceLabel.setText(priceText);
            priceLabel.setForeground(Color.RED);
            priceLabel.setAlignmentX(Component.CENTER_ALIGNMENT);  // Alineación central del precio

            // Panel para el label de descuento, alineado a la izquierda
            discountPanel.setLayout(new FlowLayout(FlowLayout.LEFT, 0, 0));
        }
        discountPanel.setBackground(Color.WHITE); 

        discountPanel.add(discountLabel);

        // Añadir el panel de descuento en la parte superior izquierda del panel del producto
        productPanel.add(discountPanel);

        // Ajustar el alignment del precio para que quede centrado
        priceLabel.setHorizontalAlignment(SwingConstants.CENTER);  //
        // Array de unidades
        String[] unidades = {"1", "2", "3", "4", "5", "6", "7", "8", "9", "10"};
        JComboBox<String> comboUnidades = new JComboBox<>(unidades);
        comboUnidades.setSelectedIndex(0); // Seleccionar "1" como valor inicial
        comboUnidades.setBorder(BorderFactory.createEmptyBorder());

        // Hacer el JComboBox más pequeño
        comboUnidades.setPreferredSize(new Dimension(60, 30)); // Ancho 60px, alto 30px

        // Botón de "Añadir"
        JButton addButton = new JButton("Añadir al carrito");
        addButton.setBackground(new Color(0, 102, 204));
        addButton.setForeground(Color.BLACK);
        addButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        addButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int opcion = JOptionPane.showConfirmDialog(null, "¿Desea añadir este producto a la cesta?");
                if (opcion == 0) { 
                    CarritoCompras.getInstance().agregarProducto(
                        ServicioPersistenciaBD.getInstance().productoPorNombre(productName.toLowerCase()),  
                        Integer.parseInt((String) comboUnidades.getSelectedItem())
                    );
                    JOptionPane.showMessageDialog(null, "Producto añadido al carrito.");
                    CarritoCompras.getInstance().mostrarCarrito();
                }
            }
        });

        // Panel para el botón y combo (para alinearlos horizontalmente)
        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 5, 5)); // Alineación a la izquierda con espaciado de 5px
        buttonPanel.setBackground(null); // Eliminar cualquier color de fondo del panel
        buttonPanel.add(comboUnidades);
        buttonPanel.add(addButton);

        // Añadir componentes al panel del producto en el orden correcto
        productPanel.add(productImage);
        productPanel.add(Box.createVerticalStrut(3));
        productPanel.add(nameLabel);
        productPanel.add(descriptionLabel);
        productPanel.add(Box.createVerticalStrut(6));
        productPanel.add(priceLabel);
        productPanel.add(Box.createVerticalStrut(3));
        productPanel.add(buttonPanel); // Añadir panel con el JComboBox y el botón

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
        
        JPanel infoPanel = new JPanel();
        infoPanel.setLayout(new BoxLayout(infoPanel, BoxLayout.X_AXIS));
        infoPanel.setBackground(new Color(76, 175, 80));  // Fondo primario
        
        for (String[] info : contactInfo) {
            JPanel linePanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
            linePanel.setBackground(Color.WHITE);
            
            JLabel lblIcono = new JLabel(info[0]);
            lblIcono.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 16));
            
            JLabel lblTexto = new JLabel(info[1]);
            lblTexto.setFont(new Font("Segoe UI", Font.PLAIN, 14));
            lblTexto.setForeground(Color.WHITE);
            
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
    private void actualizarProductosPorCategoria(String categoriaSeleccionada,boolean soloOfertas,String productoABuscar) {
        productGridPanel.removeAll();
        Map<String,Double> nombresConDescuento = ServicioPersistenciaBD.getInstance().obtenerNombresConDescuento();
        String[] productosCategoriaSeleccionada = productosPorCategoria.getOrDefault(categoriaSeleccionada, new String[0]);
        ArrayList<Producto> productos = ServicioPersistenciaBD.getInstance().cargarTodosProductos();
        if (soloOfertas) { //Mostrar Ofertas
            	for (Producto producto: productos) {

            		if (nombresConDescuento.containsKey(producto.getNombre().toLowerCase())) {
               			 productGridPanel.add(createProductPanel(
                                    "/"+producto.getRutaImagen(), producto.getNombre().substring(0,1).toUpperCase()+producto.getNombre().substring(1,producto.getNombre().length()).replace("_", " "),
                                    "Categoria: "+producto.getCategoria().getNombre()+" | Producto: "+(producto.getNombre().substring(0,1).toUpperCase()+producto.getNombre().substring(1)).replace("_", " "),
                                    producto.getPrecio()
                                ));
            			}   
            		
            	
        	}
        
        }else if (!(productoABuscar.equals(""))){ //Buscar producto por input
        	
        		for (Producto producto: productos) {
        			if (producto.getNombre().contains(productoABuscar)) {
            			productGridPanel.add(createProductPanel("/"+producto.getRutaImagen(),producto.getNombre().substring(0,1).toUpperCase()
            					+producto.getNombre().substring(1),"Categoria: "+producto.getCategoria().getNombre()+" | Producto: "
            							+producto.getNombre().substring(0,1).toUpperCase()+producto.getNombre().substring(1),producto.getPrecio()));

        			
        		}
        	}
        }else {
            	for (String imagen : productosCategoriaSeleccionada) {
                	for (Producto producto: productos) {
                			if (producto.getRutaImagen().contains(imagen)) {
                   			 productGridPanel.add(createProductPanel(
                                        "/"+producto.getRutaImagen(), producto.getNombre().substring(0,1).toUpperCase()+producto.getNombre().substring(1,producto.getNombre().length()).replace("_", " "),
                                        "Categoria: "+categoriaSeleccionada+" |  Producto: "+(producto.getNombre().substring(0,1).toUpperCase()+producto.getNombre().substring(1)).replace("_", " "),
                                        producto.getPrecio()
                                    ));
                			}   
                		}
            	}
        	}
        	
        
        productGridPanel.revalidate();
        productGridPanel.repaint();
    }
    
    private void crearMenu() {
        JMenuBar menuBar = new JMenuBar();
        menuBar.setBackground(Color.WHITE);

        JMenu menuCuenta = new JMenu("Mi Cuenta");
        menuCuenta.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        
        JMenuItem itemCarrito = new JMenuItem("Ver Carrito");
        itemCarrito.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        itemCarrito.addActionListener(e -> abrirVentanaCarrito());
        
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
    private JLabel crearIcono(String ruta, int ancho, int alto) {
        ImageIcon icono = new ImageIcon(ruta);
        Image img = icono.getImage().getScaledInstance(ancho, alto, Image.SCALE_SMOOTH);
        return new JLabel(new ImageIcon(img));
    }
    private void abrirVentanaCarrito() {
        VentanaCarrito.getInstance().actualizarListaProductos();
        VentanaCarrito.getInstance().recalcularDescuento();
        VentanaCarrito.getInstance().setVisible(true);
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

	   new VentanaSupermarket();
   }
   private ImageIcon cargarImagen(String nombreImagen, int ancho, int alto) {
       java.net.URL imageUrl = getClass().getResource(nombreImagen);        
       if (imageUrl != null) {
           
           
           ImageIcon iconoOriginal = new ImageIcon(getClass().getResource(nombreImagen));
           Image imagenEscalada = iconoOriginal.getImage().getScaledInstance(ancho, alto, Image.SCALE_SMOOTH);


           return new ImageIcon(imagenEscalada);
       } else {
           System.err.println("No se pudo encontrar la imagen: " + nombreImagen);
           return null;
       }
   }

	private void mostrarZonaSocios() {
	    JOptionPane.showMessageDialog(this, "Accediendo a la Zona de Socios...");
	    new VentanaSocio(ServicioPersistenciaBD.getInstance().getUsuario().getNombreDeUsuario());
	}

	private void mostrarOfertas() {
	    actualizarProductosPorCategoria("",true,"");
	}

	private void mostrarPerfil() {
	    JOptionPane.showMessageDialog(this, "Mostrando Mi Cuenta...");
	}
	// Función para crear el banner con cambio de imagen cada 5 segundos
	private JPanel crearBannerConCambioDeImagen() {
	    JPanel bannerPanel = new JPanel();
	    bannerPanel.setLayout(new BorderLayout());
	    
	    // Crear una etiqueta para mostrar la imagen del banner
	    JLabel bannerLabel = new JLabel();
	    int[] imagenIndex = { 0 };  // Array para hacer el índice mutable dentro del Timer

	    bannerPanel.add(bannerLabel, BorderLayout.CENTER);
	    
	    // Crear el ícono de carrito (este se mantendrá fijo)
	    JLabel carritoLabel = crearIcono("/images/cart.png", 80, 80);
	    bannerPanel.add(carritoLabel, BorderLayout.EAST);
	    
	    // Array de imágenes del banner
	    String[] imagenesBanner = {
	        "/images/banner0.png",  // Primera image  // Segunda imagen
	        "/images/banner1.png"   // Tercera imagen
	    };

	    // Función para cambiar la imagen cada 5 segundos
	    
	    // Crear el Timer para cambiar la imagen cada 5 segundos
	    Timer bannerTimer = new Timer(5000, e -> {
	        // Cambiar la imagen del banner
	        imagenIndex[0]++;
	        
	        // Si el índice supera el tamaño del array, volver al inicio
	        if (imagenIndex[0] >= imagenesBanner.length) {
	            imagenIndex[0] = 0;
	        }
	        
	        // Cambiar la imagen en el JLabel
	        bannerLabel.setIcon(new ImageIcon(getClass().getResource(imagenesBanner[imagenIndex[0]])));
	        redimensionarBanner(bannerLabel,imagenIndex[0]);

		    addComponentListener(new java.awt.event.ComponentAdapter() {
	            @Override
	            public void componentResized(java.awt.event.ComponentEvent evt) {
	                redimensionarBanner(bannerLabel,imagenIndex[0]);
	                


	            }
	        });
	    });
	    bannerTimer.start();
	    
	    // Establecer el tamaño y color del fondo
	    bannerPanel.setBackground(new Color(76, 175, 80)); // Color de fondo
	    // Establecer el evento para hacer algo al hacer clic en el banner
	    bannerLabel.addMouseListener(new java.awt.event.MouseAdapter() {
	        public void mouseClicked(java.awt.event.MouseEvent evt) {
	            hacerSocio();
	        }
	    });

	    return bannerPanel;
	}
	private void redimensionarBanner(JLabel bannerLabel,int index) {
	    // Obtener el tamaño actual del JFrame
	    int anchoVentana = getWidth();
	    int alturaBanner = (int) (anchoVentana * 0.1); // Ajustar la altura proporcionalmente al ancho

	    // Escalar la imagen del banner
	    ImageIcon bannerIcon = new ImageIcon(VentanaSupermarket.class.getResource("/images/banner"+index+".png"));
	    Image bannerImage = bannerIcon.getImage().getScaledInstance(anchoVentana, alturaBanner, Image.SCALE_SMOOTH);

	    // Actualizar el JLabel con la imagen escalada
	    bannerLabel.setIcon(new ImageIcon(bannerImage));

	    // Refrescar el JLabel
	    bannerLabel.revalidate();
	    bannerLabel.repaint();
	}
}