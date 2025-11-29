package dominio;

import java.awt.*;

public class Platano extends Fruta {
    public Platano() {
        super(100);
    }
    
    @Override
    public void render(Graphics2D g, int offsetX, int offsetY, int cellSize) {
        int x = offsetX + col * cellSize + cellSize/4;
        int y = offsetY + row * cellSize + cellSize/4;
        
        // Forma de plátano (arco amarillo)
        g.setColor(new Color(255, 225, 53));
        g.fillArc(x, y, cellSize/2, cellSize/2, 45, 270);
        
        // Brillo en el plátano
        g.setColor(new Color(255, 245, 150, 150));
        g.fillArc(x + 4, y + 4, cellSize/4, cellSize/4, 45, 180);
        
        // Borde marrón
        g.setColor(new Color(139, 90, 0));
        g.setStroke(new BasicStroke(2));
        g.drawArc(x, y, cellSize/2, cellSize/2, 45, 270);
        g.setStroke(new BasicStroke(1));
        
        // Punta marrón del plátano
        g.fillOval(x + cellSize/2 - 3, y + 2, 6, 6);
        
        // Efecto de congelada
        if (congelada) {
            dibujarEfectoCongelada(g, x, y, cellSize);
        }
    }
}