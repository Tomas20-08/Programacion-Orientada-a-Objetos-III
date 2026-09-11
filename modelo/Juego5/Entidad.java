package modelo.Juego5; // Paquete del modelo de datos (arquitectura MVC)

/**
 * CLASE ABSTRACTA: Entidad
 * -------------------------------------------------------------
 * Requisito obligatorio: "Debe existir al menos una clase abstracta que represente el núcleo del sistema".
 * Esta clase abstracta es la BASE de toda la jerarquía de clases del juego.
 *
 * Una clase abstracta NO puede instanciarse directamente (no puedes hacer new Entidad()).
 * Solo sirve como "molde" para sus clases hijas (Personaje, ElementoNarrativo, etc.).
 *
 * Contiene:
 *  - Atributos comunes (nombre, descripción)
 *  - Métodos abstractos (que las hijas DEBEN implementar)
 *  - Métodos concretos (heredados tal cual por las hijas)
 */
public abstract class Entidad {

    // ─── ATRIBUTOS COMUNES ───────────────────────────────────────────────────────
    // Encapsulamiento: los atributos son 'protected' para que las clases hijas
    // puedan acceder directamente, pero clases externas no.

    protected String nombre;       // Nombre de la entidad (ej: "Sergio Mora", "Examen POO")
    protected String descripcion;  // Descripción corta de la entidad

    // ─── CONSTRUCTOR ─────────────────────────────────────────────────────────────

    /**
     * Constructor de Entidad.
     * Al ser abstracta, este constructor SOLO lo llaman las clases hijas con super().
     *
     * @param nombre      Nombre de la entidad
     * @param descripcion Descripción de la entidad
     */
    public Entidad(String nombre, String descripcion) {
        this.nombre = nombre;           // Asigna el nombre recibido al atributo interno
        this.descripcion = descripcion; // Asigna la descripción recibida al atributo interno
    }

    // ─── MÉTODOS ABSTRACTOS ───────────────────────────────────────────────────────
    // Las clases hijas ESTÁN OBLIGADAS a implementar estos métodos.
    // Aquí solo se declaran, no se implementan.

    /**
     * Muestra la información principal de la entidad.
     * Cada hija lo implementará a su manera (polimorfismo).
     * @return String con la información formateada
     */
    public abstract String mostrarInfo();

    /**
     * Ejecuta la acción principal de la entidad dentro del juego.
     * Un Personaje puede "hablar", un Examen puede "evaluarte", etc.
     */
    public abstract void ejecutarAccion();

    // ─── MÉTODOS CONCRETOS (GETTERS Y SETTERS) ────────────────────────────────────
    // Encapsulamiento: acceso controlado a los atributos privados.

    /**
     * Devuelve el nombre de la entidad.
     * @return String - nombre
     */
    public String getNombre() {
        return nombre; // Retorna el valor del atributo nombre
    }

    /**
     * Establece un nuevo nombre para la entidad.
     * @param nombre - nuevo nombre
     */
    public void setNombre(String nombre) {
        this.nombre = nombre; // Reemplaza el nombre actual con el nuevo
    }

    /**
     * Devuelve la descripción de la entidad.
     * @return String - descripción
     */
    public String getDescripcion() {
        return descripcion; // Retorna el valor del atributo descripcion
    }

    /**
     * Establece una nueva descripción para la entidad.
     * @param descripcion - nueva descripción
     */
    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion; // Reemplaza la descripción actual
    }

    /**
     * Representación en texto de la entidad (buena práctica en Java).
     * Se usa cuando haces System.out.println(unaEntidad).
     * @return String con nombre y descripción
     */
    @Override
    public String toString() {
        // Devuelve una cadena con formato legible
        return "[Entidad] Nombre: " + nombre + " | Descripción: " + descripcion;
    }
}
