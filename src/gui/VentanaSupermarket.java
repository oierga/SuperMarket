package gui;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class VentanaSupermarket extends JFrame {
	private int productosEnCarrito;
	private int i;
    public VentanaSupermarket() {
    	
        // Configuración de la ventana
        setTitle("Supermercado Online");
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null); // Centrar la ventana

        // Crear el menú
        JMenuBar menuBar = new JMenuBar();
        JMenu menuArchivo = new JMenu("Mi cuenta");
        JMenu menuSocio = new JMenu("Zona Socio");
        JMenu menuAyuda = new JMenu("Ayuda");
        JMenuItem itemSalir = new JMenuItem("Salir");
        JMenuItem itemCarrito = new JMenuItem("Ver Carrito");

        itemSalir.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.exit(0); // Cerrar la aplicación
            }
        });

        menuArchivo.add(itemSalir);
        menuBar.add(menuArchivo);
        menuBar.add(menuAyuda);
        menuBar.add(menuSocio);
        menuBar.add(itemCarrito);
       
        setJMenuBar(menuBar);

        // Crear el panel de productos
        JPanel panelProductos = new JPanel();
        panelProductos.setLayout(new GridLayout(0, 3)); // 3 columnas

        // Ejemplo de productos (puedes reemplazarlos con tus productos reales)
        for (int i = 1; i <= 9; i++) {
            // Crear un panel para el producto
            JPanel productoPanel = new JPanel();
            productoPanel.setLayout(new BorderLayout());

            // Crear un botón para mostrar la imagen
            JButton btnProducto = new JButton();
            // Cargar y redimensionar la imagen
            ImageIcon icono = new ImageIcon("ruta/a/la/imagen"); // Asegúrate de tener imágenes
            Image imagenRedimensionada = icono.getImage().getScaledInstance(100, 100, Image.SCALE_SMOOTH);
            btnProducto.setIcon(new ImageIcon(imagenRedimensionada));
            btnProducto.setPreferredSize(new Dimension(100, 100));
           
            // Añadir acción al botón
            btnProducto.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    // Mostrar mensaje de agregar al carrito
                	new VentanaProducto();
                   
                }
            });

            // Añadir el botón al panel del producto
            productoPanel.add(btnProducto, BorderLayout.CENTER);
           
            // Crear un panel para el texto (nombre y precio)
            JPanel panelTexto = new JPanel();
            panelTexto.setLayout(new BoxLayout(panelTexto, BoxLayout.Y_AXIS));
            JLabel labelNombre = new JLabel("Producto ", SwingConstants.CENTER);
            JLabel labelPrecio = new JLabel("Precio €" , SwingConstants.CENTER);
            panelTexto.add(labelNombre);
            panelTexto.add(labelPrecio);

            // Añadir el panel de texto debajo del botón
            productoPanel.add(panelTexto, BorderLayout.SOUTH);

            // Añadir el panel del producto al panel de productos
            panelProductos.add(productoPanel);
        }

        // Añadir el panel de productos a la ventana
        add(panelProductos, BorderLayout.CENTER);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                VentanaSupermarket ventana = new VentanaSupermarket();
                ventana.setVisible(true); // Hacer visible la ventana
            }
        });
    }
}
