package presentacion;

import javax.swing.*;
import java.awt.*;

public class VentanaInicio extends JFrame {
    public VentanaInicio() {
        super("Bad Ice-Cream");
        
        setSize(600, 400);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLayout(new BorderLayout());
        
        // Fondo con color del juego original
        getContentPane().setBackground(new Color(140, 200, 255));
        
        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
        mainPanel.setOpaque(false);
        mainPanel.setBorder(BorderFactory.createEmptyBorder(40, 40, 40, 40));
        
        // Título
        JLabel titulo = new JLabel("BAD ICE-CREAM");
        titulo.setFont(new Font("Arial", Font.BOLD, 48));
        titulo.setForeground(new Color(255, 100, 150));
        titulo.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        mainPanel.add(titulo);
        mainPanel.add(Box.createVerticalStrut(60));
        
        // Botones
        JButton jugar = crearBoton("JUGAR");
        JButton config = crearBoton("CONFIGURACIÓN");
        JButton cargar = crearBoton("CARGAR PARTIDA");
        JButton salir = crearBoton("SALIR");
        
        jugar.addActionListener(e -> {
            new VentanaModalidad().setVisible(true);
            dispose();
        });
        
        config.addActionListener(e -> {
            new VentanaConfiguracion().setVisible(true);
            dispose();
        });
        
        cargar.addActionListener(e -> {
            JOptionPane.showMessageDialog(this, "Función de carga en desarrollo");
        });
        
        salir.addActionListener(e -> System.exit(0));
        
        mainPanel.add(jugar);
        mainPanel.add(Box.createVerticalStrut(15));
        mainPanel.add(config);
        mainPanel.add(Box.createVerticalStrut(15));
        mainPanel.add(cargar);
        mainPanel.add(Box.createVerticalStrut(15));
        mainPanel.add(salir);
        
        add(mainPanel, BorderLayout.CENTER);
    }
    
    private JButton crearBoton(String texto) {
        JButton btn = new JButton(texto);
        btn.setFont(new Font("Arial", Font.BOLD, 20));
        btn.setAlignmentX(Component.CENTER_ALIGNMENT);
        btn.setMaximumSize(new Dimension(300, 50));
        btn.setBackground(new Color(255, 255, 255));
        btn.setFocusPainted(false);
        return btn;
    }
}