package gui;

import javax.swing.*;

import domain.Usuario;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class VentanaAdmin extends JFrame {
    private Usuario usuario;

    public VentanaAdmin(Usuario usuario) {
        this.usuario = usuario;
        
        setTitle("Panel de Administración");
        setSize(400, 300);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        JLabel labelTitulo = new JLabel("Bienvenido al Panel de Administración");
        labelTitulo.setFont(new Font("Arial", Font.BOLD, 18));
        labelTitulo.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel labelSubtitulo = new JLabel("Gestiona los datos y productos del supermercado");
        labelSubtitulo.setFont(new Font("Arial", Font.ITALIC, 14));
        labelSubtitulo.setAlignmentX(Component.CENTER_ALIGNMENT);

        JButton btnVerDatos = new JButton("Ver Datos");
        btnVerDatos.setAlignmentX(Component.CENTER_ALIGNMENT);
        btnVerDatos.setPreferredSize(new Dimension(200, 40));
        btnVerDatos.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                VentanaDatos ventanaDatos = new VentanaDatos();
                ventanaDatos.setVisible(true);
            }
        });

        JButton btnAnadirProducto = new JButton("Añadir Producto");
        btnAnadirProducto.setAlignmentX(Component.CENTER_ALIGNMENT);
        btnAnadirProducto.setPreferredSize(new Dimension(200, 40));
        btnAnadirProducto.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                VentanaAnadirProducto ventanaAnadirProducto = new VentanaAnadirProducto(null);
                ventanaAnadirProducto.setVisible(true);
            }
        });

        panel.add(labelTitulo);
        panel.add(Box.createRigidArea(new Dimension(0, 10)));
        panel.add(labelSubtitulo);
        panel.add(Box.createRigidArea(new Dimension(0, 20)));
        panel.add(btnVerDatos);
        panel.add(Box.createRigidArea(new Dimension(0, 10)));
        panel.add(btnAnadirProducto);

        add(panel);
    }
}
