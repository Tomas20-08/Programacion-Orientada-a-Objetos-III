package modelo.Juego3;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

/**
 * ============================================================
 *  VentanaBuscaMinas — Interfaz gráfica principal
 * ============================================================
 *  Herencia: extiende JFrame (clase de Java Swing).
 *  Encapsulamiento: el tablero es privado; la ventana solo
 *  interactúa con él mediante métodos públicos.
 * ============================================================
 */
public class VentanaBuscaMinas extends JFrame {

    // ============================================================
    //  ENCAPSULAMIENTO — todos los atributos son privados
    // ============================================================
    private Tablero tablero;
    private JPanel  panelTablero;
    private JLabel  lblEstado;
    private JButton btnReiniciar;
    private Timer   timer;
    private int     segundos;
    private JLabel  lblTiempo;

    // Configuración del juego (fácilmente ajustable)
    private static final int FILAS       = 9;
    private static final int COLUMNAS    = 9;
    private static final int TOTAL_MINAS = 10;

    /**
     * Constructor: construye la ventana completa.
     * Herencia: super() llama al constructor de JFrame.
     */
    public VentanaBuscaMinas() {
        super("💣 Buscaminas — POO en Java");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setResizable(false);

        inicializarUI();
        iniciarJuego();

        pack();
        setLocationRelativeTo(null); // centra en pantalla
        setVisible(true);
    }

