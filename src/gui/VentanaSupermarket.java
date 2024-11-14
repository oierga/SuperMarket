package gui;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.HashMap;

import javax.swing.border.CompoundBorder;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;

import domain.Producto;
import domain.TipoUsuario;
import domain.Usuario;

public class VentanaSupermarket extends JFrame {
    private static final long serialVersionUID = 1L;
    private Usuario usuario;
    private static TipoUsuario tipoUsuario;
    private double totalCarrito;
    private VentanaCarrito ventanaCarrito;
    private VentanaCategorias ventanaCategorias;
    private Color colorPrimario = new Color(76, 175, 80);
    private Color colorSecundario = new Color(245, 245, 245);
    private Color colorAccent = new Color(33, 33, 33);

    public VentanaSupermarket(Usuario usuario) {
        this.usuario = usuario;
        tipoUsuario = usuario.getTipo();
        
        configurarVentana();
        crearMenu();
        
        JPanel mainPanel = new JPanel(new BorderLayout(0, 20));
        mainPanel.setBackground(colorSecundario);
        mainPanel.setBorder(new EmptyBorder(20, 20, 20, 20));
        
        mainPanel.add(crearPanelBienvenida(), BorderLayout.NORTH);
        mainPanel.add(crearPanelCentral(), BorderLayout.CENTER);
        mainPanel.add(crearPanelContacto(), BorderLayout.SOUTH);
       // mainPanel.add(crearPanelProductos());
        
        add(new JScrollPane(mainPanel));
        setVisible(true);
    }
    
    private void configurarVentana() {
        setTitle("Supermercado Online");
        setSize(1000, 800);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setMinimumSize(new Dimension(800, 600));
    }
    
    private JPanel crearPanelBienvenida() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(colorPrimario);
        panel.setBorder(new CompoundBorder(
            new LineBorder(colorPrimario.darker(), 2),
            new EmptyBorder(30, 40, 30, 40)
        ));
        
        JPanel saludoPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        saludoPanel.setBackground(colorPrimario);
        JLabel lblSaludo = new JLabel("¡Bienvenido/a a SuperMarket, " + usuario.getNombreDeUsuario() + "!");
        lblSaludo.setFont(new Font("Segoe UI", Font.BOLD, 28));
        lblSaludo.setForeground(Color.WHITE);
        saludoPanel.add(lblSaludo);
        
        JTextArea mensajeBienvenida = new JTextArea(
            "Tu supermercado de confianza ahora más cerca que nunca. " +
            "Disfruta de la mejor experiencia de compra online con productos frescos y de calidad."
        );
        mensajeBienvenida.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        mensajeBienvenida.setForeground(Color.WHITE);
        mensajeBienvenida.setBackground(colorPrimario);
        mensajeBienvenida.setWrapStyleWord(true);
        mensajeBienvenida.setLineWrap(true);
        mensajeBienvenida.setEditable(false);
        mensajeBienvenida.setBorder(new EmptyBorder(10, 0, 0, 0));
        
        panel.add(saludoPanel, BorderLayout.NORTH);
        panel.add(mensajeBienvenida, BorderLayout.CENTER);
        
        return panel;
    }
   
    private JPanel crearPanelCentral() {
        JPanel panel = new JPanel(new BorderLayout(0, 20));
        panel.setBackground(colorSecundario);
        
        JPanel caracteristicasPanel = new JPanel(new GridLayout(1, 3, 20, 0));
        caracteristicasPanel.setBackground(colorSecundario);
        
        caracteristicasPanel.add(crearPanelCaracteristica("Envío Gratis", "En pedidos superiores a 50€", "🚚"));
        caracteristicasPanel.add(crearPanelCaracteristica("Productos Frescos", "Calidad garantizada", "🥬"));
        caracteristicasPanel.add(crearPanelCaracteristica("Atención 24/7", "Estamos para ayudarte", "💬"));

        JPanel botonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        botonPanel.setBackground(colorSecundario);
        JButton btnComprar = crearBotonComprar();
        botonPanel.add(btnComprar);
        
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
    
    private JButton crearBotonComprar() {
        JButton btnComprar = new JButton("COMENZAR A COMPRAR");
        btnComprar.setFont(new Font("Segoe UI", Font.BOLD, 16));
        btnComprar.setForeground(Color.WHITE);
        btnComprar.setBackground(colorPrimario);
        btnComprar.setOpaque(true);
        btnComprar.setPreferredSize(new Dimension(300, 60));
        btnComprar.setBorder(new LineBorder(colorPrimario.darker(), 2));
        btnComprar.setCursor(new Cursor(Cursor.HAND_CURSOR));
        
        btnComprar.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                btnComprar.setBackground(colorPrimario.darker());
            }
            
            @Override
            public void mouseExited(MouseEvent e) {
                btnComprar.setBackground(colorPrimario);
            }
        });
        
        btnComprar.addActionListener(e -> abrirVentanaCategorias());
        
        return btnComprar;
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
        
        JPanel infoPanel = new JPanel(new GridLayout(contactInfo.length, 1, 0, 10));
        infoPanel.setBackground(Color.WHITE);
        
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
            
            infoPanel.add(linePanel);
        }
        
        panel.add(lblContacto);
        panel.add(Box.createRigidArea(new Dimension(0, 15)));
        panel.add(infoPanel);
        
        return panel;
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
    
    private void abrirVentanaCarrito() {
        if (ventanaCarrito == null) {
            ventanaCarrito = new VentanaCarrito(ventanaCategorias,usuario);
        }
        ventanaCarrito.setVisible(true);
    }
    
    private void hacerSocio() {
        usuario.setTipo(TipoUsuario.SOCIO);
        JOptionPane.showMessageDialog(
            this,
            "¡Felicidades! Ahora eres socio y podrás disfrutar de beneficios exclusivos.",
            "¡Bienvenido Socio!",
            JOptionPane.INFORMATION_MESSAGE
        );
        String codigoOferta = "ABC123";
        new VentanaSocio(codigoOferta).setVisible(true);
    }
    
    private void abrirVentanaSocio() {
        new VentanaSocio("ABC123").setVisible(true);
    }
    
    private void abrirVentanaAdmin() {
        new VentanaAdmin(usuario).setVisible(true);
    }
    
    private void abrirVentanaCategorias() {
        if (ventanaCategorias == null) {
            ventanaCategorias = new VentanaCategorias(usuario);
        }
        ventanaCategorias.setVisible(true);
    }
    
    public void actualizarTotalCarrito(double nuevoTotal) {
        totalCarrito = nuevoTotal;
        if (ventanaCategorias != null) {
            ventanaCategorias.actualizarTotalCarrito(nuevoTotal);
        }
    }
    //Este main es para probar la ventana mientras trabajo con ella
    public static void main(String[] args) {
    	Usuario usuario = new Usuario("admin", "123", true, TipoUsuario.USUARIO);
    	new VentanaSupermarket(usuario);
    }
}

