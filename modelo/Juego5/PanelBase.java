package modelo.Juego5; // Paquete de la vista (interfaz gráfica)

import javax.swing.*;    // Componentes gráficos de Swing (JPanel, JLabel, JButton...)
import java.awt.*;       // Clases de AWT (Color, Font, Graphics, Graphics2D...)


/**
 * CLASE ABSTRACTA: PanelBase
 * -------------------------------------------------------------
 * Clase base para TODOS los paneles del juego.
 * Extiende JPanel (el contenedor gráfico de Swing).
 *
 * Esta clase aplica el requisito de HERENCIA Y ABSTRACCIÓN del proyecto.
 * Todos los paneles del juego (PanelNarrativa, PanelExamen, PanelFinal)
 * heredan de PanelBase.
 *
 * Proporciona:
 *  - Fondo personalizable (imagen o color sólido)
 *  - Fuentes y colores temáticos del juego (estilo DOKI DOKI)
 *  - Método abstracto inicializarComponentes() que cada panel debe implementar
 *
 * Aplica: Herencia, Abstracción, Encapsulamiento, GUI (Swing)
 */
public abstract class PanelBase extends JPanel {

    // ─── CONSTANTES DE ESTILO (paleta visual del juego) ──────────────────────────
    // Los colores están inspirados en el estilo de DOKI DOKI LITERATURE CLUB:
    // fondo oscuro, texto blanco crema, accentos en rojo o azul perturbador

    /** Color de fondo principal: negro profundo con tono azulado */
    protected static final Color COLOR_FONDO = new Color(10, 8, 20);

    /** Color de texto principal: blanco crema cálido */
    protected static final Color COLOR_TEXTO = new Color(235, 228, 210);

    /** Color de acento principal: rojo oscuro (amenaza latente) */
    protected static final Color COLOR_ACENTO = new Color(180, 30, 40);

    /** Color de acento secundario: azul eléctrico (misterio) */
    protected static final Color COLOR_ACENTO2 = new Color(60, 130, 220);

    /** Color de panel de diálogo: negro translúcido (overlay) */
    protected static final Color COLOR_PANEL_DIALOGO = new Color(5, 5, 15, 220);

    /** Color de botón normal */
    protected static final Color COLOR_BOTON = new Color(40, 35, 55);

    /** Color de botón hover */
    protected static final Color COLOR_BOTON_HOVER = new Color(80, 60, 90);

    /** Color de respuesta correcta (verde esperanza) */
    protected static final Color COLOR_CORRECTO = new Color(50, 180, 80);

    /** Color de respuesta incorrecta (rojo muerte) */
    protected static final Color COLOR_INCORRECTO = new Color(200, 40, 40);

    // ─── FUENTES DEL JUEGO ────────────────────────────────────────────────────────

    /** Fuente para textos de diálogo (monoespaciada, sensación de terminal/novela) */
    protected static final Font FUENTE_DIALOGO = new Font("Courier New", Font.PLAIN, 16);

    /** Fuente para títulos grandes (impactante) */
    protected static final Font FUENTE_TITULO = new Font("Courier New", Font.BOLD, 28);

    /** Fuente para subtítulos y nombres */
    protected static final Font FUENTE_SUBTITULO = new Font("Courier New", Font.BOLD, 18);

    /** Fuente para botones */
    protected static final Font FUENTE_BOTON = new Font("Courier New", Font.BOLD, 14);

    /** Fuente para textos pequeños (notas, contadores) */
    protected static final Font FUENTE_PEQUENA = new Font("Courier New", Font.PLAIN, 12);

    // ─── ATRIBUTOS DE INSTANCIA ────────────────────────────────────────────────────

    /** Gestor de recursos para cargar imágenes */
    protected GestorRecursos gestorRecursos;

    /** Imagen de fondo del panel (puede ser null = sin imagen) */
    protected Image imagenFondo;

    /** Nombre del fondo actual (para saber qué cargar) */
    protected String nombreFondo;

    // ─── CONSTRUCTOR ─────────────────────────────────────────────────────────────

    /**
     * Constructor de PanelBase.
     * @param nombreFondo nombre del fondo a usar ("Fondo1", "Fondo2", o null para color sólido)
     */
    public PanelBase(String nombreFondo) {
        this.gestorRecursos = GestorRecursos.getInstance(); // Obtiene el gestor singleton
        this.nombreFondo = nombreFondo;                      // Guarda el nombre del fondo

        // Configura el panel base
        setLayout(null);                   // Layout null = posicionamiento absoluto (x,y)
        setBackground(COLOR_FONDO);        // Color de fondo por defecto
        setPreferredSize(new Dimension(1024, 680)); // Tamaño estándar del juego

        // Carga la imagen de fondo si se proporcionó un nombre
        if (nombreFondo != null && !nombreFondo.isEmpty()) {
            imagenFondo = gestorRecursos.getFondo(nombreFondo); // Carga desde el gestor
        }

        // Llama al método abstracto que cada panel hijo implementa
        // para crear sus propios componentes específicos
        inicializarComponentes();
    }

    // ─── MÉTODO ABSTRACTO ────────────────────────────────────────────────────────

