package gui;

import db.ServicioPersistenciaBD;
import domain.CarritoCompras;
import domain.Producto;
import domain.Usuario;

import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class VentanaCarrito extends JFrame {

    private CarritoCompras carrito;
    private JPanel panelProductos;
    private JLabel labelTotal;
    private JTextField campoCupon;
    private JLabel labelDescuento;
    private double descuentoAplicado = 0;
    private Usuario usuario;
    private VentanaCategorias ventanaCategorias;

    // Constructor actualizado
    public VentanaCarrito(VentanaCategorias ventanaCategorias, ArrayList<Producto> productosCarrito2, Usuario usuario, HashMap<Producto,Integer> productosCarritoUnidad) {
    	
    	actualizarListaProductos(productosCarritoUnidad, productosCarrito2);
    	calcularTotal(productosCarritoUnidad,productosCarrito2);
        this.ventanaCategorias = ventanaCategorias;
        this.usuario = usuario;
        this.carrito = CarritoCompras.getInstance();

        // Añadir los productos al carrito
        if (!(productosCarrito2.size()==0))
        for (Producto productoData : productosCarrito2) {
        	System.out.print(productoData.getNombre());
            String nombre = productoData.getNombre();
            double precio = productoData.getPrecio();
            Producto producto = new Producto(nombre, precio);
            carrito.agregarProducto(producto, productosCarritoUnidad.get(productoData));  // Suponiendo que por defecto agregamos 1 unidad de cada producto
        }

        // Configuración de la ventana
        setTitle("Carrito de Compras");
        setSize(800, 500);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);

        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BorderLayout());
        mainPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        panelProductos = new JPanel();
        panelProductos.setLayout(new BoxLayout(panelProductos, BoxLayout.Y_AXIS));
        actualizarListaProductos(productosCarritoUnidad,productosCarrito2);

        JPanel panelInferior = new JPanel(new BorderLayout());

        labelTotal = new JLabel("Total: "+calcularTotal(productosCarritoUnidad,productosCarrito2));
        labelTotal.setFont(new Font("Arial", Font.BOLD, 16));
        labelTotal.setHorizontalAlignment(SwingConstants.CENTER);
        panelInferior.add(labelTotal, BorderLayout.CENTER);
        repaint();
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
            actualizarListaProductos(productosCarritoUnidad, productosCarrito2);
            productosCarrito2.removeAll(productosCarrito2);
            repaint();
            labelTotal.setText("Total: 00");
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
                
                actualizarListaProductos( productosCarritoUnidad, productosCarrito2);
                VentanaSupermarket ventanaSupermarket = new VentanaSupermarket(usuario);
                ventanaSupermarket.setVisible(true);
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
            recalcularDescuento();
        } else {
            JOptionPane.showMessageDialog(this,
                    "Código de descuento inválido",
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
        }
    }

    public void agregarProductoAlCarrito(Producto producto, int cantidad,HashMap<Producto,Integer> productosCarritoUnidad, ArrayList<Producto> productosCarrito2) {
        carrito.agregarProducto(producto, cantidad);
        actualizarListaProductos(productosCarritoUnidad,productosCarrito2);
        labelTotal.setText("Total: " +calcularTotal(productosCarritoUnidad,productosCarrito2));
    }

    void actualizarListaProductos(HashMap<Producto, Integer> productosCarritoUnidad, ArrayList<Producto> productosCarrito2) {
        if (panelProductos != null) {
            panelProductos.removeAll();

            for (Map.Entry<Producto, Integer> entry : carrito.getProductos().entrySet()) {
                Producto producto = entry.getKey();
                int cantidad = entry.getValue();

                JPanel itemPanel = new JPanel(new BorderLayout());
                itemPanel.setBorder(BorderFactory.createEmptyBorder(3, 3, 3, 3));  // Reducir el espacio entre filas

                // Cargar la imagen del producto con tamaño ajustado
                String pathImagen = "src/images/" + producto.getNombre().replace(" ", "") + ".png";
                ImageIcon imagenProducto = new ImageIcon(pathImagen);

                // Escalar la imagen para que tenga una altura fija
                Image imagenEscalada = imagenProducto.getImage().getScaledInstance(50, 50, Image.SCALE_SMOOTH);
                JLabel labelImagen = new JLabel(new ImageIcon(imagenEscalada));

                // Crear los otros componentes (nombre y precio del producto)
                JLabel labelNombre = new JLabel(producto.getNombre());
                labelNombre.setFont(new Font("Arial", Font.PLAIN, 12)); // Fuente más pequeña para filas finas

                JLabel labelPrecio = new JLabel(String.format("%.2f€ x %d = %.2f€",
                        producto.getPrecio(), cantidad, producto.getPrecio() * cantidad));
                labelPrecio.setFont(new Font("Arial", Font.PLAIN, 12));  // Fuente más pequeña para precio

                // Crear el botón de eliminar
                JButton btnEliminar = new JButton("Eliminar");
                btnEliminar.setFont(new Font("Arial", Font.PLAIN, 10)); // Botón pequeño
                btnEliminar.addActionListener(e -> {
                    carrito.removerProducto(producto);
                    productosCarrito2.remove(producto);
                    actualizarListaProductos(productosCarritoUnidad, productosCarrito2);
                    recalcularDescuento();
                    recalcularTotal(productosCarritoUnidad, productosCarrito2);
                    repaint();
                });

                // Añadir los componentes al panel de producto
                itemPanel.add(labelImagen, BorderLayout.WEST);
                itemPanel.add(labelNombre, BorderLayout.CENTER);
                itemPanel.add(labelPrecio, BorderLayout.EAST);
                itemPanel.add(btnEliminar, BorderLayout.SOUTH);  // Colocar el botón abajo para ocupar menos espacio

                panelProductos.add(itemPanel);
            }

            panelProductos.revalidate();
            panelProductos.repaint();
        }
    }



    protected void recalcularTotal(HashMap<Producto,Integer> productosCarritoUnidad, ArrayList<Producto> productosCarrito2) {
    	double nuevoTotal = calcularTotal(productosCarritoUnidad,productosCarrito2);
    	labelTotal.setText("Total: "+ nuevoTotal);
    }
    private double calcularTotal(HashMap<Producto,Integer> productosCarritoUnidad,ArrayList<Producto> productosCarrito2) {
    	Double total=0.0;
    	for (Producto producto: productosCarrito2) {
    		int unidades = productosCarritoUnidad.get(producto);
    		total += producto.getPrecio()*unidades;
    		System.out.print(producto.getPrecio());
    	}
    	System.out.print(total);
    	return total;
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

	public CarritoCompras getCarrito() {
		return carrito;
	}

	public void setCarrito(CarritoCompras carrito) {
		this.carrito = carrito;
	}
}
