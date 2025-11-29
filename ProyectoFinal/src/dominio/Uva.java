package dominio;

import java.awt.*;

public class Uva extends Fruta {
    public Uva() {
        super(50);
    }
    
    @Override
    public void render(Graphics2D g, int offsetX, int offsetY, int cellSize) {
        int x = offsetX + col * cellSize + cellSize/4;
        int y = offsetY + row * cellSize + cellSize/4;
        
        // Racimo de uvas (3 uvas)
        g.setColor(new Color(138, 43, 226));
        g.fillOval(x, y, cellSize/4, cellSize/4);
        g.fillOval(x + 8, y, cellSize/4, cellSize/4);
        g.fillOval(x + 4, y + 8, cellSize/4, cellSize/4);
        
        // Brillo en cada uva
        g.setColor(new Color(180, 100, 255, 150));
        g.fillOval(x + 2, y + 2, 4, 4);
        g.fillOval(x + 10, y + 2, 4, 4);
        g.fillOval(x + 6, y + 10, 4, 4);
        
        // Borde
        g.setColor(new Color(100, 30, 180));
        g.drawOval(x, y, cellSize/4, cellSize/4);
        g.drawOval(x + 8, y, cellSize/4, cellSize/4);
        g.drawOval(x + 4, y + 8, cellSize/4, cellSize/4);
        
        // Efecto de congelada
        if (congelada) {
            dibujarEfectoCongelada(g, x - 2, y - 2, cellSize);
        }
    }
}