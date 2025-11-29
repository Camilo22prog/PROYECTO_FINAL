package dominio;

import java.io.Serializable;

public class NivelConfig implements Serializable {
    private static final long serialVersionUID = 1L;
    private static NivelConfig instance;
    
    public int filas = 12;
    public int columnas = 16;
    public int uvas = 8;
    public int platanos = 6;
    public int cerezas = 4;
    public int pinas = 2;
    public int cactus = 1;
    public int trolls = 2;
    public int macetas = 1;
    public int calamares = 0;
    public int narvales = 0;
    
    private NivelConfig() {}
    
    public static NivelConfig getConfig() {
        if (instance == null) {
            instance = new NivelConfig();
        }
        return instance;
    }
    
    public static void setCustomConfig(int uvas, int platanos, int cerezas, int trolls, int macetas) {
        NivelConfig cfg = getConfig();
        cfg.uvas = uvas;
        cfg.platanos = platanos;
        cfg.cerezas = cerezas;
        cfg.trolls = trolls;
        cfg.macetas = macetas;
    }
}