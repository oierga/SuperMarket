package gui;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JTextField;

import db.ServicioPersistenciaBD;
import domain.Usuario;

public class VentanaRegistro {
    private JFrame fCrearUsuario;
    private JLabel lNombreDeUsuario;
    private JTextField tfNombreDeUsuario;		
    private JLabel lContraseña;
    private JTextField tfContraseña;
    private JButton bCrearCuenta;
    
    public VentanaRegistro(ServicioPersistenciaBD servicioPersistencia) {
        lNombreDeUsuario = new JLabel("Nombre de usuario:");
        lNombreDeUsuario.setBounds(10, 10, 160, 30);
        tfNombreDeUsuario = new JTextField();
        tfNombreDeUsuario.setBounds(180, 11, 226, 30);

        lContraseña = new JLabel("Contraseña:");
        lContraseña.setBounds(10, 50, 160, 30);
        tfContraseña = new JTextField();
        tfContraseña.setBounds(180, 51, 226, 30);

        bCrearCuenta = new JButton("Crear Cuenta");
        bCrearCuenta.setBounds(125, 100, 180, 30);

        fCrearUsuario = new JFrame("Crear Usuario");
        fCrearUsuario.setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
        fCrearUsuario.setSize(430, 200);
        fCrearUsuario.setVisible(true);
        fCrearUsuario.getContentPane().setLayout(null);
        fCrearUsuario.getContentPane().add(lNombreDeUsuario);
        fCrearUsuario.getContentPane().add(tfNombreDeUsuario);
        fCrearUsuario.getContentPane().add(lContraseña);
        fCrearUsuario.getContentPane().add(tfContraseña);
        fCrearUsuario.getContentPane().add(bCrearCuenta);

        //Listener pa crear la cuenta
        bCrearCuenta.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String nombreDeUsuario = tfNombreDeUsuario.getText();
                String contraseña = tfContraseña.getText();

                if (!nombreDeUsuario.isEmpty() && !contraseña.isEmpty()) {
                    Usuario nuevoUsuario = new Usuario(nombreDeUsuario, contraseña, true);
                    servicioPersistencia.guardarUsuario(nuevoUsuario);
                    fCrearUsuario.dispose(); 
                }
            }
        });
    }
}

