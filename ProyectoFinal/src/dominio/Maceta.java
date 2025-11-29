package dominio;

import java.awt.*;

public class Maceta extends Enemigo {
    private int animFrame = 0;
    
    public Maceta(int row, int col) {
        super(row, col);
    }
    
    @Override
    public void update(Nivel nivel, Player player) {
        moveTimer++;
        if (moveTimer < 6) return; // Más lento que Troll
        moveTimer = 0;
        
        animFrame = (animFrame + 1) % 12;
        
        // Perseguir al jugador
        int diffRow = player.getRow() - row;
        int diffCol = player.getCol() - col;
        
        int newRow = row;
        int newCol = col;
        
        // Moverse en el eje con mayor diferencia
        if (Math.abs(diffRow) > Math.abs(diffCol)) {
            newRow += (diffRow > 0) ? 1 : -1;
            direccion = (diffRow > 0) ? Direction.SOUTH : Direction.NORTH;
            
            if (!puedeMoverse(nivel, newRow, newCol)) {
                // Intentar moverse horizontalmente
                newRow = row;
                newCol += (diffCol > 0) ? 1 : -1;
                direccion = (diffCol > 0) ? Direction.EAST : Direction.WEST;
            }
        } else {
            newCol += (diffCol > 0) ? 1 : -1;
            direccion = (diffCol > 0) ? Direction.EAST : Direction.WEST;
            
            if (!puedeMoverse(nivel, newRow, newCol)) {
                // Intentar moverse verticalmente
                newCol = col;
                newRow += (diffRow > 0) ? 1 : -1;
                direccion = (diffRow > 0) ? Direction.SOUTH : Direction.NORTH;
            }
        }
        
        if (puedeMoverse(nivel, newRow, newCol)) {
            mover(newRow, newCol);
        }
    }
    
    @Override
    public void render(Graphics2D g, int offsetX, int offsetY, int cellSize) {
        int x = offsetX + col * cellSize + cellSize/6;
        int y = offsetY + row * cellSize + cellSize/6;
        
        // Maceta marrón
        g.setColor(new Color(139, 69, 19));
        int[] xPoints = {x, x + cellSize/2, x + 2*cellSize/3, x + cellSize/6};
        int[] yPoints = {y + cellSize/3, y + cellSize/3, y + 2*cellSize/3, y + 2*cellSize/3};
        g.fillPolygon(xPoints, yPoints, 4);
        
        // Planta verde agresiva
        g.setColor(new Color(34, 139, 34));
        g.fillOval(x + cellSize/6, y, cellSize/3, cellSize/3);
        
        // Cara enojada (animada)
        int bounce = (int)(Math.sin(animFrame * 0.5) * 2);
        
        g.setColor(Color.RED);
        g.fillOval(x + cellSize/4, y + cellSize/8 + bounce, 5, 5);
        g.fillOval(x + cellSize/3, y + cellSize/8 + bounce, 5, 5);
        
        // Boca enojada
        g.setStroke(new BasicStroke(2));
        g.drawArc(x + cellSize/6, y + cellSize/6 + bounce, cellSize/4, cellSize/8, 0, -180);
        g.setStroke(new BasicStroke(1));
        
        // Hojas moviéndose
        g.setColor(new Color(50, 150, 50));
        int leafOffset = (int)(Math.sin(animFrame * 0.3) * 3);
        g.fillOval(x + cellSize/6 - leafOffset, y - 3, 8, 8);
        g.fillOval(x + cellSize/3 + leafOffset, y - 3, 8, 8);
    }
}