// ============================================================================
// JUEGOWINDOW.JAVA - VERSIÓN CORREGIDA (FIX PARA SPACE)
// Reemplaza tu JuegoWindow.java con este código
// ============================================================================
package presentacion;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import dominio.*;

public class JuegoWindow extends JFrame {
    private GamePanel panel;
    private Juego juego;
    private JLabel lblTiempo, lblPuntos, lblFrutas;
    private Timer timerUI;
    
    public JuegoWindow(HeladoType tipo, String nombre, GameMode modo) {
        super("Bad Ice-Cream - Nivel 1");
        setSize(960, 720);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLayout(new BorderLayout());
        
        NivelConfig cfg = NivelConfig.getConfig();
        juego = new Juego(modo, cfg, tipo, nombre);
        panel = new GamePanel(juego, this);
        
        // HUD superior
        JPanel hud = new JPanel(new FlowLayout(FlowLayout.LEFT, 20, 10));
        hud.setBackground(new Color(60, 120, 180));
        
        lblTiempo = crearLabel("Tiempo: 3:00");
        lblPuntos = crearLabel("Puntos: 0");
        lblFrutas = crearLabel("Frutas: 0/0");
        
        hud.add(lblTiempo);
        hud.add(lblPuntos);
        hud.add(lblFrutas);
        
        // ====================================================================
        // FIX: Deshabilitar focus del botón para que no capture SPACE
        // ====================================================================
        JButton btnPausa = new JButton("PAUSA (P)");
        btnPausa.setFocusable(false); // ⭐ ESTO ES LA CLAVE
        btnPausa.addActionListener(e -> juego.togglePause());
        hud.add(btnPausa);
        
        JButton btnSalir = new JButton("SALIR");
        btnSalir.setFocusable(false); // ⭐ TAMBIÉN ESTE
        btnSalir.addActionListener(e -> salirJuego());
        hud.add(btnSalir);
        
        add(hud, BorderLayout.NORTH);
        add(panel, BorderLayout.CENTER);
        
        // Timer para actualizar HUD
        timerUI = new Timer(100, e -> actualizarHUD());
        timerUI.start();
        
        // ====================================================================
        // FIX: Asegurar que el panel tenga el foco
        // ====================================================================
        addWindowListener(new java.awt.event.WindowAdapter() {
            @Override
            public void windowOpened(java.awt.event.WindowEvent e) {
                panel.requestFocusInWindow();
            }
        });
    }
    
    private JLabel crearLabel(String texto) {
        JLabel lbl = new JLabel(texto);
        lbl.setFont(new Font("Arial", Font.BOLD, 16));
        lbl.setForeground(Color.WHITE);
        return lbl;
    }
    
    private void actualizarHUD() {
        int segundosRestantes = juego.getTiempoRestante() / 10;
        int minutos = segundosRestantes / 60;
        int segundos = segundosRestantes % 60;
        lblTiempo.setText(String.format("Tiempo: %d:%02d", minutos, segundos));
        lblPuntos.setText("Puntos: " + juego.getScore());
        lblFrutas.setText("Frutas: " + juego.getFrutasRecolectadas() + "/" + juego.getTotalFrutas());
        
        if (juego.isNivelCompletado()) {
            mostrarVictoria();
        }
        
        if (juego.isGameOver()) {
            mostrarGameOver();
        }
    }
    
    private void mostrarVictoria() {
        timerUI.stop();
        int opcion = JOptionPane.showConfirmDialog(this,
            "¡NIVEL COMPLETADO!\n" +
            "Puntuación: " + juego.getScore() + "\n" +
            "¿Continuar al siguiente nivel?",
            "¡Victoria!",
            JOptionPane.YES_NO_OPTION);
        
        if (opcion == JOptionPane.YES_OPTION) {
            JOptionPane.showMessageDialog(this, "Siguiente nivel no implementado aún");
        }
        salirJuego();
    }
    
    private void mostrarGameOver() {
        timerUI.stop();
        JOptionPane.showMessageDialog(this, 
            "GAME OVER\n" +
            "Puntuación final: " + juego.getScore(),
            "Fin del juego",
            JOptionPane.INFORMATION_MESSAGE);
        salirJuego();
    }
    
    private void salirJuego() {
        timerUI.stop();
        dispose();
        new VentanaInicio().setVisible(true);
    }
}