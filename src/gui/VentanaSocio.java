package gui;

import javax.swing.*;

public class VentanaSocio extends JFrame{

	public VentanaSocio(String codigo) {
        // Configuración de la ventana
        setTitle("Ventana Socio");
        setSize(300, 200);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);

        // Panel principal
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));

        // Título y código de la ofertax
        JLabel tituloLabel = new JLabel("OfertaSocio:");
        tituloLabel.setAlignmentX(CENTER_ALIGNMENT);
        JLabel codigoLabel = new JLabel(codigo);
        codigoLabel.setAlignmentX(CENTER_ALIGNMENT);

        // Añadir componentes al panel
        panel.add(tituloLabel);
        panel.add(codigoLabel);

        // Añadir panel a la ventana
        add(panel);
    }
}
