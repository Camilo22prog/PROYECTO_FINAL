package dominio;

import java.io.Serializable;
import java.util.logging.Logger;

public class Juego implements Serializable {
    private static final long serialVersionUID = 1L;
    private transient Logger log = Logger.getLogger("BadIceCream");
    
    private GameMode mode;
    private Nivel nivel;
    private Player player;
    private int score = 0;
    private boolean paused = false;
    private int tiempoRestante = 1800;
    private boolean nivelCompletado = false;
    private boolean gameOver = false;
    
    public Juego(GameMode mode, NivelConfig cfg, HeladoType tipo, String nombre) {
        this.mode = mode;
        this.nivel = new Nivel(cfg);
        this.player = new Player(tipo, nombre, 1, 1);
        
        // ⭐ Asignar el jugador a las piñas para que lo sigan
        nivel.asignarJugadorAPinas(player);
    }
    
    public void update() {
        if (paused || nivelCompletado || gameOver) return;
        
        tiempoRestante--;
        if (tiempoRestante <= 0) {
            gameOver = true;
            return;
        }
        
        // Actualizar enemigos
        for (Enemigo e : nivel.getEnemigos()) {
            e.update(nivel, player);
        }
        
        // ⭐ Actualizar frutas especiales (Cereza se teletransporta, Piña se mueve, Cactus cambia)
        nivel.updateSpecials();
        
        // Verificar colisiones jugador-enemigos
        for (Enemigo e : nivel.getEnemigos()) {
            if (player.collidesWith(e)) {
                gameOver = true;
                return;
            }
        }
        
        // ⭐ Verificar colisiones jugador-frutas (incluyendo cactus con púas)
        for (Fruta f : nivel.getFrutas()) {
            if (!f.isRecolectada() && player.collidesWith(f)) {
                
                // ⭐ Caso especial: Cactus con púas mata al jugador
                if (f instanceof Cactus) {
                    Cactus cactus = (Cactus)f;
                    if (cactus.esPeligroso()) {
                        // ¡El jugador tocó un cactus con púas!
                        System.out.println("💀 ¡Jugador eliminado por cactus con púas!");
                        gameOver = true;
                        return;
                    } else if (!cactus.puedeRecolectar()) {
                        // Cactus tiene púas pero está congelado, no pasa nada
                        continue;
                    }
                }
                
                // Recolectar fruta si no está congelada
                if (!f.estaCongelada()) {
                    f.recolectar();
                    score += f.getPuntos();
                    System.out.println("✨ Fruta recolectada! +" + f.getPuntos() + " puntos");
                }
            }
        }
        
        if (nivel.remaining() == 0) {
            nivelCompletado = true;
        }
    }
    
    public void moverJugador(Direction dir) {
        if (!paused && !gameOver && !nivelCompletado) {
            int newRow = player.getRow() + dir.getDy();
            int newCol = player.getCol() + dir.getDx();
            
            // SIEMPRE cambiar la dirección
            player.setDirection(dir);
            
            // Solo moverse si la casilla destino está libre
            if (nivel.puedeMoverse(newRow, newCol)) {
                player.mover(newRow, newCol);
            }
        }
    }
    
    public void accionBloqueHielo() {
        if (!paused && !gameOver && !nivelCompletado) {
            player.accionBloqueHielo(nivel);
        }
    }
    
    public void crearBloqueHielo() {
        accionBloqueHielo();
    }
    
    public void romperBloqueHielo() {
        accionBloqueHielo();
    }
    
    public void togglePause() {
        paused = !paused;
    }
    
    // Getters
    public Nivel getNivel() { return nivel; }
    public Player getPlayer() { return player; }
    public int getScore() { return score; }
    public boolean isPaused() { return paused; }
    public int getTiempoRestante() { return tiempoRestante; }
    public boolean isNivelCompletado() { return nivelCompletado; }
    public boolean isGameOver() { return gameOver; }
    public int getFrutasRecolectadas() { 
        return nivel.getTotalFrutas() - nivel.remaining(); 
    }
    public int getTotalFrutas() { return nivel.getTotalFrutas(); }
}