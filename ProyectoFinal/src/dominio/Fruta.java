package dominio;

import java.awt.*;
import java.io.Serializable;

public abstract class Fruta implements Serializable {
    private static final long serialVersionUID = 1L;
    
    protected int row, col;
    protected boolean recolectada = false;
    protected int puntos;
    
    // ⭐ NUEVO: Estado de congelada
    protected boolean congelada = false;
    
    public Fruta(int puntos) {
        this.puntos = puntos;
    }
    
    public void setPosicion(int row, int col) {
        this.row = row;
        this.col = col;
    }
    
    public void recolectar() {
        this.recolectada = true;
    }
    
    // ⭐ NUEVO: Métodos para congelar/descongelar
    public void congelar() {
        this.congelada = true;
    }
    
    public void descongelar() {
        this.congelada = false;
    }
    
    public boolean estaCongelada() {
        return congelada;
    }
    
    // Método render base - las subclases lo sobrescriben
    public abstract void render(Graphics2D g, int offsetX, int offsetY, int cellSize);
    
    // Método helper para dibujar efecto de congelada
    protected void dibujarEfectoCongelada(Graphics2D g, int x, int y, int cellSize) {
        if (congelada) {
            // Capa de hielo semi-transparente sobre la fruta
            g.setColor(new Color(150, 200, 255, 120));
            g.fillRect(x, y, cellSize/2, cellSize/2);
            
            // Borde azul brillante
            g.setColor(new Color(100, 180, 255));
            g.setStroke(new BasicStroke(2));
            g.drawRect(x, y, cellSize/2, cellSize/2);
            g.setStroke(new BasicStroke(1));
            
            // Cristales pequeños
            g.setColor(new Color(200, 230, 255));
            g.drawLine(x + 2, y + 2, x + cellSize/2 - 2, y + cellSize/2 - 2);
            g.drawLine(x + cellSize/2 - 2, y + 2, x + 2, y + cellSize/2 - 2);
        }
    }
    
    public int getRow() { return row; }
    public int getCol() { return col; }
    public boolean isRecolectada() { return recolectada; }
    public int getPuntos() { return puntos; }
}