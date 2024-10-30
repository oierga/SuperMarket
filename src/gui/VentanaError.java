package gui;

import java.awt.Font;
import java.awt.GridLayout;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.SwingConstants;

public class VentanaError extends JFrame {
    
	private static final long serialVersionUID = 1L;
	private JLabel lError;
    private JLabel lErrorMessage;
    
    public VentanaError(String nombre) {
        lError = new JLabel("404", SwingConstants.CENTER);
        lError.setFont(new Font("Tahoma", Font.PLAIN, 99));
        
        lErrorMessage = new JLabel("Not found", SwingConstants.CENTER);
        lErrorMessage.setFont(new Font("Tahoma", Font.PLAIN, 30));
        
        setTitle(nombre);
        setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
        setSize(350, 350);
        getContentPane().setLayout(new GridLayout(2, 1)); 
        getContentPane().add(lError);
        getContentPane().add(lErrorMessage);
    }
    
    public void mostrar() {
        setVisible(true);
    }
}
