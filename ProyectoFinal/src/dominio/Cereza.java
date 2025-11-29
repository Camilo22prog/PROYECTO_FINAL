package dominio;

import java.awt.*;
import java.util.Random;

public class Cereza extends Fruta implements SpecialFruit {
    private int timer = 0;
    private Random random = new Random();
    private Nivel nivel; // Referencia al nivel para teletransportarse
    
    public Cereza() {
        super(150);
    }
    
    /**
     * Asigna el nivel para poder teletransportarse a posiciones válidas
     */
    public void setNivel(Nivel nivel) {
        this.nivel = nivel;
    }
    
    @Override
    public void tick() {
        // No se teletransporta si está congelada o ya fue recolectada
        if (congelada || recolectada) {
            return;
        }
        
        timer++;
        
        // Cada 200 ticks = 20 segundos (100ms por tick)
        if (timer >= 200) {
            teletransportar();
            timer = 0;
        }
    }
    
    /**
     * Teletransporta la cereza a una posición aleatoria libre
     */
    private void teletransportar() {
        if (nivel == null) return;
        
        // Intentar encontrar una posición libre
        for (int intento = 0; intento < 50; intento++) {
            int newRow = 2 + random.nextInt(nivel.getFilas() - 4);
            int newCol = 2 + random.nextInt(nivel.getColumnas() - 4);
            
            // Verificar que la posición esté libre
            if (nivel.puedeMoverse(newRow, newCol) && 
                !nivel.hayBloqueHielo(newRow, newCol) &&
                !nivel.hayEnemigoEn(newRow, newCol) &&
                !nivel.hayFrutaEn(newRow, newCol)) {
                
                this.row = newRow;
                this.col = newCol;
                System.out.println("🍒 Cereza se teletransportó a (" + newRow + ", " + newCol + ")");
                break;
            }
        }
    }
    
    @Override
    public void render(Graphics2D g, int offsetX, int offsetY, int cellSize) {
        int x = offsetX + col * cellSize + cellSize/4;
        int y = offsetY + row * cellSize + cellSize/4;
        
        // Dos cerezas rojas conectadas
        g.setColor(new Color(220, 20, 60));
        g.fillOval(x, y + 10, cellSize/3, cellSize/3);
        g.fillOval(x + 10, y + 10, cellSize/3, cellSize/3);
        
        // Tallos verdes
        g.setColor(new Color(34, 139, 34));
        g.setStroke(new BasicStroke(2));
        g.drawLine(x + cellSize/6, y + 10, x + cellSize/4, y);
        g.drawLine(x + cellSize/3, y + 10, x + cellSize/4, y);
        g.setStroke(new BasicStroke(1));
        
        // Efecto de advertencia cuando está por teletransportarse
        if (!congelada && timer > 150) {
            int alpha = 100 + (int)(Math.sin(timer * 0.3) * 100);
            g.setColor(new Color(255, 255, 0, alpha));
            g.fillOval(x - 4, y + 6, cellSize/3 + 8, cellSize/3 + 8);
            g.fillOval(x + 6, y + 6, cellSize/3 + 8, cellSize/3 + 8);
        }
        
        // Efecto de congelada
        if (congelada) {
            dibujarEfectoCongelada(g, x - 2, y + 6, cellSize);
        }
    }
}