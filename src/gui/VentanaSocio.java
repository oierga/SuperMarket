package gui;
import javax.swing.*;
import java.awt.*;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.StringSelection;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class VentanaSocio extends JFrame {
    public VentanaSocio(String codigo) {
        // Configuración de la ventana
        setTitle("Ventana Socio");
        setSize(400, 300);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);

        // Panel principal con estilo
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        // Título de bienvenida
        JLabel tituloLabel = new JLabel("¡Bienvenido, Socio!");
        tituloLabel.setFont(new Font("Arial", Font.BOLD, 18));
        tituloLabel.setAlignmentX(CENTER_ALIGNMENT);

        // Texto descriptivo
        JLabel descripcionLabel = new JLabel("<html>Como socio, disfruta de ofertas exclusivas y descuentos.<br>" +
                                             "Tu código de descuento es:</html>");
        descripcionLabel.setAlignmentX(CENTER_ALIGNMENT);

        // Código de descuento en un área estilizada
        JLabel codigoLabel = new JLabel(codigo);
        codigoLabel.setFont(new Font("Arial", Font.BOLD, 24));
        codigoLabel.setForeground(Color.BLUE);
        codigoLabel.setAlignmentX(CENTER_ALIGNMENT);
        
        // Botón para copiar el código al portapapeles
        JButton copiarButton = new JButton("Copiar Código");
        copiarButton.setAlignmentX(CENTER_ALIGNMENT);
        copiarButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                StringSelection stringSelection = new StringSelection(codigo);
                Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
                clipboard.setContents(stringSelection, null);
                JOptionPane.showMessageDialog(null, "Código copiado al portapapeles");
            }
        });

        // Añadir componentes al panel
        panel.add(tituloLabel);
        panel.add(Box.createRigidArea(new Dimension(0, 10))); // Espacio entre componentes
        panel.add(descripcionLabel);
        panel.add(Box.createRigidArea(new Dimension(0, 15)));
        panel.add(codigoLabel);
        panel.add(Box.createRigidArea(new Dimension(0, 15)));
        panel.add(copiarButton);

        // Añadir el panel a la ventana
        add(panel);
    }
}

