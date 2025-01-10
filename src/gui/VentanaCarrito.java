package gui;

import db.ServicioPersistenciaBD;

import domain.CarritoCompras;
import domain.Producto;
import domain.Usuario;

import javax.swing.*;
import java.awt.*;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class VentanaCarrito extends JFrame {
	
	private static final Color COLOR_PRINCIPAL = new Color(34, 139, 34);  
    private static final Color COLOR_SECUNDARIO = new Color(144, 238, 144);  
    private static final Color COLOR_FONDO = new Color(240, 255, 240);
	
	private static VentanaCarrito instance;
	private CarritoCompras carrito;
    private JPanel panelProductos;
    private JLabel labelTotal;
    private JTextField campoCupon;
    private JLabel labelDescuento;
    private double descuentoAplicado = 0;
    private Usuario usuario; 
    private  VentanaCategorias ventanaCategorias;
    
    VentanaCarrito() {
        this(null);  
    }
    
    public VentanaCarrito(VentanaCategorias ventanaCategorias) {
    	
    	this.usuario = ServicioPersistenciaBD.getInstance().getUsuario();
    	this.carrito = CarritoCompras.getInstance();
    	this.ventanaCategorias = ventanaCategorias; 
    	
        setTitle("Carrito de Compras");
        setSize(800, 500);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null); 

        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BorderLayout());
        mainPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        mainPanel.setBackground(COLOR_FONDO);

        panelProductos = new JPanel();
        panelProductos.setLayout(new BoxLayout(panelProductos, BoxLayout.Y_AXIS));
        panelProductos.setBackground(COLOR_FONDO);

        JPanel panelInferior = new JPanel(new BorderLayout());
        panelInferior.setBackground(COLOR_FONDO);
        
        labelTotal = new JLabel("Total: "+CarritoCompras.getInstance().getTotal()+" €");
        labelTotal.setFont(new Font("Arial", Font.BOLD, 18));
        labelTotal.setForeground(COLOR_PRINCIPAL);
        labelTotal.setHorizontalAlignment(SwingConstants.CENTER);
        panelInferior.add(labelTotal, BorderLayout.CENTER);
        
        JPanel panelCupon = new JPanel();
        panelCupon.setLayout(new FlowLayout(FlowLayout.LEFT));
        panelCupon.setBackground(COLOR_FONDO);
        
        JLabel labelCupon = new JLabel("Código de descuento: ");
        campoCupon = new JTextField(10);
        campoCupon.setFont(new Font("Arial", Font.PLAIN, 14));
        
        campoCupon = new JTextField(10);
        campoCupon.setFont(new Font("Arial", Font.PLAIN, 14));
        campoCupon.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(COLOR_PRINCIPAL),
            BorderFactory.createEmptyBorder(5, 5, 5, 5)));
        
        JButton btnAplicarCupon = new JButton("Aplicar");
        btnAplicarCupon.addActionListener(e -> aplicarCupon());
        
        panelCupon.add(labelCupon);
        panelCupon.add(campoCupon);
        panelCupon.add(btnAplicarCupon);
        
        panelInferior.add(panelCupon, BorderLayout.NORTH);

        labelDescuento = new JLabel("Descuento: €0.00");
        labelDescuento.setFont(new Font("Arial", Font.BOLD, 14));
        labelDescuento.setForeground(COLOR_PRINCIPAL);
        panelInferior.add(labelDescuento, BorderLayout.SOUTH);

        JButton btnVaciar = new JButton("Vaciar Carrito");
        btnVaciar.addActionListener(e -> {
            carrito.vaciarCarrito();
            actualizarListaProductos();
            labelTotal.setText("Total: €0.00");
        });
        

        JButton btnComprar = createStyledButton("Realizar Compra");
        btnComprar.setBackground(COLOR_FONDO);
        btnComprar.setForeground(Color.WHITE);
        btnComprar.addActionListener(e -> {
            if (carrito.getProductos().isEmpty()) {
                JOptionPane.showMessageDialog(this, 
                    "El carrito está vacío", 
                    "Error", 
                    JOptionPane.WARNING_MESSAGE);
                return;
            }
            double totalConDescuento = carrito.getTotal() - descuentoAplicado;
            int confirm = JOptionPane.showConfirmDialog(this,
                "¿Desea finalizar la compra?\nTotal: €" + String.format("%.2f", totalConDescuento),
                "Confirmar Compra",
                JOptionPane.YES_NO_OPTION);
                
            if (confirm == JOptionPane.YES_OPTION) {
                JOptionPane.showMessageDialog(this,
                    "¡Compra realizada con éxito!",
                    "Éxito",
                    JOptionPane.INFORMATION_MESSAGE);
                actualizarListaProductos();
                Map<Producto,Integer> productosCarrito = CarritoCompras.getInstance().getProductos();
                for (Map.Entry<Producto, Integer> entry: productosCarrito.entrySet()) {

                	ServicioPersistenciaBD.getInstance().guardarVenta(ServicioPersistenciaBD.getInstance().getUsuario().getIdUsuario(),entry.getKey().getIdProducto(),entry.getValue(),""+LocalDate.now()+LocalTime.now());
                }
                carrito.vaciarCarrito();
                dispose();
                new VentanaSupermarket().setVisible(true);                
            }
        });
        
        JButton btnAtras = createStyledButton("Atrás");
        btnAtras.addActionListener(e -> {
            if (ventanaCategorias != null) {
                ventanaCategorias.setVisible(true);
                dispose();  
            }
        });
        
       

        JPanel panelBotones = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        panelBotones.setBackground(COLOR_FONDO);
        panelBotones.add(btnVaciar);
        panelBotones.add(btnComprar);
        panelBotones.add(btnAtras);
        panelInferior.add(panelBotones, BorderLayout.EAST);

        mainPanel.add(new JScrollPane(panelProductos), BorderLayout.CENTER);
        mainPanel.add(panelInferior, BorderLayout.SOUTH);

        add(mainPanel);
    }
    
    //claude
    private JButton createStyledButton(String text) {
        JButton button = new JButton(text);
        button.setFont(new Font("Arial", Font.BOLD, 12));
        button.setBackground(COLOR_SECUNDARIO);
        button.setForeground(Color.BLACK);
        button.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(COLOR_PRINCIPAL),
            BorderFactory.createEmptyBorder(8, 15, 8, 15)));
        button.setFocusPainted(false);
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        
        //efectos hover de claude
        button.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                button.setBackground(COLOR_PRINCIPAL);
                button.setForeground(Color.WHITE);
            }

            public void mouseExited(java.awt.event.MouseEvent evt) {
                button.setBackground(COLOR_SECUNDARIO);
                button.setForeground(Color.BLACK);
            }
        });
        
        return button;
    }
    
    //podríamos haber hecho lo mismo con mostrarError para que fueran mas bonitas las lineas de warning
    /*
    private void mostrarMensajeError(String mensaje) {
        JOptionPane.showMessageDialog(this, 
            mensaje, 
            "Error", 
            JOptionPane.WARNING_MESSAGE);
    }*/
    
    private void aplicarCupon() {
        String cuponIngresado = campoCupon.getText().trim();
        
        if (cuponIngresado.equals("ABC123")) {
            descuentoAplicado = carrito.getTotal() * 0.06;  
            labelDescuento.setText("Descuento: €" + String.format("%.2f", descuentoAplicado));
            recalcularDescuento();        } else {
            JOptionPane.showMessageDialog(this, 
                "Código de descuento inválido", 
                "Error", 
                JOptionPane.ERROR_MESSAGE);
        }
    }
    
    public void agregarProductoAlCarrito(Producto producto, int cantidad) {
       
        actualizarListaProductos();
        labelTotal.setText("Total: " + carrito.getTotalFormateado());
    }

    void actualizarListaProductos() {
    	panelProductos.removeAll();
        for (Map.Entry<Producto, Integer> entry : CarritoCompras.getInstance().getProductos().entrySet()) {
            Producto producto = entry.getKey();
            int cantidad = entry.getValue();
            
            JPanel itemPanel = new JPanel(new BorderLayout());
            itemPanel.setBackground(Color.WHITE);
            itemPanel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(COLOR_SECUNDARIO),
                BorderFactory.createEmptyBorder(10, 10, 10, 10)
            ));

            // Etiquetas de nombre y precio
            JLabel labelNombre = new JLabel(producto.getNombre().substring(0,1).toUpperCase()+producto.getNombre().substring(1,producto.getNombre().length()));
            labelNombre.setFont(new Font("Arial", Font.BOLD, 14));
            labelNombre.setForeground(COLOR_PRINCIPAL);
            
            JLabel labelPrecio = new JLabel(String.format("€%.2f x %d = €%.2f", 
                  producto.getPrecio(), cantidad, producto.getPrecio() * cantidad));
            labelPrecio.setFont(new Font("Arial", Font.PLAIN, 12));
            
            JLabel labelUnidades = new JLabel("Unidades: "+String.format("%d", cantidad));
            labelUnidades.setFont(new Font("Arial", Font.PLAIN, 12));
            
            // Obtener la ruta de la imagen
            String imagePath = "src/images/" + producto.getNombre().toLowerCase() + ".png";  // Ruta de la imagen
            JLabel labelFoto = new JLabel();
            
            // Intentar cargar la imagen
            try {
                ImageIcon imageIcon = new ImageIcon(imagePath);
                Image image = imageIcon.getImage();  // Obtener la imagen original
                Image scaledImage = image.getScaledInstance(50, 50, Image.SCALE_SMOOTH);  // Redimensionar la imagen
                labelFoto.setIcon(new ImageIcon(scaledImage));  // Asignar la imagen redimensionada
            } catch (Exception e) {
                // Si no se encuentra la imagen, poner una imagen predeterminada o un mensaje
                labelFoto.setText("Imagen no disponible");
            }

            // Botón de eliminar
            JButton btnEliminar = createStyledButton("Eliminar");
            btnEliminar.addActionListener(e -> {
                CarritoCompras.getInstance().removerProducto(producto);
                actualizarListaProductos();  // Volver a actualizar la lista
                recalcularDescuento();
            });

            // Añadir los componentes al panel del producto
            JPanel productPanel = new JPanel();
            productPanel.setLayout(new BoxLayout(productPanel, BoxLayout.Y_AXIS));// Layout vertical
            productPanel.setBackground(Color.WHITE);
            
            productPanel.add(labelFoto);  
            productPanel.add(Box.createVerticalStrut(5));
            
            productPanel.add(labelNombre);
            productPanel.add(Box.createVerticalStrut(3));
            
            productPanel.add(labelUnidades);
            productPanel.add(Box.createVerticalStrut(3));
            
            productPanel.add(labelPrecio); 
            
            itemPanel.add(productPanel, BorderLayout.CENTER);
            itemPanel.add(btnEliminar, BorderLayout.EAST);
           

            panelProductos.add(itemPanel);
            panelProductos.add(Box.createVerticalStrut(10));
            //panelProductos.add(Box.createVerticalStrut(200)); // Espacio de 20px entre botones

        }

        panelProductos.revalidate();
        panelProductos.repaint();
    }

    public void recalcularDescuento() {
        double totalSinDescuento = CarritoCompras.getInstance().getTotal();
        
        if (descuentoAplicado > 0) {
            descuentoAplicado = totalSinDescuento * 0.06; 
        }

        double totalConDescuento = totalSinDescuento - descuentoAplicado;
        
        labelTotal.setText("Total: €" + String.format("%.2f", totalConDescuento));
        labelDescuento.setText("Descuento: €" + String.format("%.2f", descuentoAplicado));
    }

	public CarritoCompras getCarrito() {
		return carrito;
	}

	public void setCarrito(CarritoCompras carrito) {
		this.carrito = carrito;
	}

	public static VentanaCarrito getInstance(VentanaCategorias ventanaCategorias) {
	    if (instance == null) {
	        instance = new VentanaCarrito(ventanaCategorias);
	    } else {
	        instance.ventanaCategorias = ventanaCategorias;
	    }
	    return instance;
	}

	public static VentanaCarrito getInstance() {
	    if (instance == null) {
	        instance = new VentanaCarrito();
	    }
	    return instance;
	}
	public static void setInstance(VentanaCarrito instance) {
		VentanaCarrito.instance = instance;
	}

	
}