package modelo.Juego5; // Paquete de lógica del juego

import javax.swing.ImageIcon; // Para cargar imágenes como iconos de Swing
import java.awt.Image;        // Clase base de imagen en Java AWT
import java.net.URL;          // Para cargar recursos del classpath de forma robusta
import java.util.HashMap;     // HashMap para caché de imágenes ya cargadas

/**
 * CLASE: GestorRecursos
 * -------------------------------------------------------------
 * Clase responsable de CARGAR y GESTIONAR todas las imágenes del juego.
 * Implementa un sistema de CACHÉ para no recargar la misma imagen dos veces.
 *
 * IMPORTANTE PARA NETBEANS:
 * Las imágenes deben estar dentro de la carpeta del proyecto para que
 * getClass().getResource() pueda encontrarlas.
 *
 * Estructura de carpetas requerida dentro de 'src':
 *   src/
 *     recursos/
 *       FondosSergio/
 *         Fondo1.png
 *         Fondo2.png
 *       SpritesSergio/
 *         Sprite1.png
 *         Sprite2.png
 *         Sprite3.png
 *         Sprite4.png
 *         Sprite5.png
 *         Sprite6.png
 *         Sprite7.png
 *         FinalBueno.png
 *         FinalMalo.png
 *         FinalOculto.png
 *
 * Aplica: Encapsulamiento, Manejo de excepciones (try-catch), Patrón Caché
 */
public class GestorRecursos {

    // ─── SINGLETON ────────────────────────────────────────────────────────────────
    private static GestorRecursos instancia = null; // Única instancia

    // ─── CACHÉ DE IMÁGENES ────────────────────────────────────────────────────────
    // HashMap<nombre, imagen> para no recargar imágenes ya usadas
    private HashMap<String, Image> cacheImagenes;

    // ─── RUTAS BASE ───────────────────────────────────────────────────────────────
    // Rutas relativas dentro del classpath (carpeta src del proyecto)
    private static final String RUTA_SPRITES = "/recursos/SpritesSergio/";  // Ruta de sprites
    private static final String RUTA_FONDOS  = "/recursos/FondosSergio/";   // Ruta de fondos
    private static final String EXT          = ".png";                       // Extensión de imágenes

    // ─── CONSTRUCTOR PRIVADO (Singleton) ─────────────────────────────────────────

    /**
     * Constructor privado: inicializa el caché.
     */
    private GestorRecursos() {
        cacheImagenes = new HashMap<>(); // Inicializa el HashMap vacío
    }

    // ─── MÉTODO SINGLETON ────────────────────────────────────────────────────────

    /**
     * Devuelve la única instancia del gestor de recursos.
     * @return GestorRecursos - la instancia singleton
     */
    public static GestorRecursos getInstance() {
        if (instancia == null) {               // Si no existe instancia...
            instancia = new GestorRecursos();  // ...crea una nueva
        }
        return instancia; // Retorna la única instancia
    }

    // ─── MÉTODO PRINCIPAL: cargar imagen por ruta completa ─────────────────────────

    /**
     * Carga una imagen desde el classpath y la guarda en caché.
     * Si ya está en caché, la devuelve directamente sin recargar.
     *
     * MANEJO DE EXCEPCIONES: si la imagen no existe, devuelve null
     * y muestra un mensaje de error sin crashear el juego.
     *
     * @param rutaCompleta ruta relativa desde src (ej: "/recursos/SpritesSergio/Sprite1.png")
     * @return Image cargada, o null si no se encontró
     */
    public Image cargarImagen(String rutaCompleta) {
        // Si ya está en caché, devuelve directamente sin cargar de disco
        if (cacheImagenes.containsKey(rutaCompleta)) {
            return cacheImagenes.get(rutaCompleta); // Retorna la imagen cacheada
        }

        // Intenta cargar la imagen desde el classpath (dentro de src/)
        try {
            // getClass().getResource() busca el archivo dentro del classpath del proyecto
            URL urlImagen = getClass().getResource(rutaCompleta);

            if (urlImagen == null) {
                // La URL es null cuando el archivo no existe en esa ruta
                System.err.println("[GestorRecursos] ⚠ Imagen no encontrada: " + rutaCompleta);
                System.err.println("[GestorRecursos] Asegúrate de que la imagen esté en src" + rutaCompleta);
                return null; // Retorna null para que la vista maneje el caso sin imagen
            }

            // Crea un ImageIcon desde la URL y obtiene la imagen
            ImageIcon icono = new ImageIcon(urlImagen);
            Image imagen = icono.getImage(); // Extrae el objeto Image del ImageIcon

            // Guarda en caché para futuras solicitudes de la misma imagen
            cacheImagenes.put(rutaCompleta, imagen);

            System.out.println("[GestorRecursos] ✓ Imagen cargada: " + rutaCompleta); // Log de éxito
            return imagen; // Retorna la imagen cargada

        } catch (Exception e) {
            // Manejo de excepciones: cualquier error al cargar (disco dañado, permisos, etc.)
            System.err.println("[GestorRecursos] ✗ Error al cargar imagen: " + rutaCompleta);
            System.err.println("[GestorRecursos] Detalle del error: " + e.getMessage());
            return null; // Retorna null de forma segura sin crashear el juego
        }
    }

    // ─── MÉTODOS DE CONVENIENCIA ──────────────────────────────────────────────────

    /**
     * Carga un sprite de Sergio por su nombre.
     * @param nombreSprite nombre del sprite (ej: "Sprite1", "Sprite2", "FinalMalo")
     * @return Image del sprite, o null si no se encontró
     */
    public Image getSprite(String nombreSprite) {
        // Construye la ruta completa y llama al método principal
        return cargarImagen(RUTA_SPRITES + nombreSprite + EXT);
    }

    /**
     * Carga un fondo por su nombre.
     * @param nombreFondo nombre del fondo (ej: "Fondo1", "Fondo2")
     * @return Image del fondo, o null si no se encontró
     */
    public Image getFondo(String nombreFondo) {
        // Construye la ruta completa y llama al método principal
        return cargarImagen(RUTA_FONDOS + nombreFondo + EXT);
    }

    /**
     * Precarga todas las imágenes del juego al inicio.
     * Mejora el rendimiento evitando pausas durante el gameplay.
     * Útil llamarlo en la pantalla de carga inicial.
     */
    public void precargarTodo() {
        System.out.println("[GestorRecursos] Precargando todos los recursos...");

        // Precarga todos los sprites de Sergio
        String[] sprites = {"Sprite1", "Sprite2", "Sprite3", "Sprite4",
                            "Sprite5", "Sprite6", "Sprite7",
                            "FinalBueno", "FinalMalo", "FinalOculto"};

        for (String sprite : sprites) {         // Recorre cada nombre de sprite
            getSprite(sprite);                   // Intenta cargarlo (si existe)
        }

        // Precarga los fondos
        String[] fondos = {"Fondo1", "Fondo2"};
        for (String fondo : fondos) {           // Recorre cada nombre de fondo
            getFondo(fondo);                     // Intenta cargarlo
        }

        System.out.println("[GestorRecursos] Precarga completada. " +
                           cacheImagenes.size() + " imágenes en caché.");
    }

    /**
     * Limpia el caché de imágenes (libera memoria).
     * Llamar solo si el juego va a reiniciarse completamente.
     */
    public void limpiarCache() {
        cacheImagenes.clear();                          // Vacía el HashMap de caché
        System.out.println("[GestorRecursos] Caché limpiada."); // Confirma la limpieza
    }
}
