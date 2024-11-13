package gui;

import db.ServicioPersistenciaBD;
import domain.CarritoCompras;
import domain.Producto;
import domain.Usuario;

import javax.swing.*;
import java.awt.*;

import java.util.Map;

public class VentanaCarrito extends JFrame {
	
	private CarritoCompras carrito;
    private JPanel panelProductos;
    private JLabel labelTotal;
    private JTextField campoCupon;
    private JLabel labelDescuento;
    private double descuentoAplicado = 0;
    private Usuario usuario; 
    private  VentanaCategorias ventanaCategorias;
    
    public VentanaCarrito(VentanaCategorias ventanaCategorias) {
        this.ventanaCategorias = ventanaCategorias;
    	this.usuario = usuario;
    	this.carrito = CarritoCompras.getInstance();
    	
        setTitle("Carrito de Compras");
        setSize(800, 500);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null); 

        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BorderLayout());
        mainPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        panelProductos = new JPanel();
        panelProductos.setLayout(new BoxLayout(panelProductos, BoxLayout.Y_AXIS));
        actualizarListaProductos();

        JPanel panelInferior = new JPanel(new BorderLayout());
        
        labelTotal = new JLabel("Total: €0.00");
        labelTotal.setFont(new Font("Arial", Font.BOLD, 16));
        labelTotal.setHorizontalAlignment(SwingConstants.CENTER);
        panelInferior.add(labelTotal, BorderLayout.CENTER);
        
        JPanel panelCupon = new JPanel();
        panelCupon.setLayout(new FlowLayout(FlowLayout.LEFT));
        JLabel labelCupon = new JLabel("Código de descuento: ");
        campoCupon = new JTextField(10);
        campoCupon.setFont(new Font("Arial", Font.PLAIN, 14));
        
        JButton btnAplicarCupon = new JButton("Aplicar");
        btnAplicarCupon.addActionListener(e -> aplicarCupon());
        
        panelCupon.add(labelCupon);
        panelCupon.add(campoCupon);
        panelCupon.add(btnAplicarCupon);
        
        panelInferior.add(panelCupon, BorderLayout.NORTH);
        
        labelDescuento = new JLabel("Descuento: €0.00");
        labelDescuento.setFont(new Font("Arial", Font.PLAIN, 14));
        panelInferior.add(labelDescuento, BorderLayout.SOUTH);

        JButton btnVaciar = new JButton("Vaciar Carrito");
        btnVaciar.addActionListener(e -> {
            carrito.vaciarCarrito();
            actualizarListaProductos();
            labelTotal.setText("Total: €0.00");
        });

        JButton btnComprar = new JButton("Realizar Compra");
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
                carrito.vaciarCarrito();
                actualizarListaProductos();
                VentanaSupermarket ventanaSupermarket = new VentanaSupermarket(usuario);
                ventanaSupermarket.setVisible(true);
;
                dispose();
            }
        });
        
        JButton btnAtras = new JButton("Atrás");
        btnAtras.addActionListener(e -> {
            if (ventanaCategorias != null) {
                ventanaCategorias.setVisible(true);
                dispose();  
            }
        });

        JPanel panelBotones = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        panelBotones.add(btnVaciar);
        panelBotones.add(btnComprar);
        panelBotones.add(btnAtras, FlowLayout.LEFT);
        panelInferior.add(panelBotones, BorderLayout.EAST);

        mainPanel.add(new JScrollPane(panelProductos), BorderLayout.CENTER);
        mainPanel.add(panelInferior, BorderLayout.SOUTH);

        add(mainPanel);
    }
    
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
        carrito.agregarProducto(producto, cantidad);
        actualizarListaProductos();
        labelTotal.setText("Total: " + carrito.getTotalFormateado());
    }

    private void actualizarListaProductos() {
        panelProductos.removeAll();
        
        for (Map.Entry<Producto, Integer> entry : carrito.getProductos().entrySet()) {
            Producto producto = entry.getKey();
            int cantidad = entry.getValue();
            
            JPanel itemPanel = new JPanel(new BorderLayout());
            itemPanel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createMatteBorder(0, 0, 1, 0, Color.LIGHT_GRAY),
                BorderFactory.createEmptyBorder(5, 5, 5, 5)
            ));

            JLabel labelNombre = new JLabel(producto.getNombre());
            JLabel labelPrecio = new JLabel(String.format("€%.2f x %d = €%.2f", 
                    producto.getPrecio(), cantidad, producto.getPrecio() * cantidad));

            JButton btnEliminar = new JButton("Eliminar");
            btnEliminar.addActionListener(e -> {
                carrito.removerProducto(producto);
                actualizarListaProductos();
                recalcularDescuento();
            });

            itemPanel.add(labelNombre, BorderLayout.WEST);
            itemPanel.add(labelPrecio, BorderLayout.CENTER);
            itemPanel.add(btnEliminar, BorderLayout.EAST);

            panelProductos.add(itemPanel);
        }

        panelProductos.revalidate();
        panelProductos.repaint();
        
    }
    private void recalcularDescuento() {
        double totalSinDescuento = carrito.getTotal();
        
        if (descuentoAplicado > 0) {
            descuentoAplicado = totalSinDescuento * 0.06; 
        }

        double totalConDescuento = totalSinDescuento - descuentoAplicado;
        
        labelTotal.setText("Total: €" + String.format("%.2f", totalConDescuento));
        labelDescuento.setText("Descuento: €" + String.format("%.2f", descuentoAplicado));
    }
}
