package gui;

import domain.Venta;
import domain.Usuario;
import db.ServicioPersistenciaBD;

import javax.swing.*;
import javax.swing.border.LineBorder;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class VentanaHistorial extends JFrame {

    private static final Color COLOR_PRINCIPAL = new Color(34, 139, 34); // Verde oscuro
    private static final Color COLOR_SECUNDARIO = new Color(144, 238, 144); // Verde claro
    private static final Color COLOR_FONDO = new Color(240, 255, 240); // Fondo blanco verdoso
    private double total;
    private Usuario usuario;

    public VentanaHistorial(Usuario usuario) {
        this.usuario = usuario;
        setTitle("Historial de Compras");
        setSize(600, 400);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);

        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        mainPanel.setBackground(COLOR_FONDO);

        // Sección superior: Título
        JLabel titulo = new JLabel("Historial de Compras", SwingConstants.CENTER);
        titulo.setFont(new Font("Arial", Font.BOLD, 20));
        titulo.setForeground(COLOR_PRINCIPAL);
        mainPanel.add(titulo, BorderLayout.NORTH);

        // Sección central: Tabla de compras
        JPanel panelCentral = new JPanel();
        panelCentral.setLayout(new BorderLayout());
        panelCentral.setBackground(COLOR_FONDO);

        // Obtener las ventas del usuario desde la base de datos
        List<Venta> ventas = ServicioPersistenciaBD.getInstance().cargarVentas();
        List<Venta> ventasDeUsuario = new ArrayList<Venta>();
        int id = ServicioPersistenciaBD.getInstance().getUsuario().getIdUsuario();
        for (Venta venta: ventas) {
        	if (venta.getIdUsuario()==id) {
        		ventasDeUsuario.add(venta);
        	}
        }

        // Crear tabla de ventas
        String[] columnNames = {"ID Venta", "Producto", "Cantidad", "Fecha","Importe"};
        Object[][] data = new Object[ventas.size()][5];

        for (int i = 0; i < ventasDeUsuario.size(); i++) {
            Venta venta = ventasDeUsuario.get(i);
            data[i][0] = venta.getIdUsuario();
            data[i][1] = ServicioPersistenciaBD.getInstance().obtenerProductoPorId(venta.getIdProducto()).getNombre().substring(0,1).toUpperCase()
            		+ServicioPersistenciaBD.getInstance().obtenerProductoPorId(venta.getIdProducto()).getNombre().substring(1);
            data[i][2] = venta.getCantidad();
            data[i][3] = venta.getFecha();
            data[i][4] = ServicioPersistenciaBD.getInstance().obtenerProductoPorId(venta.getIdProducto()).getPrecio()+" €";
            total += ServicioPersistenciaBD.getInstance().obtenerProductoPorId(venta.getIdProducto()).getPrecio();

        }

        JTable tablaVentas = new JTable(data, columnNames);
        tablaVentas.setFont(new Font("Arial", Font.PLAIN, 14));
        tablaVentas.setBorder(new LineBorder(COLOR_PRINCIPAL));
        JScrollPane scrollPane = new JScrollPane(tablaVentas);
        panelCentral.add(scrollPane, BorderLayout.CENTER);

        mainPanel.add(panelCentral, BorderLayout.CENTER);

        // Sección inferior: Botones
        JPanel panelBotones = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        panelBotones.setBackground(COLOR_FONDO);
        JLabel labelTotal = new JLabel("Total: "+total+" €"); 
        JButton btnCerrar = createStyledButton("Cerrar");
        btnCerrar.addActionListener(e -> dispose());
        panelBotones.add(labelTotal);
        panelBotones.add(btnCerrar);

        mainPanel.add(panelBotones, BorderLayout.SOUTH);

        add(mainPanel);
    }

    private JButton createStyledButton(String text) {
        JButton button = new JButton(text);
        button.setFont(new Font("Arial", Font.BOLD, 12));

        if (text.contains("Cerrar")) {
            button.setBackground(COLOR_PRINCIPAL);
            button.setForeground(Color.WHITE);
            button.setOpaque(true);
        } else {
            button.setBackground(COLOR_SECUNDARIO);
            button.setForeground(Color.BLACK);
        }
        button.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(COLOR_PRINCIPAL),
                BorderFactory.createEmptyBorder(8, 15, 8, 15)));
        button.setFocusPainted(false);
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));

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
}
