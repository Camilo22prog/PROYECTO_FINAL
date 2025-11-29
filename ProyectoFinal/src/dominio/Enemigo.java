package dominio;

import java.awt.*;
import java.io.Serializable;

public abstract class Enemigo implements Serializable {
    private static final long serialVersionUID = 1L;
    
    protected int row, col;
    protected Direction direccion;
    protected int velocidad = 1;
    protected int moveTimer = 0;
    
    // ⭐ NUEVO: Posición pixel-perfect para colisiones
    protected double exactRow, exactCol;
    
    public Enemigo(int row, int col) {
        this.row = row;
        this.col = col;
        this.exactRow = row;
        this.exactCol = col;
        this.direccion = Direction.SOUTH;
    }
    
    public abstract void update(Nivel nivel, Player player);
    public abstract void render(Graphics2D g, int offsetX, int offsetY, int cellSize);
    
    protected boolean puedeMoverse(Nivel nivel, int newRow, int newCol) {
        return nivel.puedeMoverse(newRow, newCol);
    }
    
    protected void mover(int newRow, int newCol) {
        this.row = newRow;
        this.col = newCol;
        this.exactRow = newRow;
        this.exactCol = newCol;
    }
    
    // ========================================================================
    // ⭐ NUEVO: Sistema de colisión mejorado con hitboxes
    // ========================================================================
    
    /**
     * Verifica colisión con el jugador usando hitboxes
     * Más preciso que solo comparar row/col
     */
    public boolean colisionaConJugador(Player player) {
        // Obtener hitbox del enemigo (celda completa)
        Rectangle hitboxEnemigo = new Rectangle(
            col * 48, 
            row * 48, 
            48, 
            48
        );
        
        // Obtener hitbox del jugador (un poco más pequeña para ser justo)
        Rectangle hitboxJugador = new Rectangle(
            player.getCol() * 48 + 8,  // Margen de 8px
            player.getRow() * 48 + 8,
            32,  // 48 - 16 = más pequeño
            32
        );
        
        // Verificar intersección
        return hitboxEnemigo.intersects(hitboxJugador);
    }
    
    /**
     * Verifica colisión simple por celda
     */
    public boolean colisionaCelda(Player player) {
        return this.row == player.getRow() && this.col == player.getCol();
    }
    
    // Getters
    public int getRow() { return row; }
    public int getCol() { return col; }
    public Direction getDireccion() { return direccion; }
}