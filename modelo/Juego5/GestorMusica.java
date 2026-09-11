package modelo.Juego5; // Mismo paquete que todo el juego

import javax.sound.sampled.*; // API de sonido de Java (Clip, AudioInputStream, etc.)
import java.io.IOException;   // Para manejar errores de lectura de archivo
import java.net.URL;          // Para localizar el archivo dentro del classpath

/**
 * CLASE: GestorMusica
 * ─────────────────────────────────────────────────────────────
 * Maneja la música de fondo del juego usando la API javax.sound.sampled.

 * Cada pista se reproduce en loop infinito.
 * Singleton: solo existe un GestorMusica en toda la app.
 *
 * Aplica: Encapsulamiento, Singleton, Manejo de excepciones (try-catch)
 */
public class GestorMusica {

    // ─── SINGLETON ────────────────────────────────────────────────────────────────
    private static GestorMusica instancia = null; // Única instancia

    // ─── RUTA BASE ────────────────────────────────────────────────────────────────
    // Carpeta donde están los archivos de música dentro del classpath (src/)
    private static final String RUTA_MUSICA = "/recursos/MusicaSergio/"; // Ruta base
    private static final String EXT = ".wav"; // Extensión (deben ser WAV)

    // ─── CLIP ACTIVO ──────────────────────────────────────────────────────────────
    // Clip es la clase de Java que mantiene audio en memoria y lo reproduce
    private Clip clipActual; // El clip que se está reproduciendo ahora mismo

    // ─── NOMBRES DE PISTA (constantes para no escribir strings a mano) ────────────
    public static final String PISTA_AMBIENTE      = "AmbienteSergiosGame";      // Narrativa / días
    public static final String PISTA_EXAMEN        = "ExamenSergiosGame";        // Durante exámenes
    public static final String PISTA_FINAL_BUENO   = "FinalBuenoSergiosGame";   // Final bueno
    public static final String PISTA_FINAL_MALO    = "FinalMaloSergiosGame";    // Final malo / Game Over
    public static final String PISTA_FINAL_OCULTO  = "FinalOcultoSergiosGame"; // Final oculto (terror)

    // ─── CONSTRUCTOR PRIVADO (Singleton) ─────────────────────────────────────────
    private GestorMusica() {
        // Constructor vacío: el gestor empieza sin nada reproduciendo
    }

    // ─── MÉTODO SINGLETON ────────────────────────────────────────────────────────

    /**
     * Retorna la única instancia del GestorMusica.
     * @return GestorMusica - instancia singleton
     */
    public static GestorMusica getInstance() {
        if (instancia == null) {                    // Si no existe...
            instancia = new GestorMusica();         // ...crea la única instancia
        }
        return instancia; // Retorna siempre la misma
    }

    // ─── MÉTODO PRINCIPAL: reproducir una pista ────────────────────────────────────

    /**
     * Detiene lo que suena y reproduce la pista indicada en loop infinito.
     * Si la pista ya está sonando, no hace nada (evita reiniciar sin razón).
     *
     * @param nombrePista nombre de la pista sin extensión (usa las constantes PISTA_*)
     */
    public void reproducir(String nombrePista) {
        // Construye la ruta completa dentro del classpath
        String rutaCompleta = RUTA_MUSICA + nombrePista + EXT;

        // Si ya está sonando exactamente esta pista, no interrumpe
        // (permite que el loop continúe sin reiniciarse al cambiar de pantalla)
        if (clipActual != null && clipActual.isRunning()) {
            // Comprueba si es la misma pista comparando metadatos (no hay forma directa en Clip)
            // Usamos una bandera: guardamos el nombre de la pista actual
            if (nombrePista.equals(pistaActual)) {
                return; // Ya está sonando → no hace nada
            }
        }

        detener(); // Detiene y libera el clip anterior antes de cargar uno nuevo

        try {
            // Busca el archivo dentro del classpath del proyecto
            URL urlMusica = getClass().getResource(rutaCompleta);

            if (urlMusica == null) {
                // Si no encuentra el archivo, avisa pero no crashea el juego
                System.err.println("[GestorMusica] ⚠ Archivo no encontrado: " + rutaCompleta);
                System.err.println("[GestorMusica] Convierte los .mp3 a .wav y colócalos en src" + RUTA_MUSICA);
                return; // Sale sin reproducir
            }

            // Abre el stream de audio desde la URL
            AudioInputStream stream = AudioSystem.getAudioInputStream(urlMusica);

            // Obtiene una línea de tipo Clip desde el sistema de audio
            clipActual = AudioSystem.getClip();

            // Carga el audio completo en memoria (necesario para loop)
            clipActual.open(stream);

            // Reproduce en loop INFINITO (Clip.LOOP_CONTINUOUSLY = -1)
            clipActual.loop(Clip.LOOP_CONTINUOUSLY);

            // Inicia la reproducción desde el principio
            clipActual.start();

            pistaActual = nombrePista; // Guarda el nombre de la pista actual
            System.out.println("[GestorMusica] ▶ Reproduciendo: " + nombrePista);

        } catch (UnsupportedAudioFileException e) {
            // El formato de audio no es soportado (MP3 da este error → convierte a WAV)
            System.err.println("[GestorMusica] ✗ Formato no soportado: " + rutaCompleta);
            System.err.println("[GestorMusica] → Convierte el archivo a WAV (44100 Hz, 16-bit, Estéreo)");
        } catch (LineUnavailableException e) {
            // El sistema no tiene líneas de audio disponibles
            System.err.println("[GestorMusica] ✗ Línea de audio no disponible: " + e.getMessage());
        } catch (IOException e) {
            // Error de lectura del archivo
            System.err.println("[GestorMusica] ✗ Error leyendo archivo: " + e.getMessage());
        }
    }

    // ─── BANDERA DE PISTA ACTUAL ──────────────────────────────────────────────────
    private String pistaActual = ""; // Nombre de la pista que está sonando ahora

    // ─── DETENER ─────────────────────────────────────────────────────────────────

    /**
     * Detiene la reproducción actual y libera los recursos de audio.
     * Siempre llamar esto antes de reproducir una nueva pista.
     */
    public void detener() {
        if (clipActual != null) {           // Si hay un clip cargado...
            if (clipActual.isRunning()) {   // ...y está reproduciendo...
                clipActual.stop();          // ...detiene la reproducción
            }
            clipActual.close();             // Libera los recursos del clip (memoria de audio)
            clipActual = null;              // Elimina la referencia para que el GC pueda liberarlo
        }
        pistaActual = ""; // Resetea el nombre de pista actual
    }

    /**
     * Indica si hay música reproduciéndose en este momento.
     * @return boolean - true si hay algo sonando
     */
    public boolean estaReproduciendo() {
        return clipActual != null && clipActual.isRunning(); // true solo si el clip existe y corre
    }
}