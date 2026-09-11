package modelo.Juego5; // Paquete de la vista

import javax.swing.*;         // Componentes Swing
import java.awt.*;            // Clases AWT
import java.awt.event.*;      // Eventos


/**
 * CLASE: PanelFinal
 * ─────────────────────────────────────────────────────────────
 * Panel que muestra los tres finales del juego:
 *
 *   Final 0 - MALO:    Imagen FinalMalo a pantalla completa + "GAME OVER PAPU"
 *   Final 1 - BUENO:   Imagen FinalBueno + texto de felicitaciones
 *   Final 2 - OCULTO:  Monólogo de Sergio rompiendo la cuarta pared → cierre abrupto
 *
 * Para el Final Oculto, el texto dura ~3 minutos en tiempo real:
 * hay 40 líneas × 4.5 segundos por línea ≈ 180 segundos = 3 minutos.
 *
 * Aplica: Herencia (extiende PanelBase), Timer de Swing, efectos visuales
 */
public class PanelFinal extends PanelBase {

    // ─── REFERENCIAS ─────────────────────────────────────────────────────────────
    private VentanaPrincipal ventana;  // Ventana principal (para poder cerrarla abruptamente)

    // ─── ESTADO DEL FINAL ─────────────────────────────────────────────────────────
    private int tipoFinal;              // 0=Malo, 1=Bueno, 2=Oculto
    private String[] dialogosFinal;     // Array de líneas del diálogo final
    private int indiceDialogo;          // Índice de la línea actual
    private boolean finalOcultoCorriendo; // True = el final oculto está en curso

    // ─── COMPONENTES GRÁFICOS ────────────────────────────────────────────────────
    private JLabel labelImagenFinal;    // Muestra la imagen de final a pantalla completa
    private JPanel panelOverlay;        // Overlay semitransparente encima de la imagen
    private JLabel labelTextoFinal;     // Texto principal (GAME OVER PAPU / felicitaciones)
    private JTextArea areaDialogoFinal; // Para los diálogos del final oculto
    private JButton botonReintentar;    // "Volver a intentarlo" (solo en Final Malo y Bueno)
    private JLabel labelParpadeo;       // Texto parpadeante (presiona clic para continuar)

    // ─── TIMERS ───────────────────────────────────────────────────────────────────
    private Timer timerDialogoOculto;  // Timer que avanza los diálogos del Final Oculto automáticamente
    private Timer timerParpadeo;       // Timer que hace parpadear el texto "presiona para continuar"
    private Timer timerCierre;         // Timer para el cierre abrupto del Final Oculto

    // ─── CONSTRUCTOR ─────────────────────────────────────────────────────────────

    /**
     * Constructor de PanelFinal.
     * @param ventana referencia a la ventana principal
     */
    public PanelFinal(VentanaPrincipal ventana) {
        super(null);             // Sin imagen de fondo (usamos imagen de final completa)
        this.ventana = ventana;  // Guarda la referencia
    }

    // ─── IMPLEMENTACIÓN ABSTRACTA ─────────────────────────────────────────────────

    /**
     * Inicializa todos los componentes gráficos del panel de final.
     */
    @Override
    protected void inicializarComponentes() {

        // ── IMAGEN DE FINAL (pantalla completa) ────────────────────────────────
        // JLabel que ocupa toda la pantalla y muestra la imagen del final correspondiente
        labelImagenFinal = new JLabel();
        labelImagenFinal.setBounds(0, 0, 1024, 680);             // Cubre TODA la pantalla
        labelImagenFinal.setHorizontalAlignment(SwingConstants.CENTER); // Centra la imagen
        labelImagenFinal.setVerticalAlignment(SwingConstants.CENTER);   // Centra verticalmente
        add(labelImagenFinal); // Agrega al panel (se pinta debajo de todo)

        // ── OVERLAY semitransparente (encima de la imagen, debajo del texto) ────
        panelOverlay = new JPanel(null); // Sin layout manager
        panelOverlay.setBounds(0, 0, 1024, 680);              // Cubre toda la pantalla
        panelOverlay.setBackground(new Color(0, 0, 0, 160));  // Negro con 63% de opacidad
        panelOverlay.setOpaque(true);                          // Necesario para que se vea
        add(panelOverlay); // Agrega encima de la imagen

        // ── TEXTO PRINCIPAL (GAME OVER PAPU / título del final) ──────────────────
        // <html> permite centrar el texto con HTML básico
        labelTextoFinal = new JLabel("<html><center>GAME OVER PAPU</center></html>");
        labelTextoFinal.setBounds(0, 180, 1024, 120);            // Posición centrada verticalmente
        labelTextoFinal.setFont(new Font("Courier New", Font.BOLD, 52)); // Fuente enorme e impactante
        labelTextoFinal.setForeground(COLOR_ACENTO);             // Rojo del juego
        labelTextoFinal.setHorizontalAlignment(SwingConstants.CENTER); // Centrado horizontal
        panelOverlay.add(labelTextoFinal); // Se agrega al overlay

        // ── ÁREA DE DIÁLOGO (para el Final Oculto, monólogo de Sergio) ──────────
        areaDialogoFinal = new JTextArea();
        areaDialogoFinal.setBounds(80, 200, 864, 300);           // Centrada en pantalla
        areaDialogoFinal.setFont(new Font("Courier New", Font.PLAIN, 19)); // Fuente grande
        areaDialogoFinal.setForeground(new Color(230, 220, 200)); // Blanco crema
        areaDialogoFinal.setBackground(new Color(0, 0, 0, 0));   // Transparente
        areaDialogoFinal.setOpaque(false);                        // Sin fondo
        areaDialogoFinal.setEditable(false);                      // Solo lectura
        areaDialogoFinal.setLineWrap(true);                       // Ajuste de línea
        areaDialogoFinal.setWrapStyleWord(true);                  // Por palabras
        areaDialogoFinal.setFocusable(false);                     // No recibe foco
        areaDialogoFinal.setVisible(false);                       // Inicialmente oculto
        panelOverlay.add(areaDialogoFinal); // Se agrega al overlay

        // ── TEXTO PARPADEANTE ("Haz clic para continuar") ─────────────────────────
        labelParpadeo = new JLabel("[ Haz clic para continuar ]");
        labelParpadeo.setBounds(0, 580, 1024, 40);               // Parte inferior de la pantalla
        labelParpadeo.setFont(FUENTE_PEQUENA);                   // Fuente pequeña del juego
        labelParpadeo.setForeground(new Color(180, 160, 90));    // Dorado tenue
        labelParpadeo.setHorizontalAlignment(SwingConstants.CENTER); // Centrado
        panelOverlay.add(labelParpadeo); // Se agrega al overlay

        // ── BOTÓN REINTENTAR (Final Malo y Bueno) ────────────────────────────────
        botonReintentar = crearBotonEstilizado("↩ Volver al inicio", 387, 500, 250, 50);
        botonReintentar.setForeground(COLOR_ACENTO2); // Azul eléctrico
        botonReintentar.setVisible(false);            // Inicialmente oculto
        botonReintentar.addActionListener(e -> reiniciarJuego()); // Al hacer clic reinicia
        panelOverlay.add(botonReintentar); // Se agrega al overlay

        // ── TIMER DE PARPADEO (hace parpadear el texto de "clic para continuar") ──
        // Cada 700ms cambia la visibilidad del label (efecto parpadeo)
        timerParpadeo = new Timer(700, e -> {
            // Toggle de visibilidad: si era visible lo oculta, si era oculto lo muestra
            labelParpadeo.setVisible(!labelParpadeo.isVisible());
        });

        // ── TIMER DEL FINAL OCULTO (avanza los diálogos automáticamente) ─────────
        // Cada 4500ms (4.5 segundos) avanza al siguiente diálogo del monólogo de Sergio
        // 40 líneas × 4.5s = 180s = 3 minutos exactos de monólogo
        timerDialogoOculto = new Timer(4500, e -> {
            avanzarDialogoOculto(); // Muestra la siguiente línea
        });

        // ── TIMER DE CIERRE ABRUPTO (se activa al final del monólogo oculto) ──────
        // Espera 3 segundos después de la última línea y cierra el juego
        timerCierre = new Timer(3000, e -> {
            timerCierre.stop();          // Detiene este timer
            ventana.cerrarAbruptamente(); // ¡CIERRA EL JUEGO DE GOLPE!
        });
        timerCierre.setRepeats(false); // Solo se dispara UNA vez (no se repite)

        // ── DETECTOR DE CLIC EN TODO EL PANEL (para avanzar en Final Malo/Bueno) ──
        panelOverlay.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                // Solo en Final Malo sin el monólogo: el clic muestra el botón reiniciar
                if (tipoFinal == 0 && !finalOcultoCorriendo) {
                    botonReintentar.setVisible(true); // Muestra el botón de reinicio
                    labelParpadeo.setVisible(false);  // Oculta el texto parpadeante
                    timerParpadeo.stop();             // Detiene el parpadeo
                }
            }
        });
    }

    // ─── MÉTODO PRINCIPAL: iniciar un final ───────────────────────────────────────

    /**
     * Configura y arranca el final correspondiente.
     * Es llamado por VentanaPrincipal.mostrarFinal().
     *
     * @param tipoFinal    0=Malo, 1=Bueno, 2=Oculto
     * @param mensajeExtra texto adicional (razón del game over, nota final, etc.)
     */
    public void iniciarFinal(int tipoFinal, String mensajeExtra) {
        this.tipoFinal = tipoFinal; // Guarda el tipo de final

        // Detiene timers anteriores para evitar conflictos si se repite
        timerParpadeo.stop();
        timerDialogoOculto.stop();
        timerCierre.stop();

        // Reinicia el estado
        indiceDialogo = 0;
        finalOcultoCorriendo = false;
        botonReintentar.setVisible(false); // Oculta el botón al inicio

        // Elige qué configurar según el tipo de final
        switch (tipoFinal) {
            case 0: configurarFinalMalo(mensajeExtra);   break; // Final Malo
            case 1: configurarFinalBueno(mensajeExtra);  break; // Final Bueno
            case 2: configurarFinalOculto();             break; // Final Oculto
        }

        // Inicia el parpadeo del texto "clic para continuar"
        timerParpadeo.start(); // Arranca el efecto parpadeante

        repaint(); // Repinta el panel con todos los cambios aplicados
    }

    // ─── CONFIGURACIÓN DE CADA FINAL ─────────────────────────────────────────────

    /**
     * Configura el FINAL MALO (FinalMalo.png + "GAME OVER PAPU").
     * @param razon texto que explica por qué llegó al final malo
     */
    private void configurarFinalMalo(String razon) {
        // Carga la imagen FinalMalo.png y la muestra escalada a pantalla completa
        Image imgFinalMalo = GestorRecursos.getInstance().getSprite("FinalMalo");
        if (imgFinalMalo != null) {
            // Escala la imagen al tamaño de la pantalla manteniendo proporción
            Image imgEscalada = imgFinalMalo.getScaledInstance(1024, 680, Image.SCALE_SMOOTH);
            labelImagenFinal.setIcon(new ImageIcon(imgEscalada)); // Asigna la imagen escalada
        }

        // Configura el overlay para el Final Malo
        panelOverlay.setBackground(new Color(0, 0, 0, 130)); // Overlay oscuro
        areaDialogoFinal.setVisible(false); // Sin diálogo extra en el final malo

        // Muestra la razón del Game Over (si la hay) encima del "GAME OVER PAPU"
        if (razon != null && !razon.isEmpty()) {
            // Muestra la razón como texto secundario encima del título
            JLabel labelRazon = new JLabel("<html><center>" + razon + "</center></html>");
            labelRazon.setBounds(50, 100, 924, 80);
            labelRazon.setFont(new Font("Courier New", Font.PLAIN, 18));
            labelRazon.setForeground(new Color(210, 200, 180));
            labelRazon.setHorizontalAlignment(SwingConstants.CENTER);
            panelOverlay.add(labelRazon); // Agrega al overlay
        }

        // Configura el texto principal: "GAME OVER PAPU" exactamente como lo pide el cliente
        labelTextoFinal.setText("<html><center>GAME OVER PAPU</center></html>");
        labelTextoFinal.setForeground(COLOR_ACENTO); // Rojo
        labelTextoFinal.setBounds(0, 310, 1024, 120);
        labelTextoFinal.setFont(new Font("Courier New", Font.BOLD, 52));
        labelTextoFinal.setVisible(true); // Hace visible el texto
    }

    /**
     * Configura el FINAL BUENO (FinalBueno.png + mensaje de felicitaciones).
     * @param notaFinalStr texto con la nota final acumulada
     */
    private void configurarFinalBueno(String notaFinalStr) {
        // Carga la imagen FinalBueno.png
        Image imgFinalBueno = GestorRecursos.getInstance().getSprite("FinalBueno");
        if (imgFinalBueno != null) {
            Image imgEscalada = imgFinalBueno.getScaledInstance(1024, 680, Image.SCALE_SMOOTH);
            labelImagenFinal.setIcon(new ImageIcon(imgEscalada));
        }

        // El Final Bueno tiene un overlay más suave (no tan oscuro) para que se vea la imagen
        panelOverlay.setBackground(new Color(0, 0, 0, 80));
        areaDialogoFinal.setVisible(false);

        // Texto de felicitaciones
        labelTextoFinal.setText("<html><center>¡FELICITACIONES!</center></html>");
        labelTextoFinal.setForeground(new Color(100, 220, 120)); // Verde
        labelTextoFinal.setBounds(0, 80, 1024, 100);
        labelTextoFinal.setFont(new Font("Courier New", Font.BOLD, 48));
        labelTextoFinal.setVisible(true);

        // Nota debajo del título
        if (notaFinalStr != null) {
            JLabel labelNota = new JLabel("<html><center>" + notaFinalStr + "</center></html>");
            labelNota.setBounds(0, 190, 1024, 50);
            labelNota.setFont(new Font("Courier New", Font.PLAIN, 22));
            labelNota.setForeground(new Color(200, 220, 180));
            labelNota.setHorizontalAlignment(SwingConstants.CENTER);
            panelOverlay.add(labelNota);
        }

        // Mensaje de conclusión
        JLabel labelMensaje = new JLabel(
            "<html><center>Eres el primer estudiante en aprobar el curso del Maestro Sergio Mora.</center></html>");
        labelMensaje.setBounds(80, 250, 864, 80);
        labelMensaje.setFont(new Font("Courier New", Font.PLAIN, 18));
        labelMensaje.setForeground(new Color(210, 200, 180));
        labelMensaje.setHorizontalAlignment(SwingConstants.CENTER);
        panelOverlay.add(labelMensaje);

        // En el Final Bueno siempre muestra el botón de reintentar (para rejugabilidad)
        botonReintentar.setText("↩ Jugar de nuevo");
        botonReintentar.setVisible(true);       // Visible desde el principio
        labelParpadeo.setVisible(false);        // No necesita el texto parpadeante
        timerParpadeo.stop();                   // Detiene el parpadeo
    }

    /**
     * Configura el FINAL OCULTO (FinalOculto.png + monólogo de Sergio que rompe la 4ta pared).
     * Duración total: ~3 minutos en tiempo real.
     */
    private void configurarFinalOculto() {
        // Carga la imagen FinalOculto.png
        Image imgFinalOculto = GestorRecursos.getInstance().getSprite("FinalOculto");
        if (imgFinalOculto != null) {
            Image imgEscalada = imgFinalOculto.getScaledInstance(1024, 680, Image.SCALE_SMOOTH);
            labelImagenFinal.setIcon(new ImageIcon(imgEscalada));
        }

        // Overlay MUY oscuro para el final oculto (atmósfera de terror)
        panelOverlay.setBackground(new Color(0, 0, 0, 195));

        // Oculta el título estático (el final oculto solo tiene el monólogo)
        labelTextoFinal.setVisible(false);

        // Obtiene el array de diálogos del final oculto del GestorDialogos
        dialogosFinal = GestorDialogos.getFinalOculto();
        indiceDialogo = 0; // Empieza desde la primera línea

        // Configura el área de diálogo para el monólogo
        areaDialogoFinal.setText(""); // Empieza vacío
        areaDialogoFinal.setVisible(true); // Hace visible el área de diálogo
        areaDialogoFinal.setBounds(80, 150, 864, 350); // Posición centrada

        finalOcultoCorriendo = true; // Marca que el final oculto está activo

        // Muestra la primera línea del monólogo inmediatamente
        if (dialogosFinal.length > 0) {
            areaDialogoFinal.setText(dialogosFinal[0]); // Muestra la primera línea
        }

        // Inicia el timer que avanza las líneas automáticamente cada 4.5 segundos
        timerDialogoOculto.start();
    }

    // ─── AVANCE DEL MONÓLOGO OCULTO ───────────────────────────────────────────────

    /**
     * Avanza al siguiente diálogo del Final Oculto.
     * Llamado automáticamente por timerDialogoOculto cada 4.5 segundos.
     */
    private void avanzarDialogoOculto() {
        indiceDialogo++; // Pasa a la siguiente línea

        if (indiceDialogo < dialogosFinal.length) {
            // Todavía hay líneas: muestra la siguiente
            areaDialogoFinal.setText(dialogosFinal[indiceDialogo]);

            // Efecto especial: a partir de la mitad del monólogo, cambia el color del texto
            // gradualmente a rojo (tensión creciente)
            if (indiceDialogo > dialogosFinal.length / 2) {
                // Calcula la intensidad del rojo según cuánto queda
                float progreso = (float)(indiceDialogo - dialogosFinal.length / 2) /
                                 (dialogosFinal.length / 2);
                int r = (int)(200 + 30 * progreso);  // Rojo aumenta
                int g = (int)(220 - 180 * progreso); // Verde disminuye
                int b = (int)(200 - 180 * progreso); // Azul disminuye
                r = Math.min(r, 255); // Clamp a 255 máximo
                g = Math.max(g, 20);  // Clamp a 20 mínimo
                b = Math.max(b, 20);
                areaDialogoFinal.setForeground(new Color(r, g, b)); // Aplica el nuevo color
            }

        } else {
            // Se terminaron todos los diálogos del monólogo
            timerDialogoOculto.stop(); // Detiene el timer de diálogos
            timerParpadeo.stop();      // Detiene el parpadeo

            // Muestra el último mensaje antes del cierre
            areaDialogoFinal.setText("Sergio: Nos vemos.");
            areaDialogoFinal.setForeground(new Color(255, 30, 30)); // Rojo puro (terror máximo)

            // Inicia el timer de cierre: 3 segundos después de "Nos vemos" → se cierra el juego
            timerCierre.start(); // Inicia el contador de 3 segundos para el cierre abrupto
        }
    }

    // ─── REINICIAR JUEGO ──────────────────────────────────────────────────────────

    /**
     * Reinicia el juego desde el principio.
     * Llamado por el botón "Volver al inicio" en los finales Malo y Bueno.
     */
    private void reiniciarJuego() {
        // Detiene todos los timers activos
        timerParpadeo.stop();
        timerDialogoOculto.stop();
        timerCierre.stop();

        // Reinicia el estado global del juego (borra notas, flags, etc.)
        EstadoJuego.getInstance().reiniciar();

        // Limpia los componentes dinámicos del overlay (razón del game over, etc.)
        panelOverlay.removeAll();

        // Vuelve a agregar los componentes fijos del overlay
        panelOverlay.add(labelTextoFinal);
        panelOverlay.add(areaDialogoFinal);
        panelOverlay.add(labelParpadeo);
        panelOverlay.add(botonReintentar);
        panelOverlay.revalidate();
        panelOverlay.repaint();

        // Navega de vuelta al panel de narrativa y le pide al controlador que reinicie
        ventana.mostrarNarrativa(); // Regresa a la narrativa

        // Envía señal al controlador para que reinicie la historia desde el principio
        // Esto se hace a través de la ventana para mantener la arquitectura limpia
        ControladorJuego.getInstance(ventana).iniciarJuego();
    }
}