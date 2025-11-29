package dominio;

import java.util.ArrayList;
import java.util.Random;

public class Nivel {
    private int filas, columnas;
    private ArrayList<Fruta> frutas;
    private ArrayList<Enemigo> enemigos;
    private ArrayList<Obstaculo> obstaculos;
    private Random random;
    
    public Nivel(NivelConfig cfg) {
        this.filas = cfg.filas;
        this.columnas = cfg.columnas;
        this.frutas = new ArrayList<>();
        this.enemigos = new ArrayList<>();
        this.obstaculos = new ArrayList<>();
        this.random = new Random();
        
        inicializarNivel(cfg);
    }
    
    private void inicializarNivel(NivelConfig cfg) {
        crearBloquesBorde();
        crearBloquesInternos();
        crearFrutas(cfg);
        crearEnemigos(cfg);
    }
    
    private void crearBloquesBorde() {
        for (int col = 0; col < columnas; col++) {
            obstaculos.add(new BloqueHielo(0, col, false));
            obstaculos.add(new BloqueHielo(filas - 1, col, false));
        }
        for (int row = 1; row < filas - 1; row++) {
            obstaculos.add(new BloqueHielo(row, 0, false));
            obstaculos.add(new BloqueHielo(row, columnas - 1, false));
        }
    }
    
    private void crearBloquesInternos() {
        for (int row = 3; row < filas - 3; row += 3) {
            for (int col = 3; col < columnas - 3; col += 3) {
                if (random.nextDouble() < 0.3) {
                    obstaculos.add(new BloqueHielo(row, col, false));
                }
            }
        }
    }
    
    private void crearFrutas(NivelConfig cfg) {
        agregarFrutasAleatorias(cfg.uvas, () -> new Uva());
        agregarFrutasAleatorias(cfg.platanos, () -> new Platano());
        
        // ⭐ Cerezas necesitan referencia al nivel
        for (int i = 0; i < cfg.cerezas; i++) {
            Posicion pos = obtenerPosicionLibre();
            if (pos != null) {
                Cereza cereza = new Cereza();
                cereza.setPosicion(pos.row, pos.col);
                cereza.setNivel(this); // ⭐ Asignar nivel para teletransporte
                frutas.add(cereza);
            }
        }
        
        // ⭐ Piñas necesitan referencia al nivel (jugador se asigna después)
        for (int i = 0; i < cfg.pinas; i++) {
            Posicion pos = obtenerPosicionLibre();
            if (pos != null) {
                Pina pina = new Pina();
                pina.setPosicion(pos.row, pos.col);
                pina.setNivel(this); // ⭐ Asignar nivel
                frutas.add(pina);
            }
        }
        
        agregarFrutasAleatorias(cfg.cactus, () -> new Cactus());
    }
    
    private void agregarFrutasAleatorias(int cantidad, FrutaFactory factory) {
        for (int i = 0; i < cantidad; i++) {
            Posicion pos = obtenerPosicionLibre();
            if (pos != null) {
                Fruta fruta = factory.crear();
                fruta.setPosicion(pos.row, pos.col);
                frutas.add(fruta);
            }
        }
    }
    
    private void crearEnemigos(NivelConfig cfg) {
        for (int i = 0; i < cfg.trolls; i++) {
            Posicion pos = obtenerPosicionLibre();
            if (pos != null) {
                enemigos.add(new Troll(pos.row, pos.col));
            }
        }
        
        for (int i = 0; i < cfg.macetas; i++) {
            Posicion pos = obtenerPosicionLibre();
            if (pos != null) {
                enemigos.add(new Maceta(pos.row, pos.col));
            }
        }
    }
    
    private Posicion obtenerPosicionLibre() {
        for (int intento = 0; intento < 100; intento++) {
            int row = 2 + random.nextInt(filas - 4);
            int col = 2 + random.nextInt(columnas - 4);
            
            if (esPosicionLibre(row, col)) {
                return new Posicion(row, col);
            }
        }
        return null;
    }
    
    public boolean puedeMoverse(int row, int col) {
        if (row < 0 || row >= filas || col < 0 || col >= columnas) {
            return false;
        }
        
        for (Obstaculo obs : obstaculos) {
            if (obs.getRow() == row && obs.getCol() == col && obs.bloquea()) {
                return false;
            }
        }
        
        return true;
    }
    
