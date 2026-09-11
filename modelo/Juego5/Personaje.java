package modelo.Juego5; // Pertenece al paquete modelo

/**
 * CLASE: Personaje
 * -------------------------------------------------------------
 * Primera clase HIJA de Entidad (Herencia).
 * Representa a cualquier personaje dentro del videojuego:
 *  - El Maestro Sergio Mora
 *  - El Jugador (protagonista)
 *
 * Al extender Entidad, hereda nombre y descripcion,
 * y está OBLIGADA a implementar mostrarInfo() y ejecutarAccion().
 *
 * Aplica: Herencia, Encapsulamiento, Polimorfismo
 */
public class Personaje extends Entidad {

    // ─── ATRIBUTOS PROPIOS DE PERSONAJE ──────────────────────────────────────────
    private String spriteActual;  // Nombre del archivo de imagen actual (ej: "Sprite1")
    private String apodo;         // Apodo del personaje (ej: "La muerte blanca")
    private int nivel;            // Nivel de importancia narrativa (1=secundario, 10=principal)

    // ─── CONSTRUCTOR ─────────────────────────────────────────────────────────────

    /**
     * Constructor de Personaje.
     * Llama al constructor padre con super() (obligatorio en herencia).
     *
     * @param nombre       Nombre completo del personaje
     * @param descripcion  Descripción narrativa
     * @param apodo        Apodo dentro del juego
     * @param spriteActual Nombre inicial del sprite a mostrar
     * @param nivel        Nivel de importancia del personaje
     */
    public Personaje(String nombre, String descripcion, String apodo, String spriteActual, int nivel) {
        super(nombre, descripcion); // Llama al constructor de Entidad para inicializar nombre y descripcion
        this.apodo = apodo;         // Inicializa el apodo específico del personaje
        this.spriteActual = spriteActual; // Inicializa el sprite visual inicial
        this.nivel = nivel;         // Inicializa el nivel de importancia
    }

    // ─── IMPLEMENTACIÓN DE MÉTODOS ABSTRACTOS (obligatorio por herencia) ──────────

    /**
     * Implementación de mostrarInfo() heredado de Entidad.
     * Devuelve información completa del personaje.
     * @return String con datos del personaje
     */
    @Override
    public String mostrarInfo() {
        // Construye y retorna una cadena con todos los datos del personaje
        return "Personaje: " + nombre + "\n"
             + "Apodo: " + apodo + "\n"
             + "Descripción: " + descripcion + "\n"
             + "Sprite actual: " + spriteActual;
    }

    /**
     * Implementación de ejecutarAccion() heredado de Entidad.
     * La acción del personaje es aparecer en pantalla (lógica manejada por la vista).
     * Este método notifica que el personaje está activo.
     */
    @Override
    public void ejecutarAccion() {
        // En la novela gráfica, la acción del personaje es mostrarse con su sprite
        System.out.println("[Personaje] " + nombre + " aparece con sprite: " + spriteActual);
    }

    // ─── GETTERS Y SETTERS ────────────────────────────────────────────────────────

    /** @return nombre del archivo de sprite actual */
    public String getSpriteActual() {
        return spriteActual; // Retorna el sprite actualmente asignado
    }

    /**
     * Cambia el sprite del personaje (para mostrar diferentes emociones).
     * @param spriteActual nuevo nombre de sprite
     */
    public void setSpriteActual(String spriteActual) {
        this.spriteActual = spriteActual; // Actualiza el sprite a mostrar
    }

    /** @return apodo del personaje */
    public String getApodo() {
        return apodo; // Retorna el apodo del personaje
    }

    /** @return nivel de importancia del personaje */
    public int getNivel() {
        return nivel; // Retorna el nivel de importancia
    }
}
