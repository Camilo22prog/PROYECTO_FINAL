package dominio;

import java.awt.*;

public class Cactus extends Fruta implements SpecialFruit {
    private int timer = 0;
    private boolean conPuas = false;
    private int animacionPuas = 0;
    
    public Cactus() {
        super(250);
    }
    
    @Override
    public void tick() {
        // No cambia de estado si está congelado
        if (congelada) {
            return;
        }
        
        // Si ya fue recolectado, no hacer nada
        if (recolectada) {
            return;
        }
        
        timer++;
        
        // Cada 300 ticks = 30 segundos
        if (timer >= 300) {
            conPuas = !conPuas; // Alternar entre con púas y sin púas
            timer = 0;
            
            if (conPuas) {
                System.out.println("🌵 ¡Cactus creció púas! PELIGRO");
            } else {
                System.out.println("🌵 Cactus perdió las púas, seguro para recolectar");
            }
        }
        
        // Animación de púas
        if (conPuas) {
            animacionPuas = (animacionPuas + 1) % 20;
        }
    }
    
    /**
     * Verifica si el cactus puede ser recolectado (sin púas)
     */
    public boolean puedeRecolectar() {
        return !conPuas;
    }
    
    /**
     * Verifica si el cactus es peligroso (tiene púas)
     */
    public boolean esPeligroso() {
        return conPuas && !congelada;
    }
    
    /**
     * Retorna el tiempo restante hasta cambiar de estado
     */
    public int getTiempoRestante() {
        return 300 - timer;
    }
    
    @Override
    public void render(Graphics2D g, int offsetX, int offsetY, int cellSize) {
        int x = offsetX + col * cellSize + cellSize/4;
        int y = offsetY + row * cellSize + cellSize/6;
        
        // Cuerpo del cactus (verde)
        g.setColor(new Color(76, 187, 23));
        g.fillRoundRect(x, y + 10, cellSize/3, cellSize/2, 5, 5);
        
        // Brazos del cactus
        g.fillOval(x - 5, y + 15, 15, 15);
        g.fillOval(x + 5, y + 20, 15, 15);
        
        // Púas si está activo y no congelado
        if (conPuas && !congelada) {
            g.setColor(new Color(255, 0, 0));
            g.setStroke(new BasicStroke(2));
            
            // Púas en el cuerpo
            for (int i = 0; i < 6; i++) {
                int px = x + cellSize/6 + (i % 2) * 5;
                int py = y + 15 + (i * 8);
                
                // Animación de púas (pequeño movimiento)
                int offset = (int)(Math.sin(animacionPuas * 0.3 + i) * 2);
                
                g.drawLine(px, py, px - 5 + offset, py - 3);
                g.drawLine(px, py, px + 5 + offset, py - 3);
            }
            g.setStroke(new BasicStroke(1));
            
            // Advertencia visual (aura roja pulsante)
            int alpha = 80 + (int)(Math.sin(animacionPuas * 0.3) * 50);
            g.setColor(new Color(255, 0, 0, alpha));
            g.fillOval(x - 8, y + 5, cellSize/2, 3*cellSize/4);
        } else if (!conPuas && !congelada) {
            // Flores cuando está seguro
            g.setColor(new Color(255, 200, 200));
            g.fillOval(x + cellSize/6 - 2, y + 5, 6, 6);
            g.fillOval(x + cellSize/6 + 3, y + 5, 6, 6);
        }
        
        // Maceta
        g.setColor(new Color(139, 69, 19));
        g.fillRect(x - 2, y + cellSize/2, cellSize/3 + 4, 10);
        
        // Borde de la maceta
        g.setColor(new Color(101, 50, 15));
        g.drawRect(x - 2, y + cellSize/2, cellSize/3 + 4, 10);
        
        // Efecto de congelada
        if (congelada) {
            dibujarEfectoCongelada(g, x - 4, y + 8, cellSize);
        }
        
        // Indicador de tiempo (pequeña barra)
        if (!congelada && !recolectada) {
            int barWidth = cellSize/3;
            int progress = (timer * barWidth) / 300;
            
            // Fondo de barra
            g.setColor(new Color(100, 100, 100, 150));
            g.fillRect(x, y + 3, barWidth, 3);
            
            // Progreso
            Color barColor = conPuas ? new Color(255, 0, 0) : new Color(0, 255, 0);
            g.setColor(barColor);
            g.fillRect(x, y + 3, progress, 3);
        }
    }
}