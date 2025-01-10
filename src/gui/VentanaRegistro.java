package gui;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.border.*;

import db.ServicioPersistenciaBD;
import domain.Usuario;

public class VentanaRegistro extends JFrame {
    private JFrame fCrearUsuario;
    private JLabel lNombreDeUsuario;
    private JTextField tfNombreDeUsuario;
    private JLabel lContraseña;
    private JPasswordField tfContraseña;
    private JButton bCrearCuenta;

    private Color colorPrimario = new Color(76, 175, 80);
    private Color colorSecundario = new Color(245, 245, 245);
    private Color colorAccent = new Color(33, 33, 33);
    private Color colorTexto = new Color(44, 62, 80); // Gris oscuro

    public VentanaRegistro(ServicioPersistenciaBD servicioPersistencia) {
        // Configuración de la ventana principal
        fCrearUsuario = new JFrame("Crear Usuario");
        fCrearUsuario.setSize(500, 300);
        fCrearUsuario.setLocationRelativeTo(null);
        fCrearUsuario.setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
        fCrearUsuario.getContentPane().setBackground(colorSecundario);
        fCrearUsuario.getContentPane().setLayout(new BorderLayout(10, 10));

        // Panel superior: Mensaje de bienvenida
        JPanel panelSuperior = new JPanel(new BorderLayout());
        panelSuperior.setBackground(colorPrimario);
        panelSuperior.setBorder(new EmptyBorder(10, 10, 10, 10));

        JLabel titulo = new JLabel("Regístrate", SwingConstants.CENTER);
        titulo.setFont(new Font("Segoe UI", Font.BOLD, 20));
        titulo.setForeground(Color.WHITE);
        panelSuperior.add(titulo, BorderLayout.CENTER);

        fCrearUsuario.getContentPane().add(panelSuperior, BorderLayout.NORTH);

        // Panel central: Formulario
        JPanel panelFormulario = new JPanel(new GridBagLayout());
        panelFormulario.setBackground(colorSecundario);
        panelFormulario.setBorder(new EmptyBorder(20, 20, 20, 20));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(10, 10, 10, 10);

        // Nombre de usuario
        lNombreDeUsuario = new JLabel("Nombre de usuario:");
        lNombreDeUsuario.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        lNombreDeUsuario.setForeground(colorTexto);
        tfNombreDeUsuario = new JTextField(20);
        tfNombreDeUsuario.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        tfNombreDeUsuario.setBorder(new LineBorder(colorPrimario, 2));

        gbc.gridx = 0; gbc.gridy = 0;
        panelFormulario.add(lNombreDeUsuario, gbc);
        gbc.gridx = 1; gbc.gridy = 0;
        panelFormulario.add(tfNombreDeUsuario, gbc);

        // Contraseña
        lContraseña = new JLabel("Contraseña:");
        lContraseña.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        lContraseña.setForeground(colorTexto);
        tfContraseña = new JPasswordField(20);
        tfContraseña.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        tfContraseña.setBorder(new LineBorder(colorPrimario, 2));

        gbc.gridx = 0; gbc.gridy = 1;
        panelFormulario.add(lContraseña, gbc);
        gbc.gridx = 1; gbc.gridy = 1;
        panelFormulario.add(tfContraseña, gbc);

        // Botón de registro
        bCrearCuenta = new JButton("Crear Cuenta");
        bCrearCuenta.setFont(new Font("Segoe UI", Font.BOLD, 14));
        bCrearCuenta.setForeground(Color.WHITE);
        bCrearCuenta.setBackground(colorPrimario);
        bCrearCuenta.setOpaque(true);
        bCrearCuenta.setPreferredSize(new Dimension(200, 50)); // Ancho: 200, Alto: 50

        bCrearCuenta.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        bCrearCuenta.setBorder(new LineBorder(colorTexto, 1));
        bCrearCuenta.setFocusPainted(false);

        bCrearCuenta.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                bCrearCuenta.setBackground(colorPrimario.darker());
            }

            @Override
            public void mouseExited(MouseEvent e) {
                bCrearCuenta.setBackground(colorPrimario);
            }
        });

        gbc.gridx = 0; gbc.gridy = 2; gbc.gridwidth = 2;
        panelFormulario.add(bCrearCuenta, gbc);

        fCrearUsuario.getContentPane().add(panelFormulario, BorderLayout.CENTER);

        // Acción del botón
        bCrearCuenta.addActionListener(e -> {
            String nombreDeUsuario = tfNombreDeUsuario.getText();
            String contraseña = new String(tfContraseña.getPassword());

            if (!nombreDeUsuario.isEmpty() && !contraseña.isEmpty()) {
                Usuario nuevoUsuario = new Usuario(nombreDeUsuario, contraseña, true);
                servicioPersistencia.guardarUsuario(nuevoUsuario);
                JOptionPane.showMessageDialog(
                    fCrearUsuario,
                    "¡Cuenta creada exitosamente!",
                    "Éxito",
                    JOptionPane.INFORMATION_MESSAGE
                );
                fCrearUsuario.dispose();
            } else {
                JOptionPane.showMessageDialog(
                    fCrearUsuario,
                    "Por favor, completa todos los campos.",
                    "Error",
                    JOptionPane.ERROR_MESSAGE
                );
            }
        });

        fCrearUsuario.setVisible(true);
    }
}
