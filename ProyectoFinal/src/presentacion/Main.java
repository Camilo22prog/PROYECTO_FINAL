package presentacion;

import javax.swing.SwingUtilities;
import dominio.LoggerConfig;

public class Main {
    public static void main(String[] args) {
        LoggerConfig.setup();
        SwingUtilities.invokeLater(() -> {
            // Cambiar VentanaInicio por VentanaSplash
            new VentanaSplash().setVisible(true);
        });
    }
}