package modelo.Juego5; // Paquete de la vista

import javax.swing.*;         // Componentes Swing
import java.awt.*;            // Clases AWT
import java.awt.event.*;      // Eventos de mouse y teclado


/**
 * CLASE: PanelNarrativa
 * -------------------------------------------------------------
 * Panel PRINCIPAL del juego. Muestra:
 *  - El fondo de la escena (Fondo1 o Fondo2)
 *  - El sprite de Sergio (con efecto de fade in/out)
 *  - La caja de diálogo en la parte inferior (estilo novela gráfica)
 *  - El texto del diálogo con efecto de máquina de escribir
 *  - Los botones de opciones cuando hay elecciones que hacer
 *
 * FLUJO: El panel recibe un array de diálogos [] y los muestra uno por uno.
 * Al terminar todos los diálogos, llama al callback onDialogosTerminados.
 *
 * Aplica: Herencia (extiende PanelBase), Eventos, GUI avanzada, Timer
 */
public class PanelNarrativa extends PanelBase {

    // ─── REFERENCIAS ─────────────────────────────────────────────────────────────
    private VentanaPrincipal ventana;  // Referencia a la ventana principal (para navegar)
    private EstadoJuego estado;        // Estado global del juego

    // ─── ESTADO INTERNO DEL PANEL ─────────────────────────────────────────────────
    private String[] dialogosActuales;   // Array de diálogos que se están mostrando
    private int indiceDialogo;           // Índice del diálogo actual (0 = primero)
    private String textoMostrado;        // Fragmento del texto ya "escrito" (efecto máquina)
    private int indiceCaracter;          // Índice del carácter actual en el texto (máquina escribir)
    private boolean textoCompleto;       // True = todo el texto ya se mostró
    private Runnable callbackTerminar;   // Código a ejecutar cuando terminan todos los diálogos

    // ─── COMPONENTES GRÁFICOS ────────────────────────────────────────────────────
    private JLabel labelNombrePersonaje; // Muestra quién habla (ej: "Sergio:")
    private JTextArea areaDialogo;       // Área de texto para el diálogo
    private JPanel panelDialogo;         // Panel inferior que contiene el diálogo
    private JPanel panelSprite;          // Panel para mostrar el sprite de Sergio
    private JLabel labelSprite;          // Label que contiene la imagen del sprite
    private JButton botonContinuar;      // Botón para avanzar al siguiente diálogo
    private JPanel panelOpciones;        // Panel con botones de opciones (cuando hay elección)

    // ─── TIMER PARA EFECTO DE MÁQUINA DE ESCRIBIR ─────────────────────────────────
    private Timer timerTexto;            // Timer que va agregando letras al texto una por una

    // ─── SPRITE ACTUAL ────────────────────────────────────────────────────────────
    private Image spriteActual;          // Imagen del sprite actualmente mostrado

    // ─── CONSTRUCTOR ─────────────────────────────────────────────────────────────

    /**
     * Constructor del PanelNarrativa.
     * @param ventana referencia a la ventana principal
     */
    public PanelNarrativa(VentanaPrincipal ventana) {
        super("Fondo1"); // Fondo inicial: el salón de clases (Fondo1)
        this.ventana = ventana;               // Guarda la referencia a la ventana
        this.estado = EstadoJuego.getInstance(); // Obtiene el estado global
        this.indiceDialogo = 0;               // Empieza desde el primer diálogo
        this.textoMostrado = "";              // Sin texto mostrado al inicio
        this.textoCompleto = false;           // El texto no está completo aún
    }

    // ─── IMPLEMENTACIÓN DEL MÉTODO ABSTRACTO ────────────────────────────────────

