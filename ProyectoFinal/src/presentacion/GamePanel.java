package presentacion;

import dominio.*;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class GamePanel extends JPanel {
    private Juego juego;
    private Timer gameTimer;
    private static final int CELL_SIZE = 48;
    private JuegoWindow ventana;
    
    // Efectos visuales
    private boolean mostrarEfectoCrear = false;
    private boolean mostrarEfectoRomper = false;
    private int timerEfecto = 0;
    
    public GamePanel(Juego juego, JuegoWindow ventana) {
        this.juego = juego;
        this.ventana = ventana;
        setBackground(new Color(220, 240, 255));
        setFocusable(true);
        setPreferredSize(new Dimension(960, 640));
        
        configurarControles();
        
        gameTimer = new Timer(100, e -> {
            if (!juego.isPaused()) {
                juego.update();
                
                if (timerEfecto > 0) {
                    timerEfecto--;
                    if (timerEfecto == 0) {
                        mostrarEfectoCrear = false;
                        mostrarEfectoRomper = false;
                    }
                }
                
                repaint();
            }
        });
        gameTimer.start();
    }
    
    private void configurarControles() {
        InputMap im = getInputMap(WHEN_IN_FOCUSED_WINDOW);
        ActionMap am = getActionMap();
        
        // Movimiento con flechas
        im.put(KeyStroke.getKeyStroke("UP"), "arriba");
        am.put("arriba", new AbstractAction() {
            public void actionPerformed(ActionEvent e) { 
                juego.moverJugador(Direction.NORTH); 
            }
        });
        
        im.put(KeyStroke.getKeyStroke("DOWN"), "abajo");
        am.put("abajo", new AbstractAction() {
            public void actionPerformed(ActionEvent e) { 
                juego.moverJugador(Direction.SOUTH); 
            }
        });
        
        im.put(KeyStroke.getKeyStroke("LEFT"), "izquierda");
        am.put("izquierda", new AbstractAction() {
            public void actionPerformed(ActionEvent e) { 
                juego.moverJugador(Direction.WEST); 
            }
        });
        
        im.put(KeyStroke.getKeyStroke("RIGHT"), "derecha");
        am.put("derecha", new AbstractAction() {
            public void actionPerformed(ActionEvent e) { 
                juego.moverJugador(Direction.EAST); 
            }
        });
        
        // ====================================================================
        // ⭐ NUEVO: SOLO SPACE - Crea o Rompe inteligentemente
        // ====================================================================
        im.put(KeyStroke.getKeyStroke("SPACE"), "accionHielo");
        am.put("accionHielo", new AbstractAction() {
            public void actionPerformed(ActionEvent e) { 
                // Determinar si va a crear o romper
                Player p = juego.getPlayer();
                Nivel n = juego.getNivel();
                Direction dir = p.getDireccion();
                
                int targetRow = p.getRow() + dir.getDy();
                int targetCol = p.getCol() + dir.getDx();
                
                boolean hayBloque = n.hayBloqueHielo(targetRow, targetCol);
                
                // Ejecutar acción
                juego.accionBloqueHielo();
                
                // Mostrar efecto apropiado
                if (hayBloque) {
                    mostrarEfectoRomper = true;
                } else {
                    mostrarEfectoCrear = true;
                }
                timerEfecto = 3;
                repaint();
            }
        });
        
        // Pausa
        im.put(KeyStroke.getKeyStroke('P'), "pausa");
        am.put("pausa", new AbstractAction() {
            public void actionPerformed(ActionEvent e) { 
                juego.togglePause(); 
            }
        });
    }
    
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D)g;
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, 
                           RenderingHints.VALUE_ANTIALIAS_ON);
        
        Nivel nivel = juego.getNivel();
        int offsetX = 50;
        int offsetY = 40;
        
        // Dibujar fondo del mapa
        for (int row = 0; row < nivel.getFilas(); row++) {
            for (int col = 0; col < nivel.getColumnas(); col++) {
                int x = offsetX + col * CELL_SIZE;
                int y = offsetY + row * CELL_SIZE;
                
                g2.setColor(new Color(180, 220, 255));
                g2.fillRect(x, y, CELL_SIZE, CELL_SIZE);
                g2.setColor(new Color(150, 200, 240));
                g2.drawRect(x, y, CELL_SIZE, CELL_SIZE);
            }
        }
        
        // Dibujar obstáculos
        for (Obstaculo obs : nivel.getObstaculos()) {
            obs.render(g2, offsetX, offsetY, CELL_SIZE);
        }
        
        // Dibujar frutas
        for (Fruta fruta : nivel.getFrutas()) {
            if (!fruta.isRecolectada()) {
                fruta.render(g2, offsetX, offsetY, CELL_SIZE);
            }
        }
        
        // Dibujar enemigos
        for (Enemigo enemigo : nivel.getEnemigos()) {
            enemigo.render(g2, offsetX, offsetY, CELL_SIZE);
        }
        
        // Dibujar jugador
        juego.getPlayer().render(g2, offsetX, offsetY, CELL_SIZE);
        
        // Efecto visual al crear bloques
        if (mostrarEfectoCrear) {
            Player p = juego.getPlayer();
            int px = offsetX + p.getCol() * CELL_SIZE + CELL_SIZE/2;
            int py = offsetY + p.getRow() * CELL_SIZE + CELL_SIZE/2;
            
            g2.setColor(new Color(100, 200, 255, 150));
            g2.fillOval(px - 20, py - 20, 40, 40);
            
            g2.setColor(new Color(150, 220, 255));
            g2.setStroke(new BasicStroke(3));
            g2.drawOval(px - 25, py - 25, 50, 50);
            g2.setStroke(new BasicStroke(1));
            
            g2.setColor(new Color(200, 230, 255));
            for (int i = 0; i < 8; i++) {
                double angle = i * Math.PI / 4;
                int particleX = px + (int)(Math.cos(angle) * 30);
                int particleY = py + (int)(Math.sin(angle) * 30);
                g2.fillOval(particleX - 2, particleY - 2, 4, 4);
            }
        }
        
        // Efecto visual al romper bloques
        if (mostrarEfectoRomper) {
            Player p = juego.getPlayer();
            int px = offsetX + p.getCol() * CELL_SIZE + CELL_SIZE/2;
            int py = offsetY + p.getRow() * CELL_SIZE + CELL_SIZE/2;
            
            g2.setColor(new Color(255, 200, 100, 150));
            g2.fillOval(px - 15, py - 15, 30, 30);
            
            g2.setColor(new Color(255, 150, 0));
            g2.setStroke(new BasicStroke(2));
            for (int i = 0; i < 8; i++) {
                double angle = i * Math.PI / 4;
                int x1 = px + (int)(Math.cos(angle) * 15);
                int y1 = py + (int)(Math.sin(angle) * 15);
                int x2 = px + (int)(Math.cos(angle) * 25);
                int y2 = py + (int)(Math.sin(angle) * 25);
                g2.drawLine(x1, y1, x2, y2);
            }
            g2.setStroke(new BasicStroke(1));
            
            g2.setColor(new Color(255, 200, 0));
            for (int i = 0; i < 12; i++) {
                double angle = i * Math.PI / 6;
                int sparkX = px + (int)(Math.cos(angle) * 20);
                int sparkY = py + (int)(Math.sin(angle) * 20);
                g2.fillOval(sparkX - 2, sparkY - 2, 4, 4);
            }
        }
        
        // Mensaje de pausa
        if (juego.isPaused()) {
            g2.setColor(new Color(0, 0, 0, 180));
            g2.fillRect(0, 0, getWidth(), getHeight());
            
            g2.setColor(Color.WHITE);
            g2.setFont(new Font("Arial", Font.BOLD, 48));
            String msg = "PAUSA";
            FontMetrics fm = g2.getFontMetrics();
            int x = (getWidth() - fm.stringWidth(msg)) / 2;
            int y = getHeight() / 2;
            g2.drawString(msg, x, y);
            
            g2.setFont(new Font("Arial", Font.PLAIN, 20));
            String msg2 = "Presiona P para continuar";
            x = (getWidth() - g2.getFontMetrics().stringWidth(msg2)) / 2;
            g2.drawString(msg2, x, y + 40);
        }
        
        // ====================================================================
        // ⭐ AYUDA ACTUALIZADA
        // ====================================================================
        g2.setColor(new Color(255, 255, 255, 200));
        g2.setFont(new Font("Arial", Font.PLAIN, 12));
        String ayuda = "ESPACIO: Crear/Romper hielo  |  P: Pausa  |  Flechas: Mover";
        g2.drawString(ayuda, 10, getHeight() - 10);
        
        // Estadísticas
        Player p = juego.getPlayer();
        g2.setFont(new Font("Arial", Font.BOLD, 11));
        g2.drawString("Bloques creados: " + p.getBloquesCreados(), getWidth() - 180, 20);
        g2.drawString("Bloques destruidos: " + p.getBloquesDestruidos(), getWidth() - 180, 35);
        g2.drawString("Bloques en mapa: " + nivel.getObstaculos().size(), getWidth() - 180, 50);
    }
}