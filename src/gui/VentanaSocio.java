package gui;

import domain.TipoUsuario;
import domain.Usuario;
import db.ServicioPersistenciaBD;
import javax.swing.*;
import javax.swing.border.LineBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.util.Random;

public class VentanaSocio extends JFrame {

	private static final Color COLOR_PRINCIPAL = new Color(34, 139, 34); 
    private static final Color COLOR_SECUNDARIO = new Color(144, 238, 144); 
    private static final Color COLOR_FONDO = new Color(255, 250, 240); 
    private static final Font FUENTE_TITULO = new Font("Arial", Font.BOLD, 22);
    private static final Font FUENTE_NORMAL = new Font("Arial", Font.PLAIN, 14);
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
        JPanel panelSuperior = new JPanel(new BorderLayout());
        panelSuperior.setBackground(COLOR_FONDO);
        
        JLabel titulo = new JLabel("¡Bienvenido, " + socio.getNombreDeUsuario() + "!", SwingConstants.CENTER);
        titulo.setFont(FUENTE_TITULO);
        titulo.setForeground(COLOR_PRINCIPAL);
        panelSuperior.add(titulo, BorderLayout.CENTER);
        mainPanel.add(panelSuperior, BorderLayout.NORTH);

        // Sección central: Información del socio
        JPanel panelCentral = new JPanel(new GridBagLayout());
        panelCentral.setBackground(COLOR_FONDO);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // Nombre del Socio
        gbc.gridx = 0;
        gbc.gridy = 0;
        panelCentral.add(new JLabel("Nombre del Socio:"), gbc);

        gbc.gridx = 1;
        JTextField campoNombre = new JTextField(socio.getNombreDeUsuario(), 15);
        configurarCampoTexto(campoNombre);
        campoNombre.setEditable(false);
        panelCentral.add(campoNombre, gbc);
       

        // Membresía Activa
        gbc.gridx = 0;
        gbc.gridy = 1;
        panelCentral.add(new JLabel("Membresía Activa:"), gbc);

        gbc.gridx = 1;
        JCheckBox checkActivo = new JCheckBox("Activo", socio.getActivo());
        checkActivo.setEnabled(false);
        checkActivo.setBackground(COLOR_FONDO);
        panelCentral.add(checkActivo, gbc);

        // Tipo de Membresía
        gbc.gridx = 0;
        gbc.gridy = 2;
        panelCentral.add(new JLabel("Tipo de Membresía:"), gbc);

        gbc.gridx = 1;
        JLabel tipoMembresia = new JLabel(socio.getTipo().toString());
        tipoMembresia.setFont(FUENTE_NORMAL);
        tipoMembresia.setBorder(BorderFactory.createLineBorder(COLOR_PRINCIPAL));
        tipoMembresia.setHorizontalAlignment(SwingConstants.CENTER);
        panelCentral.add(tipoMembresia, gbc);

        // Mostrar Cupones
        gbc.gridx = 0;
        gbc.gridy = 3;
        panelCentral.add(new JLabel("Cupones Disponibles:"), gbc);

        gbc.gridx = 1;
        JTextField textFieldCupon = new JTextField(getCodigoDescuento());
        configurarCampoTexto(textFieldCupon);
        textFieldCupon.setHorizontalAlignment(SwingConstants.CENTER);
        textFieldCupon.setEditable(false);
        panelCentral.add(textFieldCupon, gbc);
        
        JButton btnGenerarCupon = createStyledButton("Generar Nuevo Cupón");
        btnGenerarCupon.addActionListener(e -> textFieldCupon.setText(generarCupon()));

        gbc.gridx = 1;
        gbc.gridy = 4;
        panelCentral.add(btnGenerarCupon, gbc);	
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
            String nuevoNombre = campoNombre.getText().trim();
            if (!nuevoNombre.isEmpty() && !nuevoNombre.equals(socio.getNombreDeUsuario())) {
                socio.setNombreDeUsuario(nuevoNombre);
                JOptionPane.showMessageDialog(this, "Cambios guardados exitosamente.", "Éxito", JOptionPane.INFORMATION_MESSAGE);
                dispose();
            } else {
                JOptionPane.showMessageDialog(this, "Por favor ingrese un nombre válido.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        });

        JButton btnCancelar = createStyledButton("Cancelar");
        btnCancelar.addActionListener(e -> dispose());

        panelBotones.add(btnGuardar);
        panelBotones.add(btnEditar);
        panelBotones.add(btnCancelar);

        mainPanel.add(panelBotones, BorderLayout.SOUTH);

        add(mainPanel);
        configurarTeclasAccesoRapidoGuardar(btnGuardar);
        configurarTeclasAccesoRapidoCerrar(btnCancelar);
    }
    private void configurarTeclasAccesoRapidoGuardar(JButton btnGuardar) {
    	btnGuardar.setMnemonic(KeyEvent.VK_G);
        getRootPane().getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke("control G"), "guardar");
        getRootPane().getActionMap().put("guardar", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                btnGuardar.doClick();
            }
        });
    }
    private void configurarTeclasAccesoRapidoCerrar(JButton btnCancelar) {
        btnCancelar.setMnemonic(KeyEvent.VK_D);
        getRootPane().getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke("control D"), "cerrar");
        getRootPane().getActionMap().put("cerrar", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                btnCancelar.doClick();
            }
        });
    }
        
    private void configurarCampoTexto(JTextField textField) {
        textField.setFont(FUENTE_NORMAL);
        textField.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(COLOR_PRINCIPAL),
            BorderFactory.createEmptyBorder(5, 5, 5, 5)));
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
