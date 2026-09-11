package modelo.Juego2;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Collections;

class Carta extends JButton {
    private final int id;
    private boolean volteada = false;
    private boolean encontrada = false;
    private final String valorOculto;

    public Carta(int id, String valorOculto) {
        this.id = id;
        this.valorOculto = valorOculto;
        this.setText("?");
        this.setFont(new Font("SansSerif", Font.BOLD, 26));
        this.setBackground(new Color(220, 220, 220));
        this.setFocusPainted(false);
        this.setCursor(new Cursor(Cursor.HAND_CURSOR));
    }

    public void voltear() {
        volteada = !volteada;
        if (volteada) {
            this.setText(valorOculto);
            this.setEnabled(false);
            this.setBackground(new Color(255, 255, 255));
            this.setForeground(new Color(40, 40, 40));
        } else {
            this.setText("?");
            this.setEnabled(true);
            this.setBackground(new Color(220, 220, 220));
            this.setForeground(new Color(100, 100, 100));
        }
    }

    public void marcarEncontrada() {
        encontrada = true;
        this.setBackground(new Color(180, 230, 190));
        this.setForeground(new Color(30, 100, 50));
        this.setEnabled(false);
    }

    public void marcarError() {
        this.setBackground(new Color(255, 180, 180));
        this.setForeground(new Color(150, 30, 30));
    }

    public int getId() { return id; }
    public boolean estaEncontrada() { return encontrada; }
}

public class Main extends JFrame {
    private JPanel panelCartas;
    private JLabel labelTimer;
    private JLabel labelParejas;
    private JButton btnReiniciar;
    private ArrayList<Carta> listaCartas;
    private Carta primeraSeleccionada = null;
    private Carta segundaSeleccionada = null;
    private int segundos = 0;
    private int parejasEncontradas = 0;
    private final int TOTAL_PAREJAS = 12;
    private Timer timerDeJuego;
    private boolean juegoIniciado = false;

    // CONSTRUCTOR
    public Main() {
        setTitle("Juego de Memoria");
        setSize(720, 620);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE); // Cambiado a DISPOSE para poder volver al menú
        setLayout(new BorderLayout(10, 10));
        getContentPane().setBackground(new Color(245, 245, 245));

        construirPanelNorte();
        construirPanelCartas();
        construirPanelSur();
        prepararCartas();

        setLocationRelativeTo(null);
    }

    private void construirPanelNorte() {
        JPanel panelNorte = new JPanel(new GridLayout(1, 2));
        panelNorte.setBackground(new Color(60, 60, 80));
        panelNorte.setBorder(BorderFactory.createEmptyBorder(12, 20, 12, 20));

        labelTimer = new JLabel("Tiempo: 0s", SwingConstants.LEFT);
        labelTimer.setFont(new Font("SansSerif", Font.BOLD, 20));
        labelTimer.setForeground(Color.WHITE);

        labelParejas = new JLabel("Parejas: 0 / " + TOTAL_PAREJAS, SwingConstants.RIGHT);
        labelParejas.setFont(new Font("SansSerif", Font.BOLD, 20));
        labelParejas.setForeground(Color.WHITE);

        panelNorte.add(labelTimer);
        panelNorte.add(labelParejas);
        add(panelNorte, BorderLayout.NORTH);
    }

    private void construirPanelCartas() {
        panelCartas = new JPanel(new GridLayout(4, 6, 8, 8));
        panelCartas.setBackground(new Color(245, 245, 245));
        panelCartas.setBorder(BorderFactory.createEmptyBorder(12, 16, 12, 16));
        add(panelCartas, BorderLayout.CENTER);
    }

    private void construirPanelSur() {
        JPanel panelSur = new JPanel();
        panelSur.setBackground(new Color(245, 245, 245));
        panelSur.setBorder(BorderFactory.createEmptyBorder(0, 0, 12, 0));

        btnReiniciar = new JButton("Reiniciar partida");
        btnReiniciar.setFont(new Font("SansSerif", Font.PLAIN, 14));
        btnReiniciar.setBackground(new Color(60, 60, 80));
        btnReiniciar.setForeground(Color.WHITE);
        btnReiniciar.setFocusPainted(false);
        btnReiniciar.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnReiniciar.setBorder(BorderFactory.createEmptyBorder(10, 28, 10, 28));
        btnReiniciar.addActionListener(e -> reiniciarJuego());

        panelSur.add(btnReiniciar);
        add(panelSur, BorderLayout.SOUTH);
    }

    private void prepararCartas() {
        panelCartas.removeAll();
        listaCartas = new ArrayList<>();
        String[] simbolos = {"#", "@", "$", "&", "|", "∑", "π", "*", "%", "/", "~", "°"};

        for (int i = 0; i < simbolos.length; i++) {
            listaCartas.add(new Carta(i, simbolos[i]));
            listaCartas.add(new Carta(i, simbolos[i]));
        }

        Collections.shuffle(listaCartas);

        for (int i = 0; i < listaCartas.size(); i++) {
            final Carta c = listaCartas.get(i);
            c.addActionListener(e -> manejarClicCarta(c));
            panelCartas.add(c);
        }

        panelCartas.revalidate();
        panelCartas.repaint();
    }

    private void iniciarContadorTiempo() {
        timerDeJuego = new Timer(1000, e -> {
            segundos++;
            labelTimer.setText("Tiempo: " + segundos + "s");
        });
        timerDeJuego.start();
    }

    private void manejarClicCarta(Carta carta) {
        if (primeraSeleccionada != null && segundaSeleccionada != null) return;
        if (carta.estaEncontrada()) return;

        if (!juegoIniciado) {
            juegoIniciado = true;
            iniciarContadorTiempo();
        }

        carta.voltear();

        if (primeraSeleccionada == null) {
            primeraSeleccionada = carta;
        } else {
            segundaSeleccionada = carta;
            verificarPareja();
        }
    }

    private void verificarPareja() {
        if (primeraSeleccionada.getId() == segundaSeleccionada.getId()) {
            primeraSeleccionada.marcarEncontrada();
            segundaSeleccionada.marcarEncontrada();
            primeraSeleccionada = null;
            segundaSeleccionada = null;
            parejasEncontradas++;
            labelParejas.setText("Parejas: " + parejasEncontradas + " / " + TOTAL_PAREJAS);

            if (parejasEncontradas == TOTAL_PAREJAS) {
                timerDeJuego.stop();
                JOptionPane.showMessageDialog(this,
                    "¡Completaste el tablero!\nTiempo: " + segundos + " segundos",
                    "¡Ganaste!", JOptionPane.INFORMATION_MESSAGE);
            }
        } else {
            primeraSeleccionada.marcarError();
            segundaSeleccionada.marcarError();

            final Carta p = primeraSeleccionada;
            final Carta s = segundaSeleccionada;
            primeraSeleccionada = null;
            segundaSeleccionada = null;

            Timer delay = new Timer(800, e -> {
                p.voltear();
                s.voltear();
            });
            delay.setRepeats(false);
            delay.start();
        }
    }

    private void reiniciarJuego() {
        if (timerDeJuego != null) timerDeJuego.stop();
        segundos = 0;
        parejasEncontradas = 0;
        primeraSeleccionada = null;
        segundaSeleccionada = null;
        juegoIniciado = false;
        labelTimer.setText("Tiempo: 0s");
        labelParejas.setText("Parejas: 0 / " + TOTAL_PAREJAS);
        prepararCartas();
    }

    // Main por si quieres ejecutar el juego directamente
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new Main().setVisible(true));
    }
}