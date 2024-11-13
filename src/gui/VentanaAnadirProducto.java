package gui;

import javax.swing.*;

import domain.Categoria;
import domain.Producto;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import java.util.List;

public class VentanaAnadirProducto extends JFrame {
    private JTextField nombreField;
    private JTextField precioField;
    //pa seleccionar la categoria
    private JComboBox<Categoria> categoriaComboBox;

    public VentanaAnadirProducto(List<Categoria> categorias) {
        setTitle("Añadir Producto");
        setSize(300, 200);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);

        JPanel panel = new JPanel();
        panel.setLayout(new GridLayout(4, 2));

        JLabel nombreLabel = new JLabel("Nombre:");
        nombreField = new JTextField();

        JLabel precioLabel = new JLabel("Precio:");
        precioField = new JTextField();
        
        JLabel categoriaLabel = new JLabel("Categoría:");
        categoriaComboBox = new JComboBox<>();
        
        for (Categoria categoria : categorias) {
            categoriaComboBox.addItem(categoria);
        }

        JButton anadirButton = new JButton("Añadir");
        anadirButton.addActionListener(new ActionListener() {
        	
            @Override
            public void actionPerformed(ActionEvent e) {
                String nombre = nombreField.getText().trim();
                String precioStr = precioField.getText().trim();
                Categoria categoriaSeleccionada = (Categoria) categoriaComboBox.getSelectedItem();
                
                if (nombre.isEmpty() || precioStr.isEmpty() || categoriaSeleccionada == null) {
                    JOptionPane.showMessageDialog(null, "todos los campos son obligatorios.");
                    
                } else {
                    try {
                        double precio = Double.parseDouble(precioStr);
                        Producto nuevoProducto = new Producto(0, nombre, precio, "", categoriaSeleccionada);

                        // logica pa guardar el producto en la base de datos o lista
                        // servicioPersistencia.guardarProducto(nuevoProducto);

                        JOptionPane.showMessageDialog(null, "añadido exitosamente.");
                        dispose(); 

                    } catch (NumberFormatException ex) {
                        JOptionPane.showMessageDialog(null, "el precio debe ser un número válido.");
                    }
                }
            }
        });

        panel.add(nombreLabel);
        panel.add(nombreField);
        panel.add(precioLabel);
        panel.add(precioField);
        panel.add(anadirButton);
        panel.add(categoriaLabel);
        panel.add(categoriaComboBox);

        add(panel, BorderLayout.CENTER);
    }
}
