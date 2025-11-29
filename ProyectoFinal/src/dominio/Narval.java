package dominio;

import java.awt.*;
import java.util.Random;

public class Narval extends Enemigo {
    private boolean embistiendo = false;
    private int velocidadEmbestida = 0;
    private int animFrame = 0;
    private Random random = new Random();
    private Direction direccionPatrulla;
    
    public Narval(int row, int col) {
        super(row, col);
        // Dirección de patrulla inicial aleatoria
        Direction[] dirs = {Direction.NORTH, Direction.SOUTH, Direction.EAST, Direction.WEST};
        direccionPatrulla = dirs[random.nextInt(dirs.length)];
        direccion = direccionPatrulla;
    }
    
    @Override
    public void update(Nivel nivel, Player player) {
        animFrame = (animFrame + 1) % 20;
        
        // Modo embestida
        if (embistiendo) {
            // Moverse muy rápido en la dirección de embestida
            int newRow = row + direccion.getDy();
            int newCol = col + direccion.getDx();
            
            if (puedeMoverse(nivel, newRow, newCol)) {
                mover(newRow, newCol);
                velocidadEmbestida--;
            } else {
                // Romper bloque si es hielo
                if (nivel.hayBloqueHielo(newRow, newCol)) {
                    nivel.removerBloqueHielo(newRow, newCol);
                    System.out.println("🦄 Narval rompió bloque durante embestida");
                }
                // Continuar embestida incluso después de romper
                velocidadEmbestida -= 2;
            }
            
            if (velocidadEmbestida <= 0) {
                embistiendo = false;
                direccion = direccionPatrulla;
                System.out.println("🦄 Narval terminó embestida");
            }
            return;
        }
        
        // Modo patrulla normal
        moveTimer++;
        if (moveTimer < 8) return; // Lento en patrulla
        moveTimer = 0;
        
        // Verificar si el jugador está alineado (misma fila o columna)
        boolean alineadoHorizontal = (player.getRow() == row);
        boolean alineadoVertical = (player.getCol() == col);
        
        if (alineadoHorizontal || alineadoVertical) {
            // ¡EMBESTIR!
            embistiendo = true;
            velocidadEmbestida = 15; // Embiste 15 casillas
            
            if (alineadoHorizontal) {
                direccion = (player.getCol() > col) ? Direction.EAST : Direction.WEST;
            } else {
                direccion = (player.getRow() > row) ? Direction.SOUTH : Direction.NORTH;
            }
            
            System.out.println("🦄 ¡Narval inició embestida hacia " + direccion + "!");
            return;
        }
        
        // Patrulla normal
        int newRow = row + direccionPatrulla.getDy();
        int newCol = col + direccionPatrulla.getDx();
        
        if (puedeMoverse(nivel, newRow, newCol)) {
            mover(newRow, newCol);
        } else {
            // Cambiar dirección de patrulla
            Direction[] opciones;
            if (direccionPatrulla == Direction.NORTH || direccionPatrulla == Direction.SOUTH) {
                opciones = new Direction[]{Direction.EAST, Direction.WEST};
            } else {
                opciones = new Direction[]{Direction.NORTH, Direction.SOUTH};
            }
            direccionPatrulla = opciones[random.nextInt(opciones.length)];
            direccion = direccionPatrulla;
        }
    }
    
    @Override
    public void render(Graphics2D g, int offsetX, int offsetY, int cellSize) {
        int x = offsetX + col * cellSize;
        int y = offsetY + row * cellSize;
        
        // Color según estado
        Color colorCuerpo = embistiendo ? 
            new Color(200, 100, 255) :  // Púrpura cuando embiste
            new Color(100, 150, 200);   // Azul grisáceo normal
        
        // Cuerpo del narval
        g.setColor(colorCuerpo);
        g.fillOval(x + 5, y + 10, cellSize - 10, cellSize - 20);
        
        // Cuerno (apunta en la dirección de movimiento)
        g.setColor(embistiendo ? Color.YELLOW : Color.WHITE);
        g.setStroke(new BasicStroke(embistiendo ? 4 : 3));
        
        int cuernoBaseX = x + cellSize/2;
        int cuernoBaseY = y + cellSize/2;
        int cuernoPuntaX = cuernoBaseX + direccion.getDx() * 18;
        int cuernoPuntaY = cuernoBaseY + direccion.getDy() * 18;
        
        g.drawLine(cuernoBaseX, cuernoBaseY, cuernoPuntaX, cuernoPuntaY);
        g.setStroke(new BasicStroke(1));
        
        // Punta del cuerno
        g.fillOval(cuernoPuntaX - 3, cuernoPuntaY - 3, 6, 6);
        
        // Ojo
        g.setColor(Color.BLACK);
        g.fillOval(x + cellSize/2 - 2, y + cellSize/2 - 2, 6, 6);
        
        // Aletas (animadas)
        g.setColor(new Color(80, 120, 160));
        int aletaOffset = (int)(Math.sin(animFrame * 0.3) * 3);
        g.fillOval(x + cellSize/4, y + cellSize/2 + aletaOffset, 10, 6);
        g.fillOval(x + cellSize - cellSize/4 - 10, y + cellSize/2 - aletaOffset, 10, 6);
        
        // Efecto visual de embestida
        if (embistiendo) {
            // Estela de movimiento
            g.setColor(new Color(255, 255, 255, 100));
            for (int i = 1; i <= 3; i++) {
                int trailX = x + cellSize/2 - direccion.getDx() * i * 10;
                int trailY = y + cellSize/2 - direccion.getDy() * i * 10;
                int alpha = 100 - i * 30;
                g.setColor(new Color(200, 150, 255, Math.max(0, alpha)));
                g.fillOval(trailX - 8, trailY - 8, 16, 16);
            }
        }
    }
}