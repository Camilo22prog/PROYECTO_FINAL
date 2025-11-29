package dominio;

public class GameException extends RuntimeException {
    private static final long serialVersionUID = 1L;
    
    public GameException(String mensaje) {
        super(mensaje);
    }
    
    public GameException(String mensaje, Throwable causa) {
        super(mensaje, causa);
    }
}