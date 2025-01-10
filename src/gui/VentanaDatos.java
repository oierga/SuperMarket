package gui;

import javax.swing.*;
import java.awt.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class VentanaDatos extends JFrame {

    private static final Color COLOR_PRINCIPAL = new Color(34, 139, 34);
    private static final Color COLOR_SECUNDARIO = new Color(144, 238, 144);
    private static final Color COLOR_FONDO = new Color(240, 255, 240);

    public VentanaDatos() {
        setTitle("Datos del Sistema");
        setSize(500, 400);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        JPanel headerPanel = new JPanel();
        headerPanel.setBackground(COLOR_PRINCIPAL);
        headerPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        JLabel titleLabel = new JLabel("Información del Sistema");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 20));
        titleLabel.setForeground(Color.WHITE);
        headerPanel.add(titleLabel);

        JPanel infoPanel = new JPanel();
        infoPanel.setLayout(new BoxLayout(infoPanel, BoxLayout.Y_AXIS));
        infoPanel.setBackground(COLOR_FONDO);
        infoPanel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(COLOR_PRINCIPAL),
            BorderFactory.createEmptyBorder(15, 15, 15, 15)
        ));
        addInfoSection(infoPanel, "Versión del Sistema", "1.0.0");
        addSeparator(infoPanel);
        addInfoSection(infoPanel, "Fecha y Hora", getCurrentDateTime());
        addSeparator(infoPanel);
        addInfoSection(infoPanel, "Java Version", System.getProperty("java.version"));
        addSeparator(infoPanel);
        addInfoSection(infoPanel, "Sistema Operativo", System.getProperty("os.name"));
        addSeparator(infoPanel);
        addInfoSection(infoPanel, "Memoria Disponible", formatMemoryInfo(Runtime.getRuntime().freeMemory()));
        addSeparator(infoPanel);
        addInfoSection(infoPanel, "Memoria Total", formatMemoryInfo(Runtime.getRuntime().totalMemory()));

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        buttonPanel.setBackground(COLOR_FONDO);
        JButton closeButton = new JButton("Cerrar");
        styleButton(closeButton);
        closeButton.addActionListener(e -> dispose());
        buttonPanel.add(closeButton);

        JPanel mainPanel = new JPanel(new GridBagLayout());
        mainPanel.setBackground(COLOR_FONDO);
        mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridwidth = GridBagConstraints.REMAINDER;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(0, 0, 10, 0);

        gbc.weighty = 0;
        mainPanel.add(headerPanel, gbc);

        gbc.weighty = 1.0;
        gbc.fill = GridBagConstraints.BOTH;
        mainPanel.add(infoPanel, gbc);

        gbc.weighty = 0;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(10, 0, 0, 0);
        mainPanel.add(buttonPanel, gbc);

        add(mainPanel);
    }

    private void addInfoSection(JPanel panel, String label, String value) {
        JPanel sectionPanel = new JPanel(new BorderLayout(10, 0));
        sectionPanel.setBackground(COLOR_FONDO);

        JLabel labelComponent = new JLabel(label + ":");
        labelComponent.setFont(new Font("Arial", Font.BOLD, 14));
        labelComponent.setForeground(COLOR_PRINCIPAL);

        JLabel valueComponent = new JLabel(value);
        valueComponent.setFont(new Font("Arial", Font.PLAIN, 14));

        sectionPanel.add(labelComponent, BorderLayout.WEST);
        sectionPanel.add(valueComponent, BorderLayout.CENTER);

        panel.add(sectionPanel);
        panel.add(Box.createVerticalStrut(10));
    }

    private void addSeparator(JPanel panel) {
        JSeparator separator = new JSeparator();
        separator.setForeground(COLOR_SECUNDARIO);
        panel.add(separator);
        panel.add(Box.createVerticalStrut(10));
    }

    private void styleButton(JButton button) {
        button.setBackground(COLOR_PRINCIPAL);
        button.setForeground(Color.WHITE);
        button.setFont(new Font("Arial", Font.BOLD, 14));
        button.setBorder(BorderFactory.createEmptyBorder(8, 15, 8, 15));
        button.setFocusPainted(false);
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));

        // Efectos hover
        button.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                button.setBackground(COLOR_SECUNDARIO);
                button.setForeground(Color.BLACK);
            }

            public void mouseExited(java.awt.event.MouseEvent evt) {
                button.setBackground(COLOR_PRINCIPAL);
                button.setForeground(Color.WHITE);
            }
        });
    }

    private String getCurrentDateTime() {
        LocalDateTime now = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss");
        return now.format(formatter);
    }

    private String formatMemoryInfo(long bytes) {
        return String.format("%.2f MB", bytes / (1024.0 * 1024.0));
    }
}
