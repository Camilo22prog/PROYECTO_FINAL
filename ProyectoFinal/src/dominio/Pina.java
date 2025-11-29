package dominio;

import java.awt.*;

public class Pina extends Fruta implements SpecialFruit {
    private Player jugadorCercano = null;
    private Nivel nivel = null;
    private int contadorMovimiento = 0;
    
    public Pina() {
        super(200);
    }
    
    /**
     * Asigna el nivel para verificar movimientos válidos
     */
    public void setNivel(Nivel nivel) {
        this.nivel = nivel;
    }
    
    /**
     * Asigna el jugador para seguirlo
     */
    public void setJugador(Player jugador) {
        this.jugadorCercano = jugador;
    }
    
    @Override
    public void tick() {
        // No se mueve si está congelada o recolectada
        if (congelada || recolectada || jugadorCercano == null || nivel == null) {
            return;
        }
        
        // Solo moverse cada cierto tiempo (más lento que el jugador)
        contadorMovimiento++;
        if (contadorMovimiento < 2) return;
        contadorMovimiento = 0;
        
        // Calcular dirección hacia el jugador
        int diffRow = jugadorCercano.getRow() - row;
        int diffCol = jugadorCercano.getCol() - col;
        
        // Solo moverse si el jugador está cerca (radio de 5 casillas)
        int distancia = Math.abs(diffRow) + Math.abs(diffCol);
        if (distancia > 5) return;
        
        // Moverse hacia el jugador (un paso a la vez)
        int newRow = row;
        int newCol = col;
        
        if (Math.abs(diffRow) > Math.abs(diffCol)) {
            // Moverse verticalmente
            newRow += (diffRow > 0) ? 1 : -1;
        } else if (diffCol != 0) {
            // Moverse horizontalmente
            newCol += (diffCol > 0) ? 1 : -1;
        }
        
        // Verificar que la nueva posición sea válida
        if (nivel.puedeMoverse(newRow, newCol) && 
            !nivel.hayEnemigoEn(newRow, newCol)) {
            this.row = newRow;
            this.col = newCol;
        }
    }
    
    @Override
    public void render(Graphics2D g, int offsetX, int offsetY, int cellSize) {
        int x = offsetX + col * cellSize + cellSize/6;
        int y = offsetY + row * cellSize + cellSize/6;
        
        // Cuerpo de la piña (amarillo)
        g.setColor(new Color(255, 193, 37));
        g.fillOval(x, y + 8, 2*cellSize/3, 3*cellSize/4);
        
        // Patrón de diamantes (textura de piña)
        g.setColor(new Color(139, 90, 0));
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                int dx = x + 8 + i*8;
                int dy = y + 12 + j*8;
                g.drawLine(dx, dy, dx + 4, dy + 4);
                g.drawLine(dx + 4, dy, dx, dy + 4);
            }
        }
        
        // Corona verde (hojas)
        g.setColor(new Color(34, 139, 34));
        int[] xPoints = {x + cellSize/3, x + cellSize/6, x + cellSize/4, x + 5, 
                        x + cellSize/4, x + cellSize/3, x + 2*cellSize/5, 
                        x + cellSize/2, x + 2*cellSize/5};
        int[] yPoints = {y + 8, y + 4, y, y + 4, y + 2, y + 8, y + 2, y + 4, y};
        g.fillPolygon(xPoints, yPoints, 9);
        
        // Indicador de movimiento (sombra de movimiento)
        if (!congelada && jugadorCercano != null) {
            int diffRow = jugadorCercano.getRow() - row;
            int diffCol = jugadorCercano.getCol() - col;
            int distancia = Math.abs(diffRow) + Math.abs(diffCol);
            
            if (distancia <= 5) {
                // Mostrar que está "activa" y puede moverse
                g.setColor(new Color(255, 200, 0, 100));
                g.fillOval(x - 4, y + 4, 2*cellSize/3 + 8, 3*cellSize/4 + 8);
            }
        }
        
        // Efecto de congelada
        if (congelada) {
            dibujarEfectoCongelada(g, x - 2, y + 6, cellSize);
        }
    }
}