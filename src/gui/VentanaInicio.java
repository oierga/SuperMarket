package gui;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JTextField;

import db.ServicioPersistenciaBD;
import domain.Usuario;

import java.awt.Color;

public class VentanaInicio {
    private JFrame fLogin;
    private JLabel lNombreDeUsuario;
    private JTextField tfNombreDeUsuario;		
    private JLabel lContraseña;
    private JTextField tfContraseña;
    private JLabel lErrores;
    private JButton bIniciarSesion;
    private JButton bCrearCuenta;
    
    /*el resto esta comentado abajo*/
    //private boolean isAuthenticated = false;
    //private Usuario usuarioAutenticado;

    public VentanaInicio(ServicioPersistenciaBD servicioPersistencia) {
        lNombreDeUsuario = new JLabel("Nombre de usuario:");
        lNombreDeUsuario.setBounds(10, 10, 160, 30);
        tfNombreDeUsuario = new JTextField();
        tfNombreDeUsuario.setBounds(180, 11, 226, 30);

        lContraseña = new JLabel("Contraseña:");
        lContraseña.setBounds(10, 50, 160, 30);
        tfContraseña = new JTextField();
        tfContraseña.setBounds(180, 51, 226, 30);

        lErrores = new JLabel();
        lErrores.setForeground(new Color(255, 0, 0));
        lErrores.setBounds(10, 104, 366, 30);

        bIniciarSesion = new JButton("Iniciar Sesión");
        bIniciarSesion.setBounds(125, 158, 180, 30);
        bCrearCuenta = new JButton("Crear Cuenta");
        bCrearCuenta.setBounds(125, 198, 180, 30);

        fLogin = new JFrame("Login");
        fLogin.setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
        fLogin.setSize(430, 275);
        fLogin.setVisible(true);
        fLogin.getContentPane().setLayout(null);
        fLogin.getContentPane().add(lNombreDeUsuario);
        fLogin.getContentPane().add(tfNombreDeUsuario);
        fLogin.getContentPane().add(lContraseña);
        fLogin.getContentPane().add(tfContraseña);
        fLogin.getContentPane().add(lErrores);
        fLogin.getContentPane().add(bIniciarSesion);
        fLogin.getContentPane().add(bCrearCuenta);

        // Listeners
        bCrearCuenta.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                new VentanaRegistro(servicioPersistencia);
            }
        });

        bIniciarSesion.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Usuario usuario = null;
                boolean isFound = false;
                ArrayList<Usuario> usuarios = servicioPersistencia.cargarTodosUsuarios();
                for (Usuario u : usuarios) {
                    if (u.getNombreDeUsuario().equals(tfNombreDeUsuario.getText())) {
                        isFound = true;
                        usuario = u;
                    }
                }
                
                if (!isFound) {
                    lErrores.setText("Nombre de usuario incorrecto");
                } else if (!usuario.getContraseña().equals(tfContraseña.getText())) {
                    lErrores.setText("Contraseña incorrecta");
                } else if (!usuario.getActivo()) {
                    lErrores.setText("Este usuario está bloqueado");
                } //else {
                  //  isAuthenticated = true;
                  //  usuarioAutenticado = usuario;
                  //  fLogin.dispose();  
               // }
            }
        });
    }
    /*
    
    public boolean isAuthenticated() {
        return isAuthenticated;
    }
    
    public Usuario getUsuarioAutenticado() {
        return usuarioAutenticado;
    }
}

    
    public boolean isAuthenticated() {
        return isAuthenticated;
    }
    
    public Usuario getUsuarioAutenticado() {
        return usuarioAutenticado;
    }
    */
}
