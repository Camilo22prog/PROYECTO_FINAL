// ============================================================================
// BLOQUEHIELO.JAVA - VERSIÓN COMPLETA Y MEJORADA
// Copia este archivo completo y reemplaza tu BloqueHielo.java actual
// ============================================================================
package dominio;

import java.awt.*;

public class BloqueHielo extends Obstaculo {
    private boolean permanente;
    private int animFrame = 0;

    public BloqueHielo(int row, int col, boolean permanente) {
        super(row, col); 	
        this.permanente = permanente;
    }

    @Override
    public boolean bloquea() {
        return true;
    }

    public boolean esPermanente() {
        return permanente;
    }
    
    /**
     * Actualiza la animación del bloque
     */
    public void tick() {
        animFrame = (animFrame + 1) % 60;
    }

    @Override
    public void render(Graphics2D g, int offsetX, int offsetY, int cellSize) {
        int x = offsetX + col * cellSize;
        int y = offsetY + row * cellSize;

        // Color base según tipo de bloque
        Color colorBase = permanente ? 
            new Color(180, 220, 255) : 
            new Color(200, 235, 255);
        g.setColor(colorBase);
        g.fillRect(x + 2, y + 2, cellSize - 4, cellSize - 4);

        // ====================================================================
        // BRILLO ANIMADO
        // ====================================================================
        int alpha = 100 + (int)(Math.sin(animFrame * 0.1) * 50);
        g.setColor(new Color(255, 255, 255, alpha));
        g.fillRect(x + 5, y + 5, cellSize/3, cellSize/3);
        
        // Brillo secundario
        g.setColor(new Color(255, 255, 255, alpha/2));
        g.fillRect(x + cellSize/2, y + cellSize/2, cellSize/4, cellSize/4);

        // ====================================================================
        // PATRÓN DE CRISTALES DE HIELO
        // ====================================================================
        g.setColor(new Color(200, 230, 255, 100));
        g.drawLine(x + 5, y + 5, x + cellSize - 5, y + cellSize - 5);
        g.drawLine(x + cellSize - 5, y + 5, x + 5, y + cellSize - 5);
        
        // Líneas horizontales y verticales para efecto cristal
        g.drawLine(x + cellSize/2, y + 5, x + cellSize/2, y + cellSize - 5);
        g.drawLine(x + 5, y + cellSize/2, x + cellSize - 5, y + cellSize/2);
        
        // ====================================================================
        // BORDE
        // ====================================================================
        g.setColor(permanente ? 
            new Color(100, 150, 200) : 
            new Color(150, 190, 230));
        g.setStroke(new BasicStroke(permanente ? 3 : 2));
        g.drawRect(x + 2, y + 2, cellSize - 4, cellSize - 4);
        g.setStroke(new BasicStroke(1));
        
        // ====================================================================
        // INDICADOR DE BLOQUE PERMANENTE
        // ====================================================================
        if (permanente) {
            g.setColor(new Color(100, 150, 200, 150));
            g.fillOval(x + cellSize/2 - 4, y + cellSize/2 - 4, 8, 8);
            
            // Anillo externo
            g.setColor(new Color(100, 150, 200, 100));
            g.drawOval(x + cellSize/2 - 6, y + cellSize/2 - 6, 12, 12);
        }
    }
}