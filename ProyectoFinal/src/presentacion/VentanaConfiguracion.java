package presentacion;

import javax.swing.*;
import java.awt.*;
import dominio.*;

public class VentanaConfiguracion extends JFrame {
    private JSpinner spUvas, spPlatanos, spCerezas, spTrolls, spMacetas;
    
    public VentanaConfiguracion() {
        super("Configuración de Niveles");
        setSize(600, 500);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        getContentPane().setBackground(new Color(140, 200, 255));
        
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setOpaque(false);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        
        JLabel titulo = new JLabel("CONFIGURACIÓN DEL NIVEL 1");
        titulo.setFont(new Font("Arial", Font.BOLD, 24));
        gbc.gridx = 0; gbc.gridy = 0; gbc.gridwidth = 2;
        panel.add(titulo, gbc);
        
        gbc.gridwidth = 1;
        gbc.anchor = GridBagConstraints.WEST;
        
        // Frutas
        agregarConfig(panel, gbc, 1, "Uvas:", spUvas = crearSpinner(8));
        agregarConfig(panel, gbc, 2, "Plátanos:", spPlatanos = crearSpinner(6));
        agregarConfig(panel, gbc, 3, "Cerezas:", spCerezas = crearSpinner(4));
        
        // Enemigos
        agregarConfig(panel, gbc, 4, "Trolls:", spTrolls = crearSpinner(2));
        agregarConfig(panel, gbc, 5, "Macetas:", spMacetas = crearSpinner(1));
        
        // Botones
        JPanel botones = new JPanel(new FlowLayout());
        botones.setOpaque(false);
        
        JButton guardar = new JButton("GUARDAR");
        JButton restaurar = new JButton("VALORES POR DEFECTO");
        JButton volver = new JButton("VOLVER");
        
        guardar.addActionListener(e -> {
            NivelConfig.setCustomConfig((int)spUvas.getValue(), 
                (int)spPlatanos.getValue(), (int)spCerezas.getValue(),
                (int)spTrolls.getValue(), (int)spMacetas.getValue());
            JOptionPane.showMessageDialog(this, "Configuración guardada correctamente");
        });
        
        restaurar.addActionListener(e -> restaurarDefecto());
        
        volver.addActionListener(e -> {
            new VentanaInicio().setVisible(true);
            dispose();
        });
        
        botones.add(guardar);
        botones.add(restaurar);
        botones.add(volver);
        
        gbc.gridx = 0; gbc.gridy = 6; gbc.gridwidth = 2;
        panel.add(botones, gbc);
        
        add(panel);
    }
    
    private void agregarConfig(JPanel panel, GridBagConstraints gbc, int row, String label, JSpinner spinner) {
        gbc.gridy = row;
        gbc.gridx = 0;
        JLabel lbl = new JLabel(label);
        lbl.setFont(new Font("Arial", Font.BOLD, 16));
        panel.add(lbl, gbc);
        
        gbc.gridx = 1;
        panel.add(spinner, gbc);
    }
    
    private JSpinner crearSpinner(int valor) {
        JSpinner spinner = new JSpinner(new SpinnerNumberModel(valor, 0, 20, 1));
        spinner.setPreferredSize(new Dimension(80, 30));
        return spinner;
    }
    
    private void restaurarDefecto() {
        spUvas.setValue(8);
        spPlatanos.setValue(6);
        spCerezas.setValue(4);
        spTrolls.setValue(2);
        spMacetas.setValue(1);
    }
}