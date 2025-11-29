package presentacion;

import javax.swing.*;
import java.awt.*;
import dominio.*;

public class VentanaModalidad extends JFrame {
    public VentanaModalidad() {
        super("Seleccionar Modalidad");
        setSize(500, 450);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        getContentPane().setBackground(new Color(140, 200, 255));
        
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setOpaque(false);
        panel.setBorder(BorderFactory.createEmptyBorder(30, 30, 30, 30));
        
        JLabel label = new JLabel("SELECCIONA LA MODALIDAD");
        label.setFont(new Font("Arial", Font.BOLD, 24));
        label.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        panel.add(label);
        panel.add(Box.createVerticalStrut(40));
        
        JButton p1 = crearBoton("PLAYER (1 Jugador)");
        JButton pvp = crearBoton("PLAYER vs PLAYER");
        JButton pvm = crearBoton("PLAYER vs MACHINE");
        JButton mvm = crearBoton("MACHINE vs MACHINE");
        JButton volver = crearBoton("VOLVER");
        
        p1.addActionListener(e -> {
            new VentanaSeleccionJugador(GameMode.PLAYER).setVisible(true);
            dispose();
        });
        
        pvp.addActionListener(e -> {
            new VentanaSeleccionJugador(GameMode.PvsP).setVisible(true);
            dispose();
        });
        
        pvm.addActionListener(e -> {
            new VentanaSeleccionJugador(GameMode.PvsM).setVisible(true);
            dispose();
        });
        
        mvm.addActionListener(e -> {
            new VentanaSeleccionMaquinas().setVisible(true);
            dispose();
        });
        
        volver.addActionListener(e -> {
            new VentanaInicio().setVisible(true);
            dispose();
        });
        
        panel.add(p1);
        panel.add(Box.createVerticalStrut(10));
        panel.add(pvp);
        panel.add(Box.createVerticalStrut(10));
        panel.add(pvm);
        panel.add(Box.createVerticalStrut(10));
        panel.add(mvm);
        panel.add(Box.createVerticalStrut(30));
        panel.add(volver);
        
        add(panel);
    }
    
    private JButton crearBoton(String texto) {
        JButton btn = new JButton(texto);
        btn.setFont(new Font("Arial", Font.BOLD, 16));
        btn.setAlignmentX(Component.CENTER_ALIGNMENT);
        btn.setMaximumSize(new Dimension(300, 45));
        btn.setFocusPainted(false);
        return btn;
    }
}