package gui;


import domain.TipoUsuario;
import domain.Usuario;
import javax.swing.*;
import javax.swing.border.LineBorder;

import db.ServicioPersistenciaBD;

import java.awt.*;

public class VentanaUsuario extends JFrame {

    private static final Color COLOR_PRINCIPAL = new Color(34, 139, 34); // Verde oscuro
    private static final Color COLOR_SECUNDARIO = new Color(144, 238, 144); // Verde claro
    private static final Color COLOR_FONDO = new Color(240, 255, 240); // Fondo blanco verdoso

    private Usuario usuario;

    public VentanaUsuario(Usuario usuario) {
        this.usuario = usuario;
        setTitle("Perfil de Usuario");
        setSize(600, 400);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);

        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        mainPanel.setBackground(COLOR_FONDO);

        // Sección superior: Título
        JLabel titulo = new JLabel("Perfil de Usuario", SwingConstants.CENTER);
        titulo.setFont(new Font("Arial", Font.BOLD, 20));
        titulo.setForeground(COLOR_PRINCIPAL);
        mainPanel.add(titulo, BorderLayout.NORTH);

        // Sección central: Información del usuario
        JPanel panelCentral = new JPanel();
        panelCentral.setLayout(new GridBagLayout());
        panelCentral.setBackground(COLOR_FONDO);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // Nombre de Usuario
        gbc.gridx = 0;
        gbc.gridy = 0;
        panelCentral.add(new JLabel("Nombre de Usuario:"), gbc);

        gbc.gridx = 1;
        JTextField campoNombreUsuario = new JTextField(usuario.getNombreDeUsuario(), 15);
        campoNombreUsuario.setFont(new Font("Arial", Font.PLAIN, 14));
        campoNombreUsuario.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(COLOR_PRINCIPAL),
            BorderFactory.createEmptyBorder(5, 5, 5, 5)));
        campoNombreUsuario.setText(usuario.getNombreDeUsuario());
        campoNombreUsuario.setEditable(false);
        campoNombreUsuario.setEnabled(false);
        panelCentral.add(campoNombreUsuario, gbc);

        // Contraseña
        gbc.gridx = 0;
        gbc.gridy = 1;
        panelCentral.add(new JLabel("Contraseña:"), gbc);
       

        gbc.gridx = 1;
        JPasswordField campoContrasena = new JPasswordField(usuario.getContrasena(), 15);
        campoContrasena.setFont(new Font("Arial", Font.PLAIN, 14));
        campoContrasena.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(COLOR_PRINCIPAL),
            BorderFactory.createEmptyBorder(5, 5, 5, 5)));
        campoContrasena.setText(usuario.getContrasena());
        campoContrasena.setForeground(COLOR_PRINCIPAL);
        campoContrasena.setEditable(false);
        campoContrasena.setEnabled(false);
        panelCentral.add(campoContrasena, gbc);

        // Estado
        gbc.gridx = 0;
        gbc.gridy = 2;
        panelCentral.add(new JLabel("Estado:"), gbc);

        gbc.gridx = 1;
        JCheckBox checkActivo = new JCheckBox("Activo", usuario.getActivo());
        checkActivo.setEnabled(false);
        checkActivo.setBackground(COLOR_FONDO);
        panelCentral.add(checkActivo, gbc);

        // Tipo de Usuario
        gbc.gridx = 0;
        gbc.gridy = 3;
        panelCentral.add(new JLabel("Tipo de Usuario:"), gbc);

        gbc.gridx = 1;
        TipoUsuario tipoUsuario;
        if (usuario.getNombreDeUsuario().contains("admin")) {
        	tipoUsuario = TipoUsuario.ADMIN;
        }else {
        	tipoUsuario = TipoUsuario.USUARIO;
        }
        JLabel comboTipo = new JLabel(""+tipoUsuario.toString().substring(0,1)+tipoUsuario.toString().substring(1).toLowerCase());
        comboTipo.setFont(new Font("Arial", Font.PLAIN, 14));
        comboTipo.setBorder(BorderFactory.createLineBorder(COLOR_PRINCIPAL));
        panelCentral.add(comboTipo, gbc);

        mainPanel.add(panelCentral, BorderLayout.CENTER);

        // Sección inferior: Botones
        JPanel panelBotones = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        panelBotones.setBackground(COLOR_FONDO);
        
        JButton btnEditar = createStyledButton("Editar");
        btnEditar.addActionListener(e -> {
        	campoNombreUsuario.setEnabled(true);
        	campoNombreUsuario.setEditable(true);
        	campoContrasena.setEnabled(true);
        	campoContrasena.setEditable(true);
        	
        });
        
        JButton btnGuardar = createStyledButton("Guardar");
        btnGuardar.addActionListener(e -> {
        	String nombre = usuario.getNombreDeUsuario();
            usuario.setNombreDeUsuario(campoNombreUsuario.getText().trim());
            usuario.setContrasena(new String(campoContrasena.getPassword()));
            usuario.setActivo(checkActivo.isSelected());
            usuario.setTipo(tipoUsuario);
            ServicioPersistenciaBD.getInstance().actualizarUsuario(nombre,usuario);
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
        
        if (text.contains("Guar")) {
        	button.setBackground(COLOR_PRINCIPAL);
        	button.setForeground(Color.WHITE);
            button.setOpaque(true);

        	
        }else {
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
