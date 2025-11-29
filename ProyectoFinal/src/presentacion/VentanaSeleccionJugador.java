package presentacion;

import javax.swing.*;
import java.awt.*;
import dominio.*;

public class VentanaSeleccionJugador extends JFrame {
    private GameMode modo;
    
    public VentanaSeleccionJugador(GameMode modo) {
        super("Seleccionar Helado");
        this.modo = modo;
        setSize(550, 400);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        getContentPane().setBackground(new Color(140, 200, 255));
        
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setOpaque(false);
        panel.setBorder(BorderFactory.createEmptyBorder(30, 30, 30, 30));
        
        JLabel titulo = new JLabel("SELECCIONA TU HELADO");
        titulo.setFont(new Font("Arial", Font.BOLD, 24));
        titulo.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        panel.add(titulo);
        panel.add(Box.createVerticalStrut(40));
        
        JButton vainilla = crearBotonHelado("VAINILLA (Blanco)", Color.WHITE);
        JButton fresa = crearBotonHelado("FRESA (Rosado)", new Color(255, 182, 193));
        JButton chocolate = crearBotonHelado("CHOCOLATE (Café)", new Color(139, 69, 19));
        JButton volver = crearBoton("VOLVER");
        
        vainilla.addActionListener(e -> iniciarJuego(HeladoType.VAINILLA));
        fresa.addActionListener(e -> iniciarJuego(HeladoType.FRESA));
        chocolate.addActionListener(e -> iniciarJuego(HeladoType.CHOCOLATE));
        volver.addActionListener(e -> {
            new VentanaModalidad().setVisible(true);
            dispose();
        });
        
        panel.add(vainilla);
        panel.add(Box.createVerticalStrut(15));
        panel.add(fresa);
        panel.add(Box.createVerticalStrut(15));
        panel.add(chocolate);
        panel.add(Box.createVerticalStrut(30));
        panel.add(volver);
        
        add(panel);
    }
    
    private JButton crearBotonHelado(String texto, Color colorFondo) {
        JButton btn = new JButton(texto);
        btn.setFont(new Font("Arial", Font.BOLD, 18));
        btn.setAlignmentX(Component.CENTER_ALIGNMENT);
        btn.setMaximumSize(new Dimension(350, 50));
        btn.setBackground(colorFondo);
        btn.setFocusPainted(false);
        return btn;
    }
    
    private JButton crearBoton(String texto) {
        JButton btn = new JButton(texto);
        btn.setFont(new Font("Arial", Font.BOLD, 16));
        btn.setAlignmentX(Component.CENTER_ALIGNMENT);
        btn.setMaximumSize(new Dimension(300, 45));
        btn.setFocusPainted(false);
        return btn;
    }
    
    private void iniciarJuego(HeladoType tipo) {
        String nombre = JOptionPane.showInputDialog(this, "Ingresa tu nombre:");
        if (nombre != null && !nombre.trim().isEmpty()) {
            new JuegoWindow(tipo, nombre, modo).setVisible(true);
            dispose();
        }
    }
}