    /**
     * PRIVADO — construye los componentes de la ventana.
     * Encapsulamiento: el usuario de la clase no necesita
     * conocer estos detalles de construcción.
     */
    private void inicializarUI() {
        setLayout(new BorderLayout(5, 5));
        getContentPane().setBackground(new Color(45, 45, 48));

        // ── Panel superior ──────────────────────────────────────
        JPanel panelSuperior = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 8));
        panelSuperior.setBackground(new Color(45, 45, 48));

        lblEstado = new JLabel("💣 " + TOTAL_MINAS + " minas");
        lblEstado.setForeground(Color.WHITE);
        lblEstado.setFont(new Font("Segoe UI Emoji", Font.BOLD, 16));

        lblTiempo = new JLabel("⏱ 0s");
        lblTiempo.setForeground(new Color(100, 200, 255));
        lblTiempo.setFont(new Font("Segoe UI Emoji", Font.BOLD, 16));

        btnReiniciar = new JButton("↺ Nuevo");
        btnReiniciar.setBackground(new Color(70, 130, 180));
        btnReiniciar.setForeground(Color.WHITE);
        btnReiniciar.setFocusPainted(false);
        btnReiniciar.setFont(new Font("Segoe UI", Font.BOLD, 13));
        btnReiniciar.setBorder(BorderFactory.createEmptyBorder(6, 14, 6, 14));
        btnReiniciar.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        btnReiniciar.addActionListener(e -> iniciarJuego());

        panelSuperior.add(lblEstado);
        panelSuperior.add(btnReiniciar);
        panelSuperior.add(lblTiempo);
        add(panelSuperior, BorderLayout.NORTH);

        // ── Panel del tablero ───────────────────────────────────
        panelTablero = new JPanel();
        panelTablero.setBorder(BorderFactory.createEmptyBorder(4, 8, 8, 8));
        panelTablero.setBackground(new Color(45, 45, 48));
        add(panelTablero, BorderLayout.CENTER);
    }

    /**
     * Inicia (o reinicia) una partida completa.
     */
    private void iniciarJuego() {
        // Detener timer anterior si existe
        if (timer != null) timer.stop();
        segundos = 0;
        lblTiempo.setText("⏱ 0s");
        lblEstado.setText("💣 " + TOTAL_MINAS + " minas");

        // Crear nuevo tablero (encapsulado en la clase Tablero)
        tablero = new Tablero(FILAS, COLUMNAS, TOTAL_MINAS);

        // Construir la grilla visual
        panelTablero.removeAll();
        panelTablero.setLayout(new GridLayout(FILAS, COLUMNAS, 2, 2));

        for (int f = 0; f < FILAS; f++) {
            for (int c = 0; c < COLUMNAS; c++) {
                // ================================================
                //  POLIMORFISMO
                // ================================================
                //  getCelda() retorna CeldaBase, pero el objeto
                //  real puede ser CeldaSegura o CeldaMina.
                //  Le agregamos el listener sin conocer el tipo
                //  exacto. El botón ya es el objeto celda mismo.
                // ================================================
                CeldaBase celda = tablero.getCelda(f, c);
                celda.setPreferredSize(new Dimension(46, 46));
                agregarListeners(celda, f, c);
                panelTablero.add(celda);
            }
        }

        panelTablero.revalidate();
        panelTablero.repaint();
        pack();

        // Iniciar cronómetro
        timer = new Timer(1000, e -> {
            segundos++;
            lblTiempo.setText("⏱ " + segundos + "s");
        });
        timer.start();
    }

    /**
     * Agrega los listeners de ratón a cada celda.
     * PRIVADO — detalle de implementación encapsulado.
     *
     * MouseAdapter es un ejemplo de HERENCIA en la API de Java:
     * extiende MouseAdapter (que implementa MouseListener)
     * y solo sobreescribimos lo que necesitamos.
     */
    private void agregarListeners(CeldaBase celda, int fila, int col) {
        celda.addMouseListener(new MouseAdapter() {
            // ====================================================
            //  POLIMORFISMO — Sobrescritura (Override)
            // ====================================================
            //  mouseClicked está en MouseAdapter; lo sobreescribimos
            //  con @Override para definir nuestro comportamiento.
            // ====================================================
            @Override
            public void mouseClicked(MouseEvent e) {
                if (tablero.isJuegoTerminado()) return;

                if (SwingUtilities.isRightMouseButton(e)) {
                    // Clic derecho → marcar/desmarcar bandera
                    tablero.marcarCelda(fila, col);

                } else if (SwingUtilities.isLeftMouseButton(e)) {
                    // Clic izquierdo → revelar celda
                    boolean continua = tablero.revelarCelda(fila, col);

                    if (!continua) {
                        manejarDerrota();
                    } else if (tablero.isGanado()) {
                        manejarVictoria();
                    }
                }
            }
        });
    }

    /**
     * Lógica al perder: para el timer y muestra mensaje.
     * Encapsulamiento: la reacción visual está centralizada aquí.
     */
    private void manejarDerrota() {
        timer.stop();
        lblEstado.setText("💥 ¡Pisaste una mina!");
        lblEstado.setForeground(new Color(255, 80, 80));

        // Pequeña pausa antes del diálogo
        Timer delay = new Timer(600, e -> {
            int op = JOptionPane.showConfirmDialog(
                VentanaBuscaMinas.this,
                "💥 ¡Juego terminado!\nTiempo: " + segundos + "s\n¿Volver a jugar?",
                "Perdiste", JOptionPane.YES_NO_OPTION);
            if (op == JOptionPane.YES_OPTION) iniciarJuego();
        });
        delay.setRepeats(false);
        delay.start();
    }

    /**
     * Lógica al ganar: para el timer y felicita.
     */
    private void manejarVictoria() {
        timer.stop();
        lblEstado.setText("🏆 ¡Ganaste!");
        lblEstado.setForeground(new Color(100, 220, 100));

        Timer delay = new Timer(400, e -> {
            JOptionPane.showMessageDialog(
                VentanaBuscaMinas.this,
                "🏆 ¡Felicitaciones!\nCompletaste el tablero en " + segundos + " segundos.",
                "¡Ganaste!", JOptionPane.INFORMATION_MESSAGE);
            iniciarJuego();
        });
        delay.setRepeats(false);
        delay.start();
    }

    // ============================================================
    //  MÉTODO main — punto de entrada del programa
    // ============================================================
    public static void main(String[] args) {
        // Ejecutar en el hilo de eventos de Swing (buena práctica)
        SwingUtilities.invokeLater(() -> new VentanaBuscaMinas());
    }
}
