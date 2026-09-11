package modelo.Juego5; // Paquete donde vive esta interfaz

/**
 * INTERFAZ: Evaluable
 * -------------------------------------------------------------
 * Requisito obligatorio del proyecto: "Se debe implementar al menos una interfaz".
 * Esta interfaz representa el CONTRATO que deben cumplir todos los
 * elementos que puedan ser evaluados dentro del juego (exámenes, días, etc.).
 *
 * En POO, una interfaz define QUÉ debe hacerse, no CÓMO.
 * Las clases que la implementen están obligadas a definir todos sus métodos.
 */
public interface Evaluable {

    /**
     * Método que devuelve la calificación obtenida.
     * Toda entidad evaluable DEBE poder devolver una nota de 0.0 a 5.0.
     * @return double - la nota del evaluado
     */
    double obtenerCalificacion();

    /**
     * Método que indica si el evaluado aprobó (nota >= 3.0 en Colombia).
     * @return boolean - true si aprobó, false si reprobó
     */
    boolean aprobo();

    /**
     * Método que devuelve el nombre del tema evaluado.
     * Útil para mostrar en pantalla de qué trató el examen.
     * @return String - nombre del tema
     */
    String getTemaEvaluado();
}
