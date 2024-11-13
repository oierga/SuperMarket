package gui;

import java.awt.*;

import java.awt.event.ActionEvent;
import javax.swing.*;
import db.ServicioPersistenciaBD;
import domain.Usuario;

public class VentanaLogin extends JFrame {
    private JTextField tfNombreDeUsuario;
    private JTextField tfContrasena;
    private JLabel lErrores;

    public VentanaLogin(ServicioPersistenciaBD servicioPersistencia) {
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setTitle("Iniciar Sesión");
        setLocationRelativeTo(null);

        JPanel panelLabel = new JPanel(new GridBagLayout());

        tfNombreDeUsuario = new JTextField(20);
        tfContrasena = new JTextField(20);
        JButton bIniciarSesion = new JButton("Iniciar Sesión");
        JButton bCrearCuenta = new JButton("Crear Cuenta");
        
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(10, 10, 10, 10);
       
        addComponent(panelLabel, gbc, 0, 0, new JLabel("Nombre de usuario:"), tfNombreDeUsuario);
        addComponent(panelLabel, gbc, 1, 0, new JLabel("Contraseña:"), tfContrasena);
        
        lErrores = new JLabel();
        lErrores.setForeground(Color.RED);
        
        gbc.gridwidth = 2; // Ocupa dos columnas
        gbc.gridx = 0; 
        gbc.gridy = 2;
        
        panelLabel.add(lErrores, gbc);

        gbc.gridwidth = 1; // Reiniciar gridwidth
        
        
        bCrearCuenta.addActionListener(e -> new VentanaRegistro(servicioPersistencia));    
        bIniciarSesion.addActionListener(e -> handleLogin(servicioPersistencia));
        
        gbc.gridx = 0; 
        gbc.gridy = 3;
        
        panelLabel.add(bIniciarSesion, gbc);
        
        gbc.gridx = 1;
        
        panelLabel.add(bCrearCuenta, gbc);

        
       
        setContentPane(panelLabel); 

        setVisible(true);
    }

   

    private void addComponent(JPanel panel, GridBagConstraints gbc, int row, int col, JLabel label, JTextField textField) {
        gbc.gridx = col; gbc.gridy = row;
        panel.add(label, gbc);
        gbc.gridx = col + 1;
        panel.add(textField, gbc);
    }

    private void handleLogin(ServicioPersistenciaBD servicioPersistencia) {
        String nombreUsuario = tfNombreDeUsuario.getText();
        String contrasena = tfContrasena.getText();
        Usuario usuario = servicioPersistencia.cargarTodosUsuarios()
                .stream()
                .filter(u -> u.getNombreDeUsuario().equals(nombreUsuario))
                .findFirst()
                .orElse(null);

        
        if (usuario == null) {
            lErrores.setText("Nombre de usuario incorrecto");
        } else if (!usuario.getContraseña().equals(contrasena)) {
            lErrores.setText("Contraseña incorrecta");
        } else if (!usuario.getActivo()) {
            lErrores.setText("Este usuario está bloqueado");
        } else {
            lErrores.setText("Inicio de sesión exitoso");
        }
        dispose();
        VentanaSupermarket ventanaSupermarket = new VentanaSupermarket(usuario);
        ventanaSupermarket.setVisible(true);
    }

    

    
}

