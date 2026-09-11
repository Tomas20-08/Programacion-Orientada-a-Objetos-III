package modelo.Juego5;

// Main.java NO pertenece a ningún paquete específico (está en el paquete raíz/default)
// Es el punto de entrada del programa completo

import javax.swing.SwingUtilities; // Para ejecutar la GUI en el hilo correcto de Swing
                                   // Importa la ventana principal del juego

/*
 * CLASE: Main
 * ─────────────────────────────────────────────────────────────
 * Punto de entrada del videojuego "Sergio's Programming".
 *
 * En Java, la ejecución SIEMPRE empieza en el método:
 *   public static void main(String[] args)
 *
 * Esta clase hace SOLO tres cosas:
 *   1. Crea la VentanaPrincipal (la ventana del juego)
 *   2. La hace visible
 *   3. Crea el ControladorJuego y arranca el juego
 *
 * IMPORTANTE - SwingUtilities.invokeLater():
 * Toda la interfaz gráfica de Swing debe crearse y modificarse en el
 * "Event Dispatch Thread" (EDT), que es el hilo especial que Swing usa
 * para manejar eventos de la GUI (clics, repaints, etc.).
 * Si se crea la GUI en el hilo main directamente, puede haber bugs
 * visuales o condiciones de carrera. invokeLater() garantiza que todo
 * ocurre en el EDT correcto.
 *
 * Aplica: Buenas prácticas de Swing (EDT), punto de entrada Java
 */

public class Main {

    /*
     * Método main: punto de entrada del programa.
     * @param args argumentos de línea de comandos (no usados en este juego)
     */
    public static void main(String[] args) {

        // ── Ejecuta la creación de la GUI en el Event Dispatch Thread (EDT) ───────
        // Runnable lambda que contiene TODO el código de inicialización de la GUI
        SwingUtilities.invokeLater(() -> {

            // ── PASO 1: Crear la ventana principal ────────────────────────────────
            // VentanaPrincipal es el JFrame que contiene todos los paneles del juego
            VentanaPrincipal ventana = new VentanaPrincipal();

            // ── PASO 2: Hacer visible la ventana ─────────────────────────────────
            // setVisible(true) debe llamarse DESPUÉS de configurar todos los componentes
            // Si se llama antes, la ventana puede aparecer vacía o mal renderizada
            ventana.setVisible(true);

            // ── PASO 3: Crear el ControladorJuego y arrancar el juego ─────────────
            // El controlador es el "director" de toda la lógica del juego
            // getInstance() lo crea por primera vez (patrón Singleton)
            ControladorJuego controlador = ControladorJuego.getInstance(ventana);

            // ── PASO 4: ¡INICIAR EL JUEGO! ────────────────────────────────────────
            // iniciarJuego() arranca la introducción y comienza el flujo narrativo
            controlador.iniciarJuego();

            // ── LOG de inicio en la consola de NetBeans ────────────────────────────
            System.out.println("╔══════════════════════════════════════════════╗");
            System.out.println("║   SERGIO'S PROGRAMMING - El Curso Intensivo  ║");
            System.out.println("║          Año 2030 - Fundación Compensar      ║");
            System.out.println("╚══════════════════════════════════════════════╝");
            System.out.println("[Main] Juego iniciado correctamente.");
            System.out.println("[Main] Ventana: 1024x680 px");
            System.out.println("[Main] Recursos cargados: " +
                               GestorRecursos.getInstance().getClass().getSimpleName());
        });
    }
}
