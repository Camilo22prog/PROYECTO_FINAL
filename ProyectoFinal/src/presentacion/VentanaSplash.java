// ============================================================================
// VENTANASPLASH.JAVA - Pantalla de inicio con GIFs y sonido
// ============================================================================
package presentacion;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.File;
import javax.sound.sampled.*;

public class VentanaSplash extends JFrame {
    
    private JLabel lblFondo;
    private JLabel lblLogo;
    private JLabel lblClickToLick;
    private JButton btnMusica;
    private Clip musicaFondo;
    private boolean musicaActiva = true;
    
    // Rutas de los recursos (ajusta según tu estructura)
    private static final String RUTA_FONDO = "/recursos/fondo.gif";
    private static final String RUTA_LOGO = "/recursos/logo.png";
    private static final String RUTA_CLICK = "/recursos/click_to_lick.gif";
    private static final String RUTA_MUSICA_ON = "/recursos/musica_on.png";
    private static final String RUTA_MUSICA_OFF = "/recursos/musica_off.png";
    private static final String RUTA_AUDIO = "/recursos/Enterrauw.wav";
    
    public VentanaSplash() {
        super("Bad Ice-Cream");
        
        // Configuración de la ventana
        setSize(800, 600);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setResizable(false);
        setLayout(null); // Layout absoluto para posicionar elementos
        
        // Cargar y configurar componentes
        cargarLogo();
        cargarFondo();
        cargarBotonClick();
        cargarBotonMusica();
        
        // Iniciar música de fondo
        iniciarMusica();
        
        // Hacer clic en cualquier parte para continuar
        configurarEventoClick();
        
        setVisible(true);
    }
    
 // ========================================================================
    // CARGAR LOGO DEL JUEGO
    // ========================================================================
    private void cargarLogo() {
        try {
        	System.out.println( "Logo encontrado: " + getClass().getResource(RUTA_LOGO));
            ImageIcon iconoLogo = new ImageIcon(getClass().getResource(RUTA_LOGO));
            
            // Escalar logo si es necesario
            Image imagenEscalada = iconoLogo.getImage().getScaledInstance(600,300, Image.SCALE_SMOOTH);
            iconoLogo = new ImageIcon(imagenEscalada);
            
            lblLogo = new JLabel(iconoLogo);
            lblLogo.setBounds(100, 100, 600, 300);
            
            if (lblFondo != null) {
            	lblFondo.setLayout(null);
                lblFondo.add(lblLogo);
            } else {
                add(lblLogo);
            }
            
        } catch (Exception e) {
            System.err.println("No se pudo cargar el logo: " + e.getMessage());
            
            // Fallback: texto
            lblLogo = new JLabel("BAD ICE-CREAM", SwingConstants.CENTER);
            lblLogo.setFont(new Font("Arial", Font.BOLD, 48));
            lblLogo.setForeground(new Color(255, 100, 150));
            lblLogo.setBounds(200, 100, 400, 100);
            
            if (lblFondo != null) {
            	lblFondo.setLayout(null);
                lblFondo.add(lblLogo);
            } else {
                add(lblLogo);
            }
        }
    }
    
    // ========================================================================
    // CARGAR FONDO ANIMADO (GIF)
    // ========================================================================
    private void cargarFondo() {
        try {
            ImageIcon iconoFondo = new ImageIcon(getClass().getResource(RUTA_FONDO));
            
            // Escalar el GIF al tamaño de la ventana si es necesario
            Image imagenEscalada = iconoFondo.getImage().getScaledInstance(800, 600, Image.SCALE_DEFAULT);
            iconoFondo = new ImageIcon(imagenEscalada);
            
            lblFondo = new JLabel(iconoFondo);
            lblFondo.setBounds(0, 0, 800, 600);
            add(lblFondo);
            
        } catch (Exception e) {
            // Si no se encuentra el GIF, usar color de fondo
            System.err.println("No se pudo cargar el fondo: " + e.getMessage());
            getContentPane().setBackground(new Color(140, 200, 255));
        }
    }
    
    
    
