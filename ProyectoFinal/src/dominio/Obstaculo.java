package dominio;

import java.awt.*;
import java.io.Serializable;

public abstract class Obstaculo implements Serializable {
    private static final long serialVersionUID = 1L;
    
    protected int row, col;
    
    public Obstaculo(int row, int col) {
        this.row = row;
        this.col = col;
    }
    
    public abstract boolean bloquea();
    public abstract void render(Graphics2D g, int offsetX, int offsetY, int cellSize);
    
    public int getRow() { return row; }
    public int getCol() { return col; }
}