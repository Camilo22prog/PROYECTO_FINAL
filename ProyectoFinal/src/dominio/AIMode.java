package dominio;

public enum AIMode {
    HUNGRY("Hambriento - Busca frutas"),
    FEARFUL("Temeroso - Evita enemigos"),
    EXPERT("Experto - Juego óptimo");
    
    private String descripcion;
    
    AIMode(String desc) {
        this.descripcion = desc;
    }
    
    public String getDescripcion() {
        return descripcion;
    }
    
    @Override
    public String toString() {
        return descripcion;
    }
}