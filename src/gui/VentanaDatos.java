package gui;

import javax.swing.*;

public class VentanaDatos extends JFrame {
    public VentanaDatos() {
        setTitle("Datos del Sistema");
        setSize(400, 300);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        JLabel label = new JLabel("Aquí puedes ver los datos del sistema.");
        label.setHorizontalAlignment(SwingConstants.CENTER);

        add(label);
    }
}