    /**
     * Método abstracto que CADA PANEL HIJO DEBE IMPLEMENTAR.
     * Aquí se crean los botones, etiquetas y demás componentes del panel.
     * Ejemplo de polimorfismo: cada panel tiene su propia lógica de UI.
     */
    protected abstract void inicializarComponentes();

    // ─── PINTADO DEL FONDO ───────────────────────────────────────────────────────

    /**
     * Sobreescribe el método paintComponent de JPanel.
     * Se llama automáticamente cuando el panel necesita repintarse.
     * Dibuja el fondo (imagen o color) debajo de todos los componentes.
     *
     * @param g Graphics - contexto gráfico de Java2D
     */
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g); // Llama al método padre (limpia el panel)

        // Convierte a Graphics2D para tener acceso a más funciones gráficas
        Graphics2D g2d = (Graphics2D) g;

        // Activa el anti-aliasing para texto y formas más suaves
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2d.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);

        if (imagenFondo != null) {
            // Si hay imagen de fondo, la dibuja cubriendo todo el panel
            g2d.drawImage(imagenFondo, 0, 0, getWidth(), getHeight(), this);

            // Agrega un overlay oscuro semitransparente para mejorar legibilidad del texto
            g2d.setColor(new Color(0, 0, 0, 100)); // Negro con 40% opacidad
            g2d.fillRect(0, 0, getWidth(), getHeight()); // Cubre todo el panel
        } else {
            // Si no hay imagen, pinta el fondo sólido oscuro
            g2d.setColor(COLOR_FONDO); // Color sólido definido arriba
            g2d.fillRect(0, 0, getWidth(), getHeight()); // Rellena todo el panel

            // Agrega un patrón sutil de puntos para dar textura (estética retro)
            g2d.setColor(new Color(255, 255, 255, 8)); // Blanco casi invisible
            for (int x = 0; x < getWidth(); x += 20) {     // Cada 20 píxeles en X
                for (int y = 0; y < getHeight(); y += 20) { // Cada 20 píxeles en Y
                    g2d.fillOval(x, y, 1, 1); // Punto de 1x1 píxel
                }
            }
        }
    }

    // ─── MÉTODOS UTILITARIOS PARA SUBCLASES ──────────────────────────────────────

    /**
     * Crea un JButton con el estilo visual del juego.
     * @param texto texto del botón
     * @param x     posición X
     * @param y     posición Y
     * @param ancho ancho del botón
     * @param alto  alto del botón
     * @return JButton configurado con el estilo del juego
     */
    protected JButton crearBotonEstilizado(String texto, int x, int y, int ancho, int alto) {
        JButton boton = new JButton(texto); // Crea el botón con el texto
        boton.setBounds(x, y, ancho, alto); // Posición y tamaño absolutos

        // Estilo visual: sin borde por defecto, colores personalizados
        boton.setFont(FUENTE_BOTON);                    // Fuente del juego
        boton.setForeground(COLOR_TEXTO);               // Color del texto (blanco crema)
        boton.setBackground(COLOR_BOTON);               // Fondo oscuro
        boton.setBorderPainted(false);                  // Sin borde visible
        boton.setFocusPainted(false);                   // Sin cuadro de enfoque
        boton.setCursor(new Cursor(Cursor.HAND_CURSOR)); // Cursor de mano al pasar

        // Efectos hover: cambia el fondo al pasar el mouse
        boton.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseEntered(java.awt.event.MouseEvent e) {
                boton.setBackground(COLOR_BOTON_HOVER); // Fondo más claro al hover
            }
            @Override
            public void mouseExited(java.awt.event.MouseEvent e) {
                boton.setBackground(COLOR_BOTON); // Vuelve al fondo original
            }
        });

        return boton; // Retorna el botón completamente configurado
    }

    /**
     * Crea un JLabel de texto con el estilo del juego.
     * @param texto     texto a mostrar
     * @param fuente    fuente a usar (usa las constantes FUENTE_*)
     * @param color     color del texto
     * @param x         posición X
     * @param y         posición Y
     * @param ancho     ancho del label
     * @param alto      alto del label
     * @return JLabel configurado
     */
    protected JLabel crearLabel(String texto, Font fuente, Color color,
                                int x, int y, int ancho, int alto) {
        JLabel label = new JLabel(texto);    // Crea el label con el texto
        label.setBounds(x, y, ancho, alto);  // Posición y tamaño
        label.setFont(fuente);               // Fuente del juego
        label.setForeground(color);          // Color del texto
        label.setOpaque(false);              // Sin fondo (transparente)
        return label;                        // Retorna el label configurado
    }

    /**
     * Cambia la imagen de fondo del panel y lo repinta.
     * @param nombreNuevoFondo nombre del nuevo fondo
     */
    public void cambiarFondo(String nombreNuevoFondo) {
        this.nombreFondo = nombreNuevoFondo; // Actualiza el nombre del fondo

        if (nombreNuevoFondo != null) {
            this.imagenFondo = gestorRecursos.getFondo(nombreNuevoFondo); // Carga el nuevo fondo
        } else {
            this.imagenFondo = null; // Sin imagen = fondo sólido
        }

        repaint(); // Fuerza el repintado del panel con el nuevo fondo
    }
}
