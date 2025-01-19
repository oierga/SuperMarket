package gui;

import domain.TipoUsuario;
import domain.Usuario;
import db.ServicioPersistenciaBD;
import javax.swing.*;
import javax.swing.border.LineBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Random;

public class VentanaSocio extends JFrame {

    private static final Color COLOR_PRINCIPAL = new Color(70, 130, 180); // Azul acero
    private static final Color COLOR_SECUNDARIO = new Color(176, 196, 222); // Azul claro
    private static final Color COLOR_FONDO = new Color(245, 245, 245); // Fondo gris claro
    private static String codigoDescuento;


    private Usuario socio;

    public VentanaSocio(Usuario socio) {
        this.socio = socio;
        setTitle("Perfil de Socio");
        setSize(600, 400);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);

        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        mainPanel.setBackground(COLOR_FONDO);

        // Sección superior: Título
        JLabel titulo = new JLabel("Perfil de Socio", SwingConstants.CENTER);
        titulo.setFont(new Font("Arial", Font.BOLD, 20));
        titulo.setForeground(COLOR_PRINCIPAL);
        mainPanel.add(titulo, BorderLayout.NORTH);

        // Sección central: Información del socio
        JPanel panelCentral = new JPanel(new GridBagLayout());
        panelCentral.setBackground(COLOR_FONDO);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // Nombre del Socio
        gbc.gridx = 0;
        gbc.gridy = 0;
        panelCentral.add(new JLabel("Nombre del Socio:"), gbc);

        gbc.gridx = 1;
        JTextField campoNombre = new JTextField(socio.getNombreDeUsuario(), 15);
        campoNombre.setFont(new Font("Arial", Font.PLAIN, 14));
        campoNombre.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(COLOR_PRINCIPAL),
            BorderFactory.createEmptyBorder(5, 5, 5, 5)));
        campoNombre.setEditable(false);
        campoNombre.setEnabled(false);
        panelCentral.add(campoNombre, gbc);

        // ID de Socio
        gbc.gridx = 0;
        gbc.gridy = 1;

        gbc.gridx = 1;
       

        // Membresía Activa
        gbc.gridx = 0;
        gbc.gridy = 2;
        panelCentral.add(new JLabel("Membresía Activa:"), gbc);

        gbc.gridx = 1;
        JCheckBox checkActivo = new JCheckBox("Activo", socio.getActivo());
        checkActivo.setEnabled(false);
        checkActivo.setBackground(COLOR_FONDO);
        panelCentral.add(checkActivo, gbc);

        // Tipo de Membresía
        gbc.gridx = 0;
        gbc.gridy = 3;
        panelCentral.add(new JLabel("Tipo de Membresía:"), gbc);

        gbc.gridx = 1;
        JLabel tipoMembresia = new JLabel(socio.getTipo().toString());
        tipoMembresia.setFont(new Font("Arial", Font.PLAIN, 14));
        tipoMembresia.setBorder(BorderFactory.createLineBorder(COLOR_PRINCIPAL));
        panelCentral.add(tipoMembresia, gbc);

        // Mostrar Cupones
        gbc.gridx = 0;
        gbc.gridy = 4;
        panelCentral.add(new JLabel("Cupones Disponibles:"), gbc);

        gbc.gridx = 1;
        JTextField textFieldCupon = new JTextField(getCodigoDescuento());
        textFieldCupon.setFont(new Font("Arial", Font.PLAIN, 14));
        textFieldCupon.setBorder(BorderFactory.createLineBorder(COLOR_PRINCIPAL));
        textFieldCupon.setHorizontalAlignment(SwingConstants.CENTER);
        panelCentral.add(textFieldCupon, gbc);
        textFieldCupon.setEditable(false);
        mainPanel.add(panelCentral, BorderLayout.CENTER);

        // Sección inferior: Botones
        JPanel panelBotones = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        panelBotones.setBackground(COLOR_FONDO);

        JButton btnEditar = createStyledButton("Editar");
        btnEditar.addActionListener(e -> {
            campoNombre.setEnabled(true);
            campoNombre.setEditable(true);
        });

        JButton btnGuardar = createStyledButton("Guardar");
        btnGuardar.addActionListener(e -> {
            socio.setNombreDeUsuario(campoNombre.getText().trim());
            JOptionPane.showMessageDialog(this, "Cambios guardados exitosamente.", "Éxito", JOptionPane.INFORMATION_MESSAGE);
            dispose();
        });

        JButton btnCancelar = createStyledButton("Cancelar");
        btnCancelar.addActionListener(e -> dispose());

        panelBotones.add(btnGuardar);
        panelBotones.add(btnEditar);
        panelBotones.add(btnCancelar);

        mainPanel.add(panelBotones, BorderLayout.SOUTH);

        add(mainPanel);
    }

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
    private static String generarCupon() {
        String caracteres = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
        Random random = new Random();
        StringBuilder cupon = new StringBuilder();
        for (int i = 0; i < 6; i++) {
            cupon.append(caracteres.charAt(random.nextInt(caracteres.length())));
        }
        return cupon.toString();
    }
    public static String getCodigoDescuento() {
        if (codigoDescuento == null) {
            codigoDescuento = generarCupon();
        }
        return codigoDescuento;
    }
    

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
        	Usuario socio = new Usuario("admin","123",true,TipoUsuario.ADMIN);
            new VentanaSocio(socio).setVisible(true);
        });
    }
}