    private boolean esPosicionLibre(int row, int col) {
        if (!puedeMoverse(row, col)) return false;
        
        for (Fruta f : frutas) {
            if (f.getRow() == row && f.getCol() == col) return false;
        }
        
        for (Enemigo e : enemigos) {
            if (e.getRow() == row && e.getCol() == col) return false;
        }
        
        return true;
    }
    
    // ========================================================================
    // ⭐ NUEVO: Asignar jugador a las piñas
    // ========================================================================
    public void asignarJugadorAPinas(Player jugador) {
        for (Fruta f : frutas) {
            if (f instanceof Pina) {
                ((Pina)f).setJugador(jugador);
            }
        }
    }
    
    // ========================================================================
    // ⭐ ACTUALIZADO: updateSpecials ahora maneja todas las frutas
    // ========================================================================
    public void updateSpecials() {
        for (Fruta f : frutas) {
            if (f instanceof SpecialFruit) {
                ((SpecialFruit)f).tick();
            }
        }
        
        for (Obstaculo obs : obstaculos) {
            if (obs instanceof BloqueHielo) {
                ((BloqueHielo)obs).tick();
            }
        }
    }
    
    public int remaining() {
        int count = 0;
        for (Fruta f : frutas) {
            if (!f.isRecolectada()) count++;
        }
        return count;
    }
    
    public int getTotalFrutas() {
        return frutas.size();
    }
    
    public void agregarBloqueHielo(int row, int col) {
        if (puedeMoverse(row, col)) {
            if (!hayBloqueHielo(row, col)) {
                obstaculos.add(new BloqueHielo(row, col, false));
                
                for (Fruta f : frutas) {
                    if (f.getRow() == row && f.getCol() == col && !f.isRecolectada()) {
                        f.congelar();
                    }
                }
            }
        }
    }
    
    public boolean removerBloqueHielo(int row, int col) {
        int sizeBefore = obstaculos.size();
        
        obstaculos.removeIf(obs -> 
            obs instanceof BloqueHielo && 
            obs.getRow() == row && 
            obs.getCol() == col
        );
        
        if (obstaculos.size() < sizeBefore) {
            for (Fruta f : frutas) {
                if (f.getRow() == row && f.getCol() == col) {
                    f.descongelar();
                }
            }
        }
        
        return obstaculos.size() < sizeBefore;
    }
    
    public boolean hayBloqueHielo(int row, int col) {
        for (Obstaculo obs : obstaculos) {
            if (obs instanceof BloqueHielo && 
                obs.getRow() == row && 
                obs.getCol() == col) {
                return true;
            }
        }
        return false;
    }
    
    public boolean hayEnemigoEn(int row, int col) {
        for (Enemigo enemigo : enemigos) {
            if (enemigo.getRow() == row && enemigo.getCol() == col) {
                return true;
            }
        }
        return false;
    }
    
    // ========================================================================
    // ⭐ NUEVO: Verificar si hay una fruta en una posición
    // ========================================================================
    public boolean hayFrutaEn(int row, int col) {
        for (Fruta f : frutas) {
            if (f.getRow() == row && f.getCol() == col && !f.isRecolectada()) {
                return true;
            }
        }
        return false;
    }
    
    public boolean esBloqueHieloPermanente(int row, int col) {
        for (Obstaculo obs : obstaculos) {
            if (obs instanceof BloqueHielo && 
                obs.getRow() == row && 
                obs.getCol() == col) {
                return ((BloqueHielo)obs).esPermanente();
            }
        }
        return false;
    }
    
    public int contarBloquesTemporales() {
        int count = 0;
        for (Obstaculo obs : obstaculos) {
            if (obs instanceof BloqueHielo && !((BloqueHielo)obs).esPermanente()) {
                count++;
            }
        }
        return count;
    }
    
    // Getters
    public int getFilas() { return filas; }
    public int getColumnas() { return columnas; }
    public ArrayList<Fruta> getFrutas() { return frutas; }
    public ArrayList<Enemigo> getEnemigos() { return enemigos; }
    public ArrayList<Obstaculo> getObstaculos() { return obstaculos; }
}

@FunctionalInterface
interface FrutaFactory {
    Fruta crear();
}

class Posicion {
    int row, col;
    Posicion(int row, int col) {
        this.row = row;
        this.col = col;
    }
}