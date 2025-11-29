package dominio;

import java.awt.*;
import java.io.Serializable;

public class Player implements Serializable {
    private static final long serialVersionUID = 1L;
    
    private HeladoType tipo;
    private String nombre;
    private int row, col;
    private Direction direccion;
    private int animFrame = 0;
    
    private int bloquesCreados = 0;
    private int bloquesDestruidos = 0;
    
    public Player(HeladoType tipo, String nombre, int row, int col) {
        this.tipo = tipo;
        this.nombre = nombre;
        this.row = row;
        this.col = col;
        this.direccion = Direction.SOUTH;
    }
    
    public void mover(int newRow, int newCol) {
        this.row = newRow;
        this.col = newCol;
        animFrame = (animFrame + 1) % 4;
    }
    
    public void setDirection(Direction dir) {
        this.direccion = dir;
    }
    
    // ========================================================================
    // ACCIÓN INTELIGENTE DE HIELO (crear o romper según contexto)
    // ========================================================================
    public void accionBloqueHielo(Nivel nivel) {
        int targetRow = row + direccion.getDy();
        int targetCol = col + direccion.getDx();
        
        if (targetRow < 0 || targetRow >= nivel.getFilas() ||
            targetCol < 0 || targetCol >= nivel.getColumnas()) {
            return;
        }
        
        // Si hay bloque en la dirección que mira: ROMPER
        if (nivel.hayBloqueHielo(targetRow, targetCol)) {
            romperBloqueHielo(nivel);
        } 
        // Si no hay bloque: CREAR
        else {
            crearBloqueHielo(nivel);
        }
    }
    
    // ========================================================================
    // FIX: CREAR BLOQUES - Detenerse antes de enemigos
    // ========================================================================
    private void crearBloqueHielo(Nivel nivel) {
        int targetRow = row + direccion.getDy();
        int targetCol = col + direccion.getDx();
        
        int bloquesEnLinea = 0;
        final int MAX_BLOQUES_LINEA = 15;
        
        while (nivel.puedeMoverse(targetRow, targetCol) && 
               bloquesEnLinea < MAX_BLOQUES_LINEA) {
            
            // ================================================================
            // ⭐ FIX: Verificar si hay un enemigo en la posición
            // ================================================================
            if (nivel.hayEnemigoEn(targetRow, targetCol)) {
                // Detener la creación ANTES del enemigo
                break;
            }
            
            nivel.agregarBloqueHielo(targetRow, targetCol);
            this.bloquesCreados++;
            bloquesEnLinea++;
            
            targetRow += direccion.getDy();
            targetCol += direccion.getDx();
            
            if (targetRow < 0 || targetRow >= nivel.getFilas() ||
                targetCol < 0 || targetCol >= nivel.getColumnas()) {
                break;
            }
        }
    }
    
    // ========================================================================
    // ROMPER BLOQUES DE HIELO
    // ========================================================================
    private void romperBloqueHielo(Nivel nivel) {
        int targetRow = row + direccion.getDy();
        int targetCol = col + direccion.getDx();
        
        while (targetRow >= 0 && targetRow < nivel.getFilas() && 
               targetCol >= 0 && targetCol < nivel.getColumnas()) {
            
            boolean bloqueRemovido = nivel.removerBloqueHielo(targetRow, targetCol);
            
            if (bloqueRemovido) {
                this.bloquesDestruidos++;
            } else {
                break;
            }
            
            targetRow += direccion.getDy();
            targetCol += direccion.getDx();
        }
    }
    
    public boolean collidesWith(Enemigo enemigo) {
        return this.row == enemigo.getRow() && this.col == enemigo.getCol();
    }
    
    public boolean collidesWith(Fruta fruta) {
        return this.row == fruta.getRow() && this.col == fruta.getCol();
    }
    
    public void render(Graphics2D g, int offsetX, int offsetY, int cellSize) {
        int x = offsetX + col * cellSize;
        int y = offsetY + row * cellSize;
        
        Color color = getColorForType();
        
        // Cuerpo del helado (forma de cono)
        g.setColor(new Color(238, 213, 183));
        int[] xPoints = {x + cellSize/2, x + cellSize/4, x + 3*cellSize/4};
        int[] yPoints = {y + cellSize - 5, y + cellSize/2, y + cellSize/2};
        g.fillPolygon(xPoints, yPoints, 3);
        
        // Bola de helado
        g.setColor(color);
        g.fillOval(x + cellSize/6, y + 5, 2*cellSize/3, 2*cellSize/3);
        
        // Ojos
        g.setColor(Color.BLACK);
        int eyeY = y + cellSize/3;
        g.fillOval(x + cellSize/3, eyeY, 6, 6);
        g.fillOval(x + cellSize/2, eyeY, 6, 6);
        
        // Sonrisa
        g.drawArc(x + cellSize/3, eyeY + 5, cellSize/3, cellSize/6, 0, -180);
        
        // Indicador de dirección (flecha)
        g.setColor(new Color(255, 255, 255, 200));
        int dirX = x + cellSize/2;
        int dirY = y + cellSize/2;
        
        switch (direccion) {
            case NORTH:
                g.fillPolygon(
                    new int[]{dirX, dirX - 4, dirX + 4},
                    new int[]{dirY - 10, dirY - 6, dirY - 6},
                    3
                );
                break;
            case SOUTH:
                g.fillPolygon(
                    new int[]{dirX, dirX - 4, dirX + 4},
                    new int[]{dirY + 10, dirY + 6, dirY + 6},
                    3
                );
                break;
            case EAST:
                g.fillPolygon(
                    new int[]{dirX + 10, dirX + 6, dirX + 6},
                    new int[]{dirY, dirY - 4, dirY + 4},
                    3
                );
                break;
            case WEST:
                g.fillPolygon(
                    new int[]{dirX - 10, dirX - 6, dirX - 6},
                    new int[]{dirY, dirY - 4, dirY + 4},
                    3
                );
                break;
        }
    }
    
    private Color getColorForType() {
        switch (tipo) {
            case VAINILLA: return new Color(255, 250, 240);
            case FRESA: return new Color(255, 182, 193);
            case CHOCOLATE: return new Color(139, 90, 43);
            default: return Color.WHITE;
        }
    }
    
    // Getters
    public int getRow() { return row; }
    public int getCol() { return col; }
    public HeladoType getTipo() { return tipo; }
    public String getNombre() { return nombre; }
    public Direction getDireccion() { return direccion; }
    public int getBloquesCreados() { return bloquesCreados; }
    public int getBloquesDestruidos() { return bloquesDestruidos; }
}