    // ========================================================================
    // CARGAR BOTÓN "CLICK TO LICK" (GIF ANIMADO)
    // ========================================================================
    private void cargarBotonClick() {
        try {
            ImageIcon iconoClick = new ImageIcon(getClass().getResource(RUTA_CLICK));
            
            // Escalar GIF del botón
            Image imagenEscalada = iconoClick.getImage().getScaledInstance(300, 100, Image.SCALE_DEFAULT);
            iconoClick = new ImageIcon(imagenEscalada);
            
            lblClickToLick = new JLabel(iconoClick);
            lblClickToLick.setBounds(250, 380, 300, 100); // Centrado abajo
            lblClickToLick.setCursor(new Cursor(Cursor.HAND_CURSOR));
            
            // Agregar efecto hover
            lblClickToLick.addMouseListener(new MouseAdapter() {
                @Override
                public void mouseEntered(MouseEvent e) {
                    lblClickToLick.setBounds(245, 375, 310, 110); // Agrandar ligeramente
                }
                
                @Override
                public void mouseExited(MouseEvent e) {
                    lblClickToLick.setBounds(250, 380, 300, 100); // Tamaño original
                }
                
                @Override
                public void mouseClicked(MouseEvent evt) {
                    irAlMenu();
                }
            });
            
            if (lblFondo != null) {
                lblFondo.add(lblClickToLick);
            } else {
                add(lblClickToLick);
            }
            
        } catch (Exception e) {
            System.err.println("No se pudo cargar el botón click: " + e.getMessage());
            
            // Fallback: botón normal
            JButton btnClick = new JButton("CLICK TO LICK");
            btnClick.setFont(new Font("Arial", Font.BOLD, 24));
            btnClick.setBounds(250, 400, 300, 80);
            btnClick.setBackground(new Color(255, 200, 100));
            btnClick.setFocusPainted(false);
            btnClick.setCursor(new Cursor(Cursor.HAND_CURSOR));
            btnClick.addActionListener(evt -> irAlMenu());
            
            if (lblFondo != null) {
                lblFondo.add(btnClick);
            } else {
                add(btnClick);
            }
        }
    }
    
    // ========================================================================
    // CARGAR BOTÓN DE MÚSICA (ON/OFF)
    // ========================================================================
    private void cargarBotonMusica() {
        try {
            ImageIcon iconoMusicaOn = new ImageIcon(getClass().getResource(RUTA_MUSICA_ON));
            ImageIcon iconoMusicaOff = new ImageIcon(getClass().getResource(RUTA_MUSICA_OFF));
            
            // Escalar iconos
            Image imgOn = iconoMusicaOn.getImage().getScaledInstance(50, 50, Image.SCALE_SMOOTH);
            Image imgOff = iconoMusicaOff.getImage().getScaledInstance(50, 50, Image.SCALE_SMOOTH);
            iconoMusicaOn = new ImageIcon(imgOn);
            iconoMusicaOff = new ImageIcon(imgOff);
            
            btnMusica = new JButton(iconoMusicaOn);
            btnMusica.setBounds(720, 20, 60, 60); // Esquina superior derecha
            btnMusica.setBorderPainted(false);
            btnMusica.setContentAreaFilled(false);
            btnMusica.setFocusPainted(false);
            btnMusica.setCursor(new Cursor(Cursor.HAND_CURSOR));
            
            ImageIcon finalIconoOn = iconoMusicaOn;
            ImageIcon finalIconoOff = iconoMusicaOff;
            
            btnMusica.addActionListener(evt -> {
                musicaActiva = !musicaActiva;
                
                if (musicaActiva) {
                    btnMusica.setIcon(finalIconoOn);
                    reanudarMusica();
                } else {
                    btnMusica.setIcon(finalIconoOff);
                    pausarMusica();
                }
            });
            
            if (lblFondo != null) {
                lblFondo.add(btnMusica);
            } else {
                add(btnMusica);
            }
            
        } catch (Exception e) {
            System.err.println("No se pudieron cargar los iconos de música: " + e.getMessage());
            
            // Fallback: botón con texto
            btnMusica = new JButton("♪");
            btnMusica.setFont(new Font("Arial", Font.BOLD, 24));
            btnMusica.setBounds(720, 20, 60, 60);
            btnMusica.setBackground(new Color(255, 255, 255, 180));
            btnMusica.setFocusPainted(false);
            btnMusica.setCursor(new Cursor(Cursor.HAND_CURSOR));
            
            btnMusica.addActionListener(evt -> {
                musicaActiva = !musicaActiva;
                btnMusica.setText(musicaActiva ? "♪" : "♪̸");
                
                if (musicaActiva) {
                    reanudarMusica();
                } else {
                    pausarMusica();
                }
            });
            
            if (lblFondo != null) {
                lblFondo.add(btnMusica);
            } else {
                add(btnMusica);
            }
        }
    }
    
    // ========================================================================
    // CONFIGURAR EVENTO CLICK EN TODA LA VENTANA
    // ========================================================================
    private void configurarEventoClick() {
        // Hacer que toda la ventana sea clickeable
        addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent evt) {
                irAlMenu();
            }
        });
        
        // También hacer el fondo clickeable
        if (lblFondo != null) {
            lblFondo.addMouseListener(new MouseAdapter() {
                @Override
                public void mouseClicked(MouseEvent evt) {
                    irAlMenu();
                }
            });
        }
    }
    
    // ========================================================================
    // SISTEMA DE MÚSICA
    // ========================================================================
    
    private void iniciarMusica() {
        try {
            // Intentar cargar desde recursos
            AudioInputStream audioStream = AudioSystem.getAudioInputStream(
                getClass().getResource(RUTA_AUDIO)
            );
            
            musicaFondo = AudioSystem.getClip();
            musicaFondo.open(audioStream);
            musicaFondo.loop(Clip.LOOP_CONTINUOUSLY);
            musicaFondo.start();
            
        } catch (Exception e) {
            System.err.println("No se pudo cargar la música: " + e.getMessage());
            
            // Intentar desde archivo externo
            try {
                File archivoAudio = new File("recursos/musica_fondo.wav");
                if (archivoAudio.exists()) {
                    AudioInputStream audioStream = AudioSystem.getAudioInputStream(archivoAudio);
                    musicaFondo = AudioSystem.getClip();
                    musicaFondo.open(audioStream);
                    musicaFondo.loop(Clip.LOOP_CONTINUOUSLY);
                    musicaFondo.start();
                }
            } catch (Exception ex) {
                System.err.println("Tampoco se pudo cargar desde archivo externo: " + ex.getMessage());
            }
        }
    }
    
    private void pausarMusica() {
        if (musicaFondo != null && musicaFondo.isRunning()) {
            musicaFondo.stop();
        }
    }
    
    private void reanudarMusica() {
        if (musicaFondo != null && !musicaFondo.isRunning()) {
            musicaFondo.setFramePosition(0);
            musicaFondo.loop(Clip.LOOP_CONTINUOUSLY);
            musicaFondo.start();
        }
    }
    
    private void detenerMusica() {
        if (musicaFondo != null) {
            musicaFondo.stop();
            musicaFondo.close();
        }
    }
    
    // ========================================================================
    // IR AL MENÚ PRINCIPAL
    // ========================================================================
    private void irAlMenu() {
        // Detener música si está activa
        detenerMusica();
        
        // Cerrar esta ventana
        dispose();
        
        // Abrir menú principal
        SwingUtilities.invokeLater(() -> {
            new VentanaInicio().setVisible(true);
        });
    }
}

