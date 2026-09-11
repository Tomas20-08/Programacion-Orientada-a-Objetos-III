package modelo.Juego5; // Pertenece al paquete modelo


import java.util.ArrayList;   // ArrayList para guardar preguntas (requisito del proyecto)

/**
 * CLASE: Examen
 * -------------------------------------------------------------
 * Segunda clase HIJA de Entidad (Herencia).
 * También IMPLEMENTA la interfaz Evaluable (polimorfismo de interfaces).
 *
 * Representa un examen de un día específico del curso con el Maestro Sergio.
 * Contiene preguntas de opción múltiple, las evalúa y devuelve nota de 0 a 5.
 *
 * Aplica: Herencia, Interfaces, Encapsulamiento, ArrayList, Polimorfismo
 */
public class Examen extends Entidad implements Evaluable {

    // ─── ATRIBUTOS PROPIOS DE EXAMEN ─────────────────────────────────────────────
    private String temaEvaluado;                    // Tema del examen (ej: "POO", "ArrayList")
    private ArrayList<Pregunta> preguntas;          // Lista de preguntas del examen (requisito: usar ArrayList)
    private double calificacion;                    // Nota final obtenida (0.0 - 5.0)
    private boolean yaFueResuelto;                  // Bandera: evita que se resuelva dos veces

    // ─── CONSTRUCTOR ─────────────────────────────────────────────────────────────

    /**
     * Constructor de Examen.
     * @param nombre       Nombre del examen (ej: "Examen Día 1")
     * @param descripcion  Descripción del examen
     * @param temaEvaluado Tema que evalúa este examen
     */
    public Examen(String nombre, String descripcion, String temaEvaluado) {
        super(nombre, descripcion);         // Llama al constructor de Entidad
        this.temaEvaluado = temaEvaluado;   // Establece el tema a evaluar
        this.preguntas = new ArrayList<>();  // Inicializa la lista vacía de preguntas
        this.calificacion = 0.0;            // Nota inicial en 0
        this.yaFueResuelto = false;         // El examen aún no ha sido respondido
    }

    // ─── MÉTODO PARA AGREGAR PREGUNTAS ────────────────────────────────────────────

    /**
     * Agrega una pregunta al ArrayList del examen.
     * Requisito del proyecto: usar ArrayList<ClaseAbstracta> con polimorfismo.
     * @param pregunta objeto Pregunta a agregar
     */
    public void agregarPregunta(Pregunta pregunta) {
        preguntas.add(pregunta); // Agrega la pregunta al final del ArrayList
    }

    /**
     * Calcula la calificación final basada en respuestas correctas.
     * Fórmula: (correctas / total) * 5.0
     * El resultado es de 0.0 a 5.0 (escala colombiana universitaria).
     *
     * @param respuestasJugador Array con índices de las respuestas elegidas por el jugador
     * @return double - nota calculada de 0.0 a 5.0
     */
    public double calcularNota(int[] respuestasJugador) {
        // Si ya fue resuelto antes, retorna la calificación guardada para evitar trampas
        if (yaFueResuelto) {
            return calificacion;
        }

        int correctas = 0; // Contador de respuestas correctas

        // Recorre TODAS las preguntas del ArrayList usando el ciclo FOR-EACH obligatorio
        for (int i = 0; i < preguntas.size(); i++) {
            // Verifica si la respuesta del jugador en posición i es la respuesta correcta
            if (i < respuestasJugador.length) { // Evita IndexOutOfBoundsException (manejo de excepciones)
                if (preguntas.get(i).esCorrecta(respuestasJugador[i])) {
                    correctas++; // Incrementa el contador si acertó
                }
            }
        }

        // Calcula la nota proporcional: (aciertos / total preguntas) * 5
        // Si no hay preguntas, evita división por cero
        if (preguntas.isEmpty()) {
            calificacion = 0.0; // Si no hay preguntas, la nota es 0
        } else {
            calificacion = ((double) correctas / preguntas.size()) * 5.0; // Escala de 0 a 5
        }

        yaFueResuelto = true; // Marca el examen como ya resuelto
        return calificacion;  // Retorna la nota final
    }

    // ─── IMPLEMENTACIÓN DE ENTIDAD (métodos abstractos) ──────────────────────────

    /**
     * Devuelve información del examen.
     * @return String con datos del examen
     */
    @Override
    public String mostrarInfo() {
        // Retorna un resumen del examen con tema, número de preguntas y nota
        return "Examen: " + nombre + "\n"
             + "Tema: " + temaEvaluado + "\n"
             + "Preguntas: " + preguntas.size() + "\n"
             + "Calificación: " + calificacion;
    }

    /**
     * Ejecutar un examen significa iniciarlo (lógica visual la maneja la vista).
     */
    @Override
    public void ejecutarAccion() {
        System.out.println("[Examen] Iniciando examen de: " + temaEvaluado); // Log de inicio
    }

    // ─── IMPLEMENTACIÓN DE EVALUABLE (interfaz) ───────────────────────────────────

    /**
     * Implementación de Evaluable: devuelve la calificación.
     * @return double - nota de 0.0 a 5.0
     */
    @Override
    public double obtenerCalificacion() {
        return calificacion; // Devuelve la nota calculada
    }

    /**
     * Implementación de Evaluable: indica si aprobó (>=3.0).
     * @return boolean - true si aprobó
     */
    @Override
    public boolean aprobo() {
        return calificacion >= 3.0; // En Colombia, 3.0 es la nota mínima aprobatoria
    }

    /**
     * Implementación de Evaluable: devuelve el tema evaluado.
     * @return String - nombre del tema
     */
    @Override
    public String getTemaEvaluado() {
        return temaEvaluado; // Retorna el tema del examen
    }

    // ─── GETTERS ──────────────────────────────────────────────────────────────────

    /** @return ArrayList con todas las preguntas del examen */
    public ArrayList<Pregunta> getPreguntas() {
        return preguntas; // Retorna la lista completa de preguntas
    }

    /** @return true si el examen ya fue respondido */
    public boolean isYaFueResuelto() {
        return yaFueResuelto; // Retorna si ya se respondió
    }

    /**
     * Establece calificación directamente (útil para testing).
     * @param calificacion nota a establecer
     */
    public void setCalificacion(double calificacion) {
        this.calificacion = calificacion; // Establece la calificación directamente
    }
}
