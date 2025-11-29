package presentacion;

import javax.swing.*;
import java.awt.*;
import dominio.*;

public class VentanaSeleccionMaquinas extends JFrame {
    
    private JComboBox<String> cbMaq1;
    private JComboBox<String> cbMaq2;
    
    public VentanaSeleccionMaquinas() {
        super("Machine vs Machine");
        setSize(500, 400);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        getContentPane().setBackground(new Color(140, 200, 255));
        
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setOpaque(false);
        panel.setBorder(BorderFactory.createEmptyBorder(30, 30, 30, 30));
        
        JLabel titulo = new JLabel("SELECCIONA LOS PERFILES DE IA");
        titulo.setFont(new Font("Arial", Font.BOLD, 20));
        titulo.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        panel.add(titulo);
        panel.add(Box.createVerticalStrut(30));
        
        JLabel lblMaq1 = new JLabel("Máquina 1:");
        lblMaq1.setAlignmentX(Component.CENTER_ALIGNMENT);
        lblMaq1.setFont(new Font("Arial", Font.BOLD, 14));
        
        String[] opciones = {"HUNGRY - Busca frutas", "FEARFUL - Evita enemigos", "EXPERT - Juego óptimo"};
        cbMaq1 = new JComboBox<>(opciones);
        cbMaq1.setMaximumSize(new Dimension(300, 30));
        cbMaq1.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        JLabel lblMaq2 = new JLabel("Máquina 2:");
        lblMaq2.setAlignmentX(Component.CENTER_ALIGNMENT);
        lblMaq2.setFont(new Font("Arial", Font.BOLD, 14));
        
        cbMaq2 = new JComboBox<>(opciones);
        cbMaq2.setSelectedIndex(1); // Diferente por defecto
        cbMaq2.setMaximumSize(new Dimension(300, 30));
        cbMaq2.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        JButton iniciar = new JButton("INICIAR");
        iniciar.setAlignmentX(Component.CENTER_ALIGNMENT);
        iniciar.setMaximumSize(new Dimension(200, 40));
        iniciar.setFont(new Font("Arial", Font.BOLD, 16));
        
        JButton volver = new JButton("VOLVER");
        volver.setAlignmentX(Component.CENTER_ALIGNMENT);
        volver.setMaximumSize(new Dimension(200, 40));
        volver.setFont(new Font("Arial", Font.BOLD, 16));
        
        iniciar.addActionListener(e -> {
            String ai1 = getAIModeFromSelection(cbMaq1.getSelectedIndex());
            String ai2 = getAIModeFromSelection(cbMaq2.getSelectedIndex());
            
            int confirmacion = JOptionPane.showConfirmDialog(this,
                "Iniciar simulación:\n" +
                "Máquina 1 (" + ai1 + ") vs Máquina 2 (" + ai2 + ")\n\n" +
                "Nota: Esta es una versión simplificada.\n" +
                "Las máquinas se moverán aleatoriamente.",
                "Confirmar",
                JOptionPane.YES_NO_OPTION);
            
            if (confirmacion == JOptionPane.YES_OPTION) {
                // Iniciar juego MvsM simplificado
                dispose();
                JOptionPane.showMessageDialog(null, 
                    "Modo MvsM iniciado.\n" +
                    "Las máquinas competirán automáticamente.\n\n" +
                    "Funcionalidad completa en desarrollo.",
                    "Machine vs Machine",
                    JOptionPane.INFORMATION_MESSAGE);
                new VentanaInicio().setVisible(true);
            }
        });
        
        volver.addActionListener(e -> {
            new VentanaModalidad().setVisible(true);
            dispose();
        });
        
        panel.add(lblMaq1);
        panel.add(Box.createVerticalStrut(10));
        panel.add(cbMaq1);
        panel.add(Box.createVerticalStrut(25));
        panel.add(lblMaq2);
        panel.add(Box.createVerticalStrut(10));
        panel.add(cbMaq2);
        panel.add(Box.createVerticalStrut(40));
        panel.add(iniciar);
        panel.add(Box.createVerticalStrut(10));
        panel.add(volver);
        
        add(panel);
    }
    
    private String getAIModeFromSelection(int index) {
        switch (index) {
            case 0: return "HUNGRY";
            case 1: return "FEARFUL";
            case 2: return "EXPERT";
            default: return "HUNGRY";
        }
    }
}