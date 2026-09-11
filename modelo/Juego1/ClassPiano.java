package modelo.Juego1;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.BufferedInputStream;
import java.io.InputStream;
import javax.sound.sampled.*;

public class ClassPiano {

    public int AnchoTeclasNegras;
    public int AltoTeclasNegras;
    public int AnchoTeclasBlancas;
    public int AltoTeclasBlancas;

    public ClassPiano() {
        AnchoTeclasNegras  = 40;
        AltoTeclasNegras   = 200;
        AnchoTeclasBlancas = 50;
        AltoTeclasBlancas  = 300;
    }

    // -------------------------------------------------------------------------
    // TeclaPiano: componente que se dibuja a si mismo.
    // Evita completamente los efectos hover/press del Look&Feel de Swing.
    // -------------------------------------------------------------------------
    public static class TeclaPiano extends JComponent {

        private final Color colorBase;
        private final Color colorTexto;
        private final Font  fuente;
        private boolean     presionada = false;
        private String      texto      = "";

        public TeclaPiano(Color colorBase, Color colorTexto, Font fuente) {
            this.colorBase  = colorBase;
            this.colorTexto = colorTexto;
            this.fuente     = fuente;
            setOpaque(true);
            setFocusable(false);
            setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        }

        public void setTexto(String t)  { this.texto = t; }
        public Color getColorBase()     { return colorBase; }

        public void setPresionada(boolean p) {
            this.presionada = p;
            repaint();
        }

        @Override
        protected void paintComponent(Graphics g) {
            g.setColor(presionada ? Color.GRAY : colorBase);
            g.fillRect(0, 0, getWidth(), getHeight());

            g.setColor(Color.DARK_GRAY);
            g.drawRect(0, 0, getWidth() - 1, getHeight() - 1);

            if (texto != null && !texto.isEmpty()) {
                g.setColor(colorTexto);
                g.setFont(fuente);
                FontMetrics fm = g.getFontMetrics();
                int x = (getWidth() - fm.stringWidth(texto)) / 2;
                int y = getHeight() - 8;
                g.drawString(texto, x, y);
            }
        }
    }

    private String notaANombreArchivo(String nota) {
        return nota.replace("#", "sharp");
    }

    public void CrearTeclasNegras(String[] NotasDeTeclasNegras, JPanel panel) {
        for (int i = 0; i < NotasDeTeclasNegras.length; i++) {
            if (!NotasDeTeclasNegras[i].equals("_")) {
                TeclaPiano TeclaNegra = new TeclaPiano(
                        Color.BLACK, Color.WHITE,
                        new Font("Arial", Font.PLAIN, 8));
                TeclaNegra.setTexto(NotasDeTeclasNegras[i]);
                TeclaNegra.setName(NotasDeTeclasNegras[i]);
                TeclaNegra.setSize(AnchoTeclasNegras, AltoTeclasNegras);
                TeclaNegra.setForeground(Color.WHITE);
                TeclaNegra.setLocation((i * AnchoTeclasBlancas) + 30, 0);

                final String nota = NotasDeTeclasNegras[i];
                TeclaNegra.addMouseListener(new MouseAdapter() {
                    @Override public void mousePressed(MouseEvent e) { ReproducirSonido(nota); }
                });
                panel.add(TeclaNegra);
            }
        }
    }

    public void CrearTeclasBlancas(String[] NotasDeTeclasBlancas, JPanel panel) {
        for (int i = 0; i < NotasDeTeclasBlancas.length; i++) {
            TeclaPiano TeclaBlanca = new TeclaPiano(
                    Color.WHITE, Color.BLACK,
                    new Font("Arial", Font.BOLD, 10));
            TeclaBlanca.setTexto(NotasDeTeclasBlancas[i]);
            TeclaBlanca.setName(NotasDeTeclasBlancas[i]);
            TeclaBlanca.setSize(AnchoTeclasBlancas, AltoTeclasBlancas);
            TeclaBlanca.setForeground(Color.BLACK);
            TeclaBlanca.setLocation(i * AnchoTeclasBlancas, 0);

            final String nota = NotasDeTeclasBlancas[i];
            TeclaBlanca.addMouseListener(new MouseAdapter() {
                @Override public void mousePressed(MouseEvent e) { ReproducirSonido(nota); }
            });
            panel.add(TeclaBlanca);
        }
    }

    public void ReproducirSonido(String nombreNota) {
        boolean pedalActivo = false;
        try {
            pedalActivo = Toolkit.getDefaultToolkit()
                    .getLockingKeyState(KeyEvent.VK_CAPS_LOCK);
        } catch (Exception ex) { }

        String nombreArchivo = notaANombreArchivo(
                pedalActivo ? nombreNota + "_SUS" : nombreNota);

        new Thread(() -> {
            try {
                InputStream is = getClass()
                        .getResourceAsStream("/resources/" + nombreArchivo + ".wav");
                if (is != null) {
                    AudioInputStream ais = AudioSystem.getAudioInputStream(
                            new BufferedInputStream(is));
                    Clip clip = AudioSystem.getClip();
                    clip.open(ais);
                    clip.start();
                } else {
                    System.out.println("Recurso no encontrado: " + nombreArchivo + ".wav");
                }
            } catch (Exception ex) {
                System.out.println("Error: " + ex.getMessage());
            }
        }).start();
    }
}