    /**
     * Inicializa todos los componentes gráficos del panel narrativo.
     * Este método es llamado por el constructor de PanelBase.
     */
    @Override
    protected void inicializarComponentes() {

        // ── ÁREA DEL SPRITE (mitad superior del panel) ─────────────────────────
        panelSprite = new JPanel() {
            // Sobreescribe paintComponent para dibujar el sprite manualmente
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);                          // Limpia el panel
                if (spriteActual != null) {                       // Si hay un sprite cargado...
                    Graphics2D g2d = (Graphics2D) g;

                    // Anti-aliasing para imagen más suave
                    g2d.setRenderingHint(RenderingHints.KEY_INTERPOLATION,
                                        RenderingHints.VALUE_INTERPOLATION_BILINEAR);

                    // Centra el sprite en su panel con altura máxima de 350px
                    int maxAlto = 380;                           // Alto máximo del sprite
                    int altoSprite = Math.min(spriteActual.getHeight(null), maxAlto);
                    // Escala proporcionalmente si es más grande que el máximo
                    int anchoSprite = (int)((double)spriteActual.getWidth(null) *
                                           altoSprite / spriteActual.getHeight(null));

                    // Posición centrada dentro del panel
                    int x = (getWidth() - anchoSprite) / 2;
                    int y = getHeight() - altoSprite; // Pegado al fondo del panel sprite

                    g2d.drawImage(spriteActual, x, y, anchoSprite, altoSprite, this);
                }
            }
        };
        panelSprite.setBounds(200, 30, 620, 390); // Posición y tamaño del área de sprite
        panelSprite.setOpaque(false);              // Transparente (muestra el fondo debajo)
        add(panelSprite);                          // Agrega al panel principal

        // ── PANEL DE DIÁLOGO (parte inferior) ──────────────────────────────────
        panelDialogo = new JPanel(null); // Layout null para posicionamiento libre
        panelDialogo.setBounds(20, 440, 984, 220);      // Posición y tamaño
        panelDialogo.setBackground(COLOR_PANEL_DIALOGO); // Fondo oscuro semitransparente
        panelDialogo.setBorder(BorderFactory.createLineBorder(
            new Color(180, 160, 100, 150), 2)); // Borde dorado semitransparente (estilo DOKI)
        add(panelDialogo); // Agrega al panel principal

        // ── NOMBRE DEL PERSONAJE (quién habla) ─────────────────────────────────
        labelNombrePersonaje = new JLabel("???");
        labelNombrePersonaje.setBounds(15, 8, 400, 30); // Esquina superior izquierda del panel diálogo
        labelNombrePersonaje.setFont(FUENTE_SUBTITULO);  // Fuente de subtítulos
        labelNombrePersonaje.setForeground(COLOR_ACENTO); // Color rojo (como el nombre en DOKI DOKI)
        panelDialogo.add(labelNombrePersonaje);            // Agrega al panel de diálogo

        // ── ÁREA DE TEXTO DEL DIÁLOGO ───────────────────────────────────────────
        areaDialogo = new JTextArea();
        areaDialogo.setBounds(15, 45, 850, 130);          // Posición dentro del panel diálogo
        areaDialogo.setFont(FUENTE_DIALOGO);              // Fuente monoespaciada
        areaDialogo.setForeground(COLOR_TEXTO);           // Texto blanco crema
        areaDialogo.setBackground(new Color(0, 0, 0, 0)); // Fondo transparente
        areaDialogo.setOpaque(false);                     // Sin fondo opaco
        areaDialogo.setEditable(false);                   // Solo lectura (no editable)
        areaDialogo.setLineWrap(true);                    // Ajuste de línea automático
        areaDialogo.setWrapStyleWord(true);               // Ajusta por palabras (no corta palabras)
        areaDialogo.setFocusable(false);                  // No recibe foco del teclado
        panelDialogo.add(areaDialogo);                    // Agrega al panel de diálogo

        // ── BOTÓN CONTINUAR (triángulo en la esquina inferior derecha) ──────────
        botonContinuar = new JButton("CONTINUAR");
        botonContinuar.setBounds(850, 175, 130, 35);       // Esquina inferior derecha
        botonContinuar.setFont(FUENTE_PEQUENA);            // Fuente pequeña
        botonContinuar.setForeground(new Color(200, 180, 100)); // Dorado
        botonContinuar.setBackground(COLOR_PANEL_DIALOGO);  // Sin fondo visible
        botonContinuar.setBorderPainted(false);             // Sin borde
        botonContinuar.setFocusPainted(false);              // Sin cuadro de enfoque
        botonContinuar.setCursor(new Cursor(Cursor.HAND_CURSOR));

        // Acción del botón continuar: avanza al siguiente diálogo
        botonContinuar.addActionListener(e -> avanzarDialogo());
        panelDialogo.add(botonContinuar); // Agrega al panel de diálogo

        // ── PANEL DE OPCIONES (para las decisiones del jugador) ─────────────────
        panelOpciones = new JPanel(null); // Layout null
        panelOpciones.setBounds(20, 440, 984, 220);      // Mismo lugar que el panel diálogo
        panelOpciones.setBackground(COLOR_PANEL_DIALOGO); // Mismo estilo
        panelOpciones.setBorder(BorderFactory.createLineBorder(
            new Color(180, 160, 100, 150), 2));
        panelOpciones.setVisible(false); // Inicialmente oculto
        add(panelOpciones);              // Agrega al panel principal

        // ── TIMER PARA EFECTO MÁQUINA DE ESCRIBIR ──────────────────────────────
        // Cada 30ms agrega un carácter más al texto mostrado
        timerTexto = new Timer(30, e -> {
            if (dialogosActuales != null && indiceDialogo < dialogosActuales.length) {
                String textoCompleto2 = dialogosActuales[indiceDialogo]; // Texto completo actual

                if (indiceCaracter < textoCompleto2.length()) {
                    // Aún hay caracteres por mostrar: agrega el siguiente
                    indiceCaracter++;
                    textoMostrado = textoCompleto2.substring(0, indiceCaracter); // Subcadena hasta aquí
                    areaDialogo.setText(textoMostrado);  // Actualiza el área de texto
                } else {
                    // El texto se terminó de "escribir"
                    textoCompleto = true;     // Marca como completo
                    timerTexto.stop();        // Detiene el timer
                    botonContinuar.setVisible(true); // Muestra el botón continuar
                }
            }
        });

        // Permite avanzar con clic en el área de diálogo también
        panelDialogo.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                avanzarDialogo(); // Clic en cualquier lugar del diálogo avanza
            }
        });
    }

    // ─── MÉTODOS DE CONTROL DE NARRATIVA ─────────────────────────────────────────

    /**
     * Inicia la reproducción de un array de diálogos.
     * @param dialogos    array de strings con los diálogos a mostrar
     * @param fondo       nombre del fondo a usar ("Fondo1" o "Fondo2")
     * @param sprite      nombre del sprite de Sergio (o null si no hay)
     * @param alTerminar  Runnable que se ejecuta cuando terminan TODOS los diálogos
     */
    public void iniciarDialogos(String[] dialogos, String fondo, String sprite, Runnable alTerminar) {
        this.dialogosActuales = dialogos;  // Guarda los diálogos a mostrar
        this.indiceDialogo = 0;            // Empieza desde el primero
        this.callbackTerminar = alTerminar; // Guarda qué hacer al terminar

        // Cambia el fondo si es diferente al actual
        if (fondo != null && !fondo.equals(this.nombreFondo)) {
            cambiarFondo(fondo); // Método heredado de PanelBase
        }

        // Carga el sprite si se proporcionó uno
        if (sprite != null) {
            spriteActual = gestorRecursos.getSprite(sprite); // Carga el sprite
        } else {
            spriteActual = null; // Sin sprite (escenas sin Sergio)
        }

        // Oculta opciones y muestra diálogo
        panelOpciones.setVisible(false); // Oculta el panel de opciones
        panelDialogo.setVisible(true);   // Muestra el panel de diálogo

        mostrarDialogoActual(); // Muestra el primer diálogo
        repaint(); // Repinta el panel para mostrar los cambios
    }

    /**
     * Muestra el diálogo en el índice actual con el efecto de máquina de escribir.
     */
    private void mostrarDialogoActual() {
        if (dialogosActuales == null || indiceDialogo >= dialogosActuales.length) {
            return; // Protección: no hace nada si no hay diálogos
        }

        String dialogoActual = dialogosActuales[indiceDialogo]; // Obtiene el diálogo actual

        // Detecta si el diálogo tiene formato "Nombre: texto" para separar quién habla
        if (dialogoActual.contains(":")) {
            String[] partes = dialogoActual.split(":", 2); // Divide en máximo 2 partes
            if (partes[0].length() < 20) { // El nombre no debería ser muy largo
                labelNombrePersonaje.setText(partes[0].trim()); // Muestra el nombre del hablante
                textoMostrado = ""; // Reinicia el texto mostrado
                indiceCaracter = 0; // Reinicia el contador de caracteres
                areaDialogo.setText(""); // Limpia el área de texto

                // El texto a "escribir" es solo la parte después del ":"
                dialogosActuales[indiceDialogo] = partes[1].trim();
            }
        } else {
            // No hay ":", es texto narrativo (voz en off / narrador)
            labelNombrePersonaje.setText("..."); // Sin nombre específico
        }

        // Reinicia variables para la nueva animación de texto
        textoMostrado = "";        // Sin texto mostrado
        indiceCaracter = 0;        // Empieza desde el primer carácter
        textoCompleto = false;     // No está completo
        botonContinuar.setVisible(false); // Oculta botón hasta que termine de escribir

        timerTexto.start(); // INICIA el efecto de máquina de escribir
    }

    /**
     * Avanza al siguiente diálogo.
     * Si el texto no ha terminado de "escribirse", lo muestra completo primero.
     * Si ya terminó, pasa al siguiente diálogo.
     * Si era el último, ejecuta el callback.
     */
    public void avanzarDialogo() {
        if (!textoCompleto) {
            // Si el texto aún no terminó de escribirse, lo muestra completo de golpe
            timerTexto.stop();                                   // Detiene la animación
            textoMostrado = dialogosActuales[indiceDialogo];     // Muestra todo el texto
            areaDialogo.setText(textoMostrado);                  // Actualiza el área
            textoCompleto = true;                                // Marca como completo
            botonContinuar.setVisible(true);                     // Muestra el botón
            return; // Sale sin avanzar aún (primer clic = mostrar completo)
        }

        indiceDialogo++; // Avanza al siguiente diálogo

        if (indiceDialogo < dialogosActuales.length) {
            mostrarDialogoActual(); // Muestra el siguiente diálogo
        } else {
            // Se terminaron todos los diálogos
            timerTexto.stop(); // Asegura que el timer esté detenido

            if (callbackTerminar != null) {
                callbackTerminar.run(); // Ejecuta el código de "¿qué hacer después?"
            }
        }
    }

    /**
     * Muestra un panel de OPCIONES en lugar del diálogo normal.
     * Para cuando el jugador debe tomar una decisión.
     *
     * @param pregunta    Texto de la pregunta / situación
     * @param opciones    Array de textos de las opciones
     * @param alElegir    Runnable[] - qué hacer cuando el jugador elige cada opción
     */
    public void mostrarOpciones(String pregunta, String[] opciones, Runnable[] alElegir) {
        panelDialogo.setVisible(false);  // Oculta el diálogo normal
        panelOpciones.setVisible(true);  // Muestra el panel de opciones
        panelOpciones.removeAll();       // Limpia opciones anteriores

        // Label con la pregunta / situación
        JLabel labelPregunta = new JLabel("<html>" + pregunta + "</html>"); // HTML para ajuste de línea
        labelPregunta.setBounds(15, 10, 950, 50);       // Posición
        labelPregunta.setFont(FUENTE_DIALOGO);           // Fuente
        labelPregunta.setForeground(COLOR_TEXTO);        // Color
        panelOpciones.add(labelPregunta);                // Agrega al panel

        // Crea un botón por cada opción
        int yBase = 70; // Posición Y inicial de los botones
        for (int i = 0; i < opciones.length; i++) {
            final int indice = i; // Variable final para usar en el lambda

            JButton botonOpcion = crearBotonEstilizado(
                "▸ " + opciones[i],  // Texto con indicador visual
                50,                   // X
                yBase + (i * 45),     // Y (cada botón separado 45px)
                884,                  // Ancho
                38                    // Alto
            );

            // Borde lateral de acento (como las opciones de DOKI DOKI)
            botonOpcion.setBorder(BorderFactory.createMatteBorder(0, 3, 0, 0, COLOR_ACENTO2));

            // Agrega la acción de cada botón
            botonOpcion.addActionListener(e -> {
                panelOpciones.setVisible(false); // Oculta las opciones
                panelDialogo.setVisible(true);   // Muestra el diálogo normal de nuevo
                if (alElegir[indice] != null) {
                    alElegir[indice].run(); // Ejecuta la acción de la opción elegida
                }
            });

            panelOpciones.add(botonOpcion); // Agrega el botón al panel de opciones
        }

        panelOpciones.revalidate(); // Re-valida el layout del panel
        panelOpciones.repaint();    // Repinta el panel con las nuevas opciones
    }

    /**
     * Cambia el sprite visible de Sergio.
     * @param nombreSprite nombre del sprite a mostrar (o null para ocultarlo)
     */
    public void cambiarSprite(String nombreSprite) {
        if (nombreSprite != null) {
            spriteActual = gestorRecursos.getSprite(nombreSprite); // Carga el nuevo sprite
        } else {
            spriteActual = null; // Elimina el sprite
        }
        panelSprite.repaint(); // Repinta el área del sprite
    }
}
