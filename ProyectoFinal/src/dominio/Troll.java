package dominio;

import java.awt.*;
import java.util.Random;

public class Troll extends Enemigo {
    private Random random = new Random();
    private int animFrame = 0;
    
    public Troll(int row, int col) {
        super(row, col);
        // Dirección inicial aleatoria
        Direction[] dirs = Direction.values();
        this.direccion = dirs[random.nextInt(dirs.length)];
    }
    
    @Override
    public void update(Nivel nivel, Player player) {
        moveTimer++;
        if (moveTimer < 4) return; // Velocidad moderada
        moveTimer = 0;
        
        animFrame = (animFrame + 1) % 8;
        
        // Intentar moverse en la dirección actual
        int newRow = row + direccion.getDy();
        int newCol = col + direccion.getDx();
        
        // Si puede moverse, avanzar
        if (puedeMoverse(nivel, newRow, newCol)) {
            mover(newRow, newCol);
        } else {
            // Rebotó con algo, cambiar dirección
            cambiarDireccionAleatoria();
        }
    }
    
    /**
     * Cambia a una dirección perpendicular
     */
    private void cambiarDireccionAleatoria() {
        Direction[] opciones;
        
        // Si iba vertical, ahora va horizontal
        if (direccion == Direction.NORTH || direccion == Direction.SOUTH) {
            opciones = new Direction[]{Direction.EAST, Direction.WEST};
        } 
        // Si iba horizontal, ahora va vertical
        else {
            opciones = new Direction[]{Direction.NORTH, Direction.SOUTH};
        }
        
        direccion = opciones[random.nextInt(opciones.length)];
    }
    
    @Override
    public void render(Graphics2D g, int offsetX, int offsetY, int cellSize) {
        int x = offsetX + col * cellSize;
        int y = offsetY + row * cellSize;
        
        // Cuerpo redondo peludo (rojo)
        g.setColor(new Color(255, 100, 100));
        g.fillOval(x + 6, y + 6, cellSize - 12, cellSize - 12);
        
        // "Pelos" alrededor (animados)
        g.setColor(new Color(200, 50, 50));
        for (int i = 0; i < 8; i++) {
            double angle = i * Math.PI / 4 + animFrame * 0.1;
            int px = x + cellSize/2 + (int)(Math.cos(angle) * (cellSize/2 - 3));
            int py = y + cellSize/2 + (int)(Math.sin(angle) * (cellSize/2 - 3));
            g.drawLine(x + cellSize/2, y + cellSize/2, px, py);
        }
        
        // Ojos (miran en la dirección de movimiento)
        g.setColor(Color.WHITE);
        int eyeOffsetX = direccion.getDx() * 3;
        int eyeOffsetY = direccion.getDy() * 3;
        
        g.fillOval(x + cellSize/3 + eyeOffsetX, y + cellSize/3 + eyeOffsetY, 8, 8);
        g.fillOval(x + cellSize/2 + 2 + eyeOffsetX, y + cellSize/3 + eyeOffsetY, 8, 8);
        
        // Pupilas
        g.setColor(Color.BLACK);
        g.fillOval(x + cellSize/3 + 2 + eyeOffsetX, y + cellSize/3 + 2 + eyeOffsetY, 4, 4);
        g.fillOval(x + cellSize/2 + 4 + eyeOffsetX, y + cellSize/3 + 2 + eyeOffsetY, 4, 4);
    }
}