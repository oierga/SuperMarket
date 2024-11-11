package gui;
import javax.swing.*;
import javax.swing.border.CompoundBorder;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;

import domain.TipoUsuario;
import domain.Usuario;

import java.awt.*;

public class VentanaBienvenida extends JFrame {
    private static final long serialVersionUID = 1L;
    
    public VentanaBienvenida() {
        setTitle("Bienvenida a SuperMarket");
        setSize(600, 400);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        // Crear el panel principal con un diseño de BorderLayout
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(new Color(76, 175, 80)); // Color de fondo personalizado
        panel.setBorder(new CompoundBorder(
            new LineBorder(new Color(76, 175, 80).darker(), 2),
            new EmptyBorder(30, 40, 30, 40)
        ));

        // Crear el panel de saludo con FlowLayout
        JPanel saludoPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        saludoPanel.setBackground(new Color(76, 175, 80)); // Color de fondo del saludo
        JLabel lblSaludo = new JLabel("¡Bienvenido/a a SuperMarket!");
        lblSaludo.setFont(new Font("Segoe UI", Font.BOLD, 28));
        lblSaludo.setForeground(Color.WHITE); // Color de texto blanco
        saludoPanel.add(lblSaludo);

        // Crear el área de texto para el mensaje de bienvenida
        JTextArea mensajeBienvenida = new JTextArea(
            "Tu supermercado de confianza ahora más cerca que nunca. " +
            "Disfruta de la mejor experiencia de compra online con productos frescos y de calidad."
        );
        mensajeBienvenida.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        mensajeBienvenida.setForeground(Color.WHITE);
        mensajeBienvenida.setBackground(new Color(76, 175, 80));
        mensajeBienvenida.setWrapStyleWord(true);
        mensajeBienvenida.setLineWrap(true);
        mensajeBienvenida.setEditable(false);
        mensajeBienvenida.setBorder(new EmptyBorder(10, 0, 0, 0));

        // Crear el botón para cerrar la ventana o continuar
        JButton btnComenzar = new JButton("Comenzar");
        btnComenzar.setFont(new Font("Segoe UI", Font.BOLD, 14));
        btnComenzar.setBackground(new Color(0, 102, 204)); // Color de fondo del botón
        btnComenzar.setForeground(Color.WHITE); // Color del texto del botón
        btnComenzar.setFocusPainted(false);
        btnComenzar.setBorderPainted(false);

        // Acción para cerrar la ventana de bienvenida y abrir la VentanaSupermarket
        btnComenzar.addActionListener(e -> {
            this.dispose(); // Cerrar ventana de bienvenida
            Usuario usuario = new Usuario("admin", "123", true, TipoUsuario.ADMIN); // Crear usuario
            new VentanaSupermarket(usuario).setVisible(true); // Abrir la VentanaSupermarket
        });

        // Añadir los componentes al panel principal
        panel.add(saludoPanel, BorderLayout.NORTH);
        panel.add(mensajeBienvenida, BorderLayout.CENTER);
        panel.add(btnComenzar, BorderLayout.SOUTH); // Botón en la parte inferior

        // Añadir el panel principal al contenido de la ventana
        setContentPane(panel);
        setVisible(true);
    }
    public static void main(String[] args) {
    	new VentanaBienvenida();
    }
}