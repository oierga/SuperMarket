package gui;

import javax.swing.*;
import db.ServicioPersistenciaBD;
import domain.Producto;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.lang.reflect.Array;

public class VentanaSupermarket extends JFrame {
    private int productosEnCarrito;
    private int[] idEnCarrito = new int[0];

    public VentanaSupermarket() {
        // Configuración de la ventana
        setTitle("Supermercado Online");
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null); // Centrar la ventana

        // Crear el menú
        JMenuBar menuBar = new JMenuBar();
        JMenu menuArchivo = new JMenu("Mi cuenta");
        JMenuItem itemSocio = new JMenuItem("Zona Socio");
        JMenu menuAyuda = new JMenu("Ayuda");
        JMenuItem itemSalir = new JMenuItem("Salir");
        JMenuItem itemCarrito = new JMenuItem("Ver Carrito");
        
        itemSocio.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String codigoOferta = "ABC123"; // Código de oferta de ejemplo
                VentanaSocio ventanaSocio = new VentanaSocio(codigoOferta);
                ventanaSocio.setVisible(true);
            }
        });
        
        itemSalir.addActionListener(e -> System.exit(0)); // Cerrar la aplicación

        menuArchivo.add(itemSalir);
        menuBar.add(menuArchivo);
        menuBar.add(menuAyuda);
        menuBar.add(itemSocio);
        menuBar.add(itemCarrito);

        setJMenuBar(menuBar);

        // Crear el panel de productos
        JPanel panelProductos = new JPanel();
        panelProductos.setLayout(new GridLayout(0, 3)); // 3 columnas

        // Cargar productos desde la base de datos
       
        // Mostrar los productos en el panel como botones
        for (int i = 0; i < 9; i++) {
            //final int idProducto = producto.getIdProducto(); // ID único para cada producto

            // Crear el botón para el producto
            JButton btnProducto = new JButton("<html><center> + /*producto.getNombre()*/ + <br>Precio: $ </center></html>");
            btnProducto.setPreferredSize(new Dimension(120, 100));
            btnProducto.setVerticalAlignment(SwingConstants.CENTER);
            btnProducto.setHorizontalAlignment(SwingConstants.CENTER);
            btnProducto.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

            // Acción para abrir detalles del producto
            btnProducto.addActionListener(e -> {
                // Abre la ventana de detalles del producto con el ID correspondiente
                VentanaProducto ventanaProducto = new VentanaProducto(null, idEnCarrito);
                idEnCarrito = ventanaProducto.getIdEnCarrito();
                dispose(); // Cierra la ventana de supermercado actual
            });

            // Añadir el botón al panel de productos
            panelProductos.add(btnProducto);
        }
        itemCarrito.addActionListener(e -> {
        	int[] idPrueba = new int[1];
            VentanaCarrito ventanaCarrito = new VentanaCarrito(idPrueba);
            ventanaCarrito.setVisible(true);
        });

        // Añadir el panel de productos a la ventana
        add(panelProductos, BorderLayout.CENTER);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            VentanaSupermarket ventana = new VentanaSupermarket();
            ventana.setVisible(true); // Hacer visible la ventana
        });
    }

    public int[] getIdEnCarrito() {
        return idEnCarrito;
    }

    public void setIdEnCarrito(int[] idEnCarrito) {
        this.idEnCarrito = idEnCarrito;
    }
}
