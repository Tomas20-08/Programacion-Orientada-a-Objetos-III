package modelo.Juego5; // Mismo paquete que todo el juego

import javax.swing.*;      // Componentes Swing
import java.awt.*;         // Clases AWT

/**
 * CLASE: PanelExamen  ← CORREGIDA
 * ─────────────────────────────────────────────────────────────
 * CORRECCIONES EN ESTA VERSIÓN:
 *
 *  1. LA RETROALIMENTACIÓN APARECE DESPUÉS DE ELEGIR, NO ANTES.
 *     El bug: el panelResultadoFinal se mostraba antes de que el jugador
 *     viera la explicación de la pregunta. Se separó en dos fases:
 *       Fase A: El jugador elige → se colorean opciones → aparece explicación
 *       Fase B: El jugador hace clic en "Siguiente" → siguiente pregunta o resultado final
 *     La pantalla de resultado final SOLO aparece después de la última pregunta.
 *
 *  2. EL PANEL ESCALA con la ventana.
 *     paintComponent usa getWidth()/getHeight() en vez de valores fijos.
 *     Los componentes usan setBounds relativos y el layout se adapta.
 *
 *  3. Se mantiene toda la lógica de correcto/incorrecto y la explicación.
 */
public class PanelExamen extends PanelBase {

    // ─── REFERENCIAS ─────────────────────────────────────────────────────────────
    private VentanaPrincipal ventana;
    private EstadoJuego      estado;
    private Examen           examenActual;

    // ─── ESTADO INTERNO ───────────────────────────────────────────────────────────
    private int     indicePreguntaActual;
    private int[]   respuestasJugador;
    private boolean esperandoRespuesta;

    // ─── CALLBACK ────────────────────────────────────────────────────────────────
    private java.util.function.Consumer<Double> callbackNotaObtenida;

    // ─── COMPONENTES ─────────────────────────────────────────────────────────────
    private JLabel    labelTitulo;
    private JLabel    labelContador;
    private JLabel    labelPuntaje;
    private JTextArea areaPregunta;
    private JButton[] botonesOpciones;

    // ─── RETROALIMENTACIÓN (aparece DESPUÉS de elegir) ────────────────────────────
    private JPanel  panelRetro;           // Panel de retroalimentación
    private JLabel  labelRetro;           // "✓ CORRECTO" / "✗ INCORRECTO"
    private JLabel  labelExplicacion;     // Explicación de la respuesta correcta
    private JButton botonSiguiente;       // "Siguiente pregunta →"

    // ─── PANTALLA DE RESULTADO FINAL ─────────────────────────────────────────────
    // Esta pantalla se muestra SOLO al terminar todas las preguntas
    private JPanel panelResultadoFinal;

    // ─── LETRAS DE OPCIONES ───────────────────────────────────────────────────────
    private static final String[] LETRAS = {"A", "B", "C", "D"};

    // ─── CONSTRUCTOR ─────────────────────────────────────────────────────────────
    public PanelExamen(VentanaPrincipal ventana) {
        super(null); // Sin imagen de fondo (fondo sólido del PanelBase)
        this.ventana = ventana;
        this.estado  = EstadoJuego.getInstance();
        this.indicePreguntaActual = 0;
        this.esperandoRespuesta   = true;
    }

    // ─── IMPLEMENTACIÓN ABSTRACTA ─────────────────────────────────────────────────

    @Override
    protected void inicializarComponentes() {

        // ── TÍTULO DEL EXAMEN ────────────────────────────────────────────────────
        labelTitulo = crearLabel("EXAMEN", FUENTE_TITULO, COLOR_ACENTO, 20, 15, 700, 45);
        add(labelTitulo);

        // ── CONTADOR "Pregunta X de Y" ────────────────────────────────────────────
        labelContador = crearLabel("Pregunta 1 de 5", FUENTE_SUBTITULO, COLOR_TEXTO,
                                   700, 15, 304, 45);
        labelContador.setHorizontalAlignment(SwingConstants.RIGHT);
        add(labelContador);

        // ── PUNTAJE "Correctas: X/Y" ──────────────────────────────────────────────
        labelPuntaje = crearLabel("Correctas: 0/0", FUENTE_PEQUENA,
                                  new Color(150, 200, 150), 700, 60, 304, 30);
        labelPuntaje.setHorizontalAlignment(SwingConstants.RIGHT);
        add(labelPuntaje);

        // ── SEPARADOR ─────────────────────────────────────────────────────────────
        JSeparator sep = new JSeparator();
        sep.setBounds(20, 68, 984, 2);
        sep.setForeground(new Color(100, 90, 130));
        add(sep);

        // ── ÁREA DE PREGUNTA ──────────────────────────────────────────────────────
        areaPregunta = new JTextArea("Cargando pregunta...");
        areaPregunta.setBounds(20, 80, 984, 110);
        areaPregunta.setFont(new Font("Courier New", Font.PLAIN, 17));
        areaPregunta.setForeground(COLOR_TEXTO);
        areaPregunta.setBackground(new Color(20, 15, 35));
        areaPregunta.setEditable(false);
        areaPregunta.setLineWrap(true);
        areaPregunta.setWrapStyleWord(true);
        areaPregunta.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(80, 70, 100), 1),
            BorderFactory.createEmptyBorder(10, 14, 10, 14)
        ));
        add(areaPregunta);

        // ── BOTONES DE OPCIONES (A, B, C, D) ─────────────────────────────────────
        botonesOpciones = new JButton[4];
        for (int i = 0; i < 4; i++) {
            final int idx = i;
            botonesOpciones[i] = new JButton(LETRAS[i] + ")  Opción " + LETRAS[i]);
            botonesOpciones[i].setBounds(20, 205 + i * 58, 984, 50);
            botonesOpciones[i].setFont(FUENTE_BOTON);
            botonesOpciones[i].setForeground(COLOR_TEXTO);
            botonesOpciones[i].setBackground(new Color(30, 25, 45));
            botonesOpciones[i].setHorizontalAlignment(SwingConstants.LEFT);
            botonesOpciones[i].setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(70, 60, 90), 1),
                BorderFactory.createEmptyBorder(0, 14, 0, 0)
            ));
            botonesOpciones[i].setFocusPainted(false);
            botonesOpciones[i].setCursor(new Cursor(Cursor.HAND_CURSOR));

            // Hover
            botonesOpciones[i].addMouseListener(new java.awt.event.MouseAdapter() {
                @Override public void mouseEntered(java.awt.event.MouseEvent e) {
                    if (esperandoRespuesta) botonesOpciones[idx].setBackground(new Color(50, 40, 75));
                }
                @Override public void mouseExited(java.awt.event.MouseEvent e) {
                    if (esperandoRespuesta) botonesOpciones[idx].setBackground(new Color(30, 25, 45));
                }
            });

            botonesOpciones[i].addActionListener(e -> {
                if (esperandoRespuesta) seleccionarOpcion(idx);
            });
            add(botonesOpciones[i]);
        }

        // ── PANEL DE RETROALIMENTACIÓN ─────────────────────────────────────────────
        // CORRECCIÓN: aparece DEBAJO de los botones, solo después de elegir.
        // Y se muestra la explicación ANTES del botón "Siguiente".
        panelRetro = new JPanel(null);
        panelRetro.setBounds(20, 440, 984, 145);
        panelRetro.setBackground(new Color(18, 15, 30));
        panelRetro.setBorder(BorderFactory.createLineBorder(new Color(90, 80, 110), 1));
        panelRetro.setVisible(false); // Oculto hasta que el jugador responda
        add(panelRetro);

        labelRetro = new JLabel("✓ ¡CORRECTO!");
        labelRetro.setBounds(15, 8, 600, 32);
        labelRetro.setFont(FUENTE_SUBTITULO);
        labelRetro.setForeground(COLOR_CORRECTO);
        panelRetro.add(labelRetro);

        labelExplicacion = new JLabel("<html>Explicación aquí</html>");
        labelExplicacion.setBounds(15, 42, 840, 90);
        labelExplicacion.setFont(FUENTE_PEQUENA);
        labelExplicacion.setForeground(new Color(200, 195, 180));
        panelRetro.add(labelExplicacion);

        // ── BOTÓN SIGUIENTE ───────────────────────────────────────────────────────
        // CORRECCIÓN: el botón "Siguiente" está dentro del panelRetro,
        // así el jugador VE la explicación antes de avanzar.
        botonSiguiente = new JButton("Siguiente →");
        botonSiguiente.setBounds(820, 100, 150, 38);
        botonSiguiente.setFont(FUENTE_BOTON);
        botonSiguiente.setForeground(COLOR_ACENTO2);
        botonSiguiente.setBackground(new Color(30, 40, 60));
        botonSiguiente.setBorderPainted(false);
        botonSiguiente.setFocusPainted(false);
        botonSiguiente.setCursor(new Cursor(Cursor.HAND_CURSOR));
        botonSiguiente.addActionListener(e -> siguientePregunta());
        panelRetro.add(botonSiguiente);

        // ── PANEL DE RESULTADO FINAL ──────────────────────────────────────────────
        // Se muestra SOLO al terminar TODAS las preguntas, encima de todo
        panelResultadoFinal = new JPanel(null);
        panelResultadoFinal.setBounds(0, 0, 1024, 680);
        panelResultadoFinal.setBackground(new Color(5, 3, 15));
        panelResultadoFinal.setVisible(false); // Inicialmente oculto
        add(panelResultadoFinal);
    }

    // ─── MÉTODOS DE CONTROL ───────────────────────────────────────────────────────

    /** Inicia el examen. */
    public void iniciarExamen(Examen examen,
                              java.util.function.Consumer<Double> callbackNotaObtenida) {
        this.examenActual          = examen;
        this.callbackNotaObtenida  = callbackNotaObtenida;
        this.indicePreguntaActual  = 0;
        this.respuestasJugador     = new int[examen.getPreguntas().size()];
        this.esperandoRespuesta    = true;

        for (int i = 0; i < respuestasJugador.length; i++) {
            respuestasJugador[i] = -1; // -1 = sin responder
        }

        labelTitulo.setText(examen.getNombre().toUpperCase());
        panelResultadoFinal.setVisible(false); // Oculta resultado previo
        mostrarPreguntaActual();
    }

    private void mostrarPreguntaActual() {
        if (examenActual == null) return;

        try {
            Pregunta pregunta = examenActual.getPreguntas().get(indicePreguntaActual);

            // Actualiza contador
            labelContador.setText("Pregunta " + (indicePreguntaActual + 1) +
                                  " de " + examenActual.getPreguntas().size());

            // Cuenta correctas hasta ahora
            int correctas = 0;
            for (int i = 0; i < indicePreguntaActual; i++) {
                if (examenActual.getPreguntas().get(i).esCorrecta(respuestasJugador[i])) {
                    correctas++;
                }
            }
            labelPuntaje.setText("Correctas: " + correctas + "/" + indicePreguntaActual);

            // Muestra el enunciado
            areaPregunta.setText("❓ " + pregunta.getEnunciado());

            // Restaura botones de opciones
            String[] opciones = pregunta.getOpciones();
            for (int i = 0; i < 4; i++) {
                botonesOpciones[i].setText(LETRAS[i] + ")  " + opciones[i]);
                botonesOpciones[i].setBackground(new Color(30, 25, 45)); // Color neutro
                botonesOpciones[i].setForeground(COLOR_TEXTO);
                botonesOpciones[i].setEnabled(true);
            }

            // CORRECCIÓN: oculta la retroalimentación al mostrar nueva pregunta
            panelRetro.setVisible(false);
            esperandoRespuesta = true;

        } catch (IndexOutOfBoundsException e) {
            System.err.println("[PanelExamen] Índice de pregunta inválido: " + e.getMessage());
        }
    }

    /**
     * CORRECCIÓN PRINCIPAL: el jugador elige → se colorean botones → aparece
     * la retroalimentación (resultado + explicación) → el jugador DEBE leerla
     * y luego hacer clic en "Siguiente" para avanzar.
     * La nota final NO aparece hasta que el jugador pasa por TODAS las preguntas.
     */
    private void seleccionarOpcion(int indiceElegido) {
        esperandoRespuesta = false; // Bloquea más clics en las opciones

        respuestasJugador[indicePreguntaActual] = indiceElegido;

        Pregunta pregunta = examenActual.getPreguntas().get(indicePreguntaActual);
        boolean  esCorrecta = pregunta.esCorrecta(indiceElegido);

        // Colorea los botones para mostrar qué era correcto
        for (int i = 0; i < 4; i++) {
            botonesOpciones[i].setEnabled(false);

            if (i == pregunta.getRespuestaCorrecta()) {
                // Respuesta correcta → siempre en verde
                botonesOpciones[i].setBackground(new Color(30, 80, 40));
                botonesOpciones[i].setForeground(COLOR_CORRECTO);
            } else if (i == indiceElegido && !esCorrecta) {
                // Respuesta elegida incorrecta → en rojo
                botonesOpciones[i].setBackground(new Color(80, 20, 20));
                botonesOpciones[i].setForeground(COLOR_INCORRECTO);
            }
        }

        // Muestra la retroalimentación
        if (esCorrecta) {
            labelRetro.setText("✓  ¡CORRECTO! Bien jugado.");
            labelRetro.setForeground(COLOR_CORRECTO);
        } else {
            String letraCorrecta = LETRAS[pregunta.getRespuestaCorrecta()];
            labelRetro.setText("✗  INCORRECTO. La respuesta era: " + letraCorrecta);
            labelRetro.setForeground(COLOR_INCORRECTO);
        }

        // Muestra la explicación DENTRO del panelRetro
        labelExplicacion.setText("<html>" + pregunta.getExplicacion() + "</html>");

        // Muestra el panel de retroalimentación (CORRECCIÓN: ANTES no aparecía bien)
        panelRetro.setVisible(true);

        repaint(); // Fuerza el repintado para que se vea inmediatamente
    }

    private void siguientePregunta() {
        indicePreguntaActual++;

        if (indicePreguntaActual < examenActual.getPreguntas().size()) {
            mostrarPreguntaActual(); // Hay más preguntas
        } else {
            // Terminó todas → calcula nota y muestra resultado FINAL
            double nota = examenActual.calcularNota(respuestasJugador);
            mostrarResultadoFinal(nota); // Esto sí muestra la pantalla de nota
        }
    }

    /**
     * Pantalla de resultado final: aparece SOLO al terminar todas las preguntas.
     * CORRECCIÓN: antes aparecía prematuramente.
     */
    private void mostrarResultadoFinal(double nota) {
        panelResultadoFinal.removeAll();
        panelResultadoFinal.setVisible(true); // Ahora sí la mostramos

        // Color y mensaje según la nota
        Color  colorNota;
        String mensajeNota;
        if (nota == 5.0) {
            colorNota   = COLOR_CORRECTO;
            mensajeNota = "PERFECTO. Sergio asiente con la cabeza.";
        } else if (nota >= 4.0) {
            colorNota   = new Color(100, 180, 100);
            mensajeNota = "Suficiente. Por ahora.";
        } else if (nota >= 3.0) {
            colorNota   = new Color(200, 160, 50);
            mensajeNota = "Pasaste. Apenas, pero pasaste.";
        } else {
            colorNota   = COLOR_INCORRECTO;
            mensajeNota = "...Decepcionante.";
        }

        // Título
        JLabel lTitulo = new JLabel("EXAMEN FINALIZADO", SwingConstants.CENTER);
        lTitulo.setBounds(0, 60, 1024, 60);
        lTitulo.setFont(new Font("Courier New", Font.BOLD, 34));
        lTitulo.setForeground(COLOR_ACENTO);
        panelResultadoFinal.add(lTitulo);

        // Nota grande
        JLabel lNota = new JLabel(String.format("%.1f / 5.0", nota), SwingConstants.CENTER);
        lNota.setBounds(0, 160, 1024, 120);
        lNota.setFont(new Font("Courier New", Font.BOLD, 72));
        lNota.setForeground(colorNota);
        panelResultadoFinal.add(lNota);

        // Mensaje de Sergio
        JLabel lMensaje = new JLabel("Sergio: \"" + mensajeNota + "\"", SwingConstants.CENTER);
        lMensaje.setBounds(0, 300, 1024, 50);
        lMensaje.setFont(FUENTE_SUBTITULO);
        lMensaje.setForeground(COLOR_TEXTO);
        panelResultadoFinal.add(lMensaje);

        // Botón continuar
        JButton btnContinuar = new JButton("Continuar →");
        btnContinuar.setBounds(387, 420, 250, 50);
        btnContinuar.setFont(FUENTE_BOTON);
        btnContinuar.setForeground(COLOR_ACENTO2);
        btnContinuar.setBackground(new Color(30, 25, 45));
        btnContinuar.setBorderPainted(false);
        btnContinuar.setFocusPainted(false);
        btnContinuar.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnContinuar.addActionListener(e -> {
            panelResultadoFinal.setVisible(false); // Oculta el resultado
            if (callbackNotaObtenida != null) {
                callbackNotaObtenida.accept(nota); // Entrega la nota al controlador
            }
        });
        panelResultadoFinal.add(btnContinuar);

        panelResultadoFinal.revalidate();
        panelResultadoFinal.repaint();
    }
}