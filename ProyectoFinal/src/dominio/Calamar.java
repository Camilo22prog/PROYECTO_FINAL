package dominio;

import java.awt.*;

public class Calamar extends Enemigo {
    private boolean rompiendo = false;
    private int timerRomper = 0;
    private int animFrame = 0;
    private int targetRow, targetCol;
    
    public Calamar(int row, int col) {
        super(row, col);
    }
    
    @Override
    public void update(Nivel nivel, Player player) {
        animFrame = (animFrame + 1) % 16;
        
        // Si está rompiendo un bloque
        if (rompiendo) {
            timerRomper++;
            if (timerRomper >= 15) { // Tarda 1.5 segundos en romper
                // Romper el bloque
                nivel.removerBloqueHielo(targetRow, targetCol);
                rompiendo = false;
                timerRomper = 0;
                System.out.println("🦑 Calamar rompió bloque en (" + targetRow + ", " + targetCol + ")");
            }
            return; // No se mueve mientras rompe
        }
        
        moveTimer++;
        if (moveTimer < 5) return; // Velocidad moderada
        moveTimer = 0;
        
        // Perseguir al jugador
        int diffRow = player.getRow() - row;
        int diffCol = player.getCol() - col;
        
        int newRow = row;
        int newCol = col;
        
        // Moverse hacia el jugador
        if (Math.abs(diffRow) > Math.abs(diffCol)) {
            newRow += (diffRow > 0) ? 1 : -1;
            direccion = (diffRow > 0) ? Direction.SOUTH : Direction.NORTH;
        } else if (diffCol != 0) {
            newCol += (diffCol > 0) ? 1 : -1;
            direccion = (diffCol > 0) ? Direction.EAST : Direction.WEST;
        }
        
        // Verificar si puede moverse
        if (puedeMoverse(nivel, newRow, newCol)) {
            mover(newRow, newCol);
        } else if (nivel.hayBloqueHielo(newRow, newCol)) {
            // Hay un bloque de hielo, empezar a romperlo
            rompiendo = true;
            timerRomper = 0;
            targetRow = newRow;
            targetCol = newCol;
            System.out.println("🦑 Calamar empezó a romper bloque en (" + newRow + ", " + newCol + ")");
        }
    }
    
    @Override
    public void render(Graphics2D g, int offsetX, int offsetY, int cellSize) {
        int x = offsetX + col * cellSize;
        int y = offsetY + row * cellSize;
        
        // Cuerpo naranja del calamar
        g.setColor(new Color(255, 140, 0));
        g.fillOval(x + 8, y + 8, cellSize - 16, cellSize - 16);
        
        // Tentáculos animados
        g.setColor(new Color(255, 100, 0));
        g.setStroke(new BasicStroke(3));
        
        for (int i = 0; i < 8; i++) {
            double angle = i * Math.PI / 4 + animFrame * 0.1;
            double length = 12 + Math.sin(animFrame * 0.2 + i) * 3;
            int tx = x + cellSize/2 + (int)(Math.cos(angle) * length);
            int ty = y + cellSize/2 + (int)(Math.sin(angle) * length);
            g.drawLine(x + cellSize/2, y + cellSize/2, tx, ty);
        }
        g.setStroke(new BasicStroke(1));
        
        // Ojos
        g.setColor(Color.WHITE);
        g.fillOval(x + cellSize/3, y + cellSize/3, 8, 8);
        g.fillOval(x + cellSize/2 + 2, y + cellSize/3, 8, 8);
        g.setColor(Color.BLACK);
        g.fillOval(x + cellSize/3 + 2, y + cellSize/3 + 2, 4, 4);
        g.fillOval(x + cellSize/2 + 4, y + cellSize/3 + 2, 4, 4);
        
        // Si está rompiendo, mostrar efecto visual
        if (rompiendo) {
            // Martillo o efecto de romper
            g.setColor(new Color(255, 255, 0, 150));
            int effectX = x + cellSize/2 + direccion.getDx() * 15;
            int effectY = y + cellSize/2 + direccion.getDy() * 15;
            g.fillOval(effectX - 8, effectY - 8, 16, 16);
            
            // Líneas de impacto
            g.setColor(new Color(255, 200, 0));
            for (int i = 0; i < 4; i++) {
                double angle = i * Math.PI / 2 + animFrame * 0.3;
                int lx = effectX + (int)(Math.cos(angle) * 12);
                int ly = effectY + (int)(Math.sin(angle) * 12);
                g.drawLine(effectX, effectY, lx, ly);
            }
        }
    }
}