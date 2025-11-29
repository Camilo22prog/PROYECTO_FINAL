package dominio;

import java.util.logging.*;
import java.io.IOException;

public class LoggerConfig {
    public static void setup() {
        try {
            Logger rootLogger = Logger.getLogger("");
            
            // Remover handlers existentes
            for (Handler handler : rootLogger.getHandlers()) {
                rootLogger.removeHandler(handler);
            }
            
            // Console handler
            ConsoleHandler consoleHandler = new ConsoleHandler();
            consoleHandler.setLevel(Level.INFO);
            consoleHandler.setFormatter(new SimpleFormatter());
            
            // File handler
            FileHandler fileHandler = new FileHandler("bad_icecream.log", true);
            fileHandler.setLevel(Level.ALL);
            fileHandler.setFormatter(new SimpleFormatter());
            
            rootLogger.addHandler(consoleHandler);
            rootLogger.addHandler(fileHandler);
            rootLogger.setLevel(Level.INFO);
            
        } catch (IOException e) {
            System.err.println("Error configurando logger: " + e.getMessage());
        }
    }
}