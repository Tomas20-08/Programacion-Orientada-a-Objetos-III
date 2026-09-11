package modelo.Juego5; // Pertenece al paquete modelo

/**
 * CLASE: Pregunta
 * -------------------------------------------------------------
 * Tercera clase HIJA de Entidad (Herencia).
 * Representa una pregunta de opción múltiple dentro de un Examen.
 *
 * Contiene el enunciado, las 4 opciones de respuesta y el índice correcto.
 * Se usa dentro del ArrayList<Pregunta> en la clase Examen.
 *
 * Aplica: Herencia, Encapsulamiento
 */
public class Pregunta extends Entidad {

    // ─── ATRIBUTOS PROPIOS DE PREGUNTA ────────────────────────────────────────────
    private String[] opciones;      // Array con las 4 opciones de respuesta (A, B, C, D)
    private int respuestaCorrecta;  // Índice de la opción correcta (0=A, 1=B, 2=C, 3=D)
    private String explicacion;     // Explicación de por qué esa es la respuesta correcta

    // ─── CONSTRUCTOR ─────────────────────────────────────────────────────────────

    /**
     * Constructor de Pregunta.
     * @param enunciado         Texto de la pregunta
     * @param opciones          Array con 4 opciones de respuesta
     * @param respuestaCorrecta Índice (0-3) de la respuesta correcta
     * @param explicacion       Explicación de la respuesta correcta
     */
    public Pregunta(String enunciado, String[] opciones, int respuestaCorrecta, String explicacion) {
        super(enunciado, "Pregunta de selección múltiple"); // Llama al constructor padre
        this.opciones = opciones;                           // Asigna las 4 opciones
        this.respuestaCorrecta = respuestaCorrecta;         // Guarda el índice correcto
        this.explicacion = explicacion;                     // Guarda la explicación
    }

    // ─── MÉTODOS ABSTRACTOS IMPLEMENTADOS ────────────────────────────────────────

    /**
     * Muestra la pregunta completa con sus opciones.
     * @return String formateado con la pregunta y opciones
     */
    @Override
    public String mostrarInfo() {
        // Construye el texto de la pregunta con todas sus opciones
        StringBuilder sb = new StringBuilder(); // StringBuilder es más eficiente que concatenar Strings
        sb.append("❓ ").append(nombre).append("\n"); // Agrega el enunciado (nombre es el enunciado)

        // Letras de las opciones para mostrar (A, B, C, D)
        String[] letras = {"A", "B", "C", "D"};

        // Recorre las opciones y las agrega al texto
        for (int i = 0; i < opciones.length; i++) {
            sb.append("   ").append(letras[i]).append(") ").append(opciones[i]).append("\n");
        }

        return sb.toString(); // Retorna el texto completo formateado
    }

    /**
     * Ejecutar una pregunta significa presentarla al jugador.
     */
    @Override
    public void ejecutarAccion() {
        System.out.println("[Pregunta] Presentando: " + nombre); // Log de presentación
    }

    // ─── MÉTODO PRINCIPAL: verificar si la respuesta es correcta ──────────────────

    /**
     * Verifica si el índice elegido por el jugador es la respuesta correcta.
     * @param indiceElegido Índice (0-3) elegido por el jugador
     * @return boolean - true si es correcta, false si no
     */
    public boolean esCorrecta(int indiceElegido) {
        return indiceElegido == respuestaCorrecta; // Compara directamente los índices
    }

    // ─── GETTERS ──────────────────────────────────────────────────────────────────

    /**
     * Retorna el enunciado de la pregunta.
     * @return String - enunciado
     */
    public String getEnunciado() {
        return nombre; // El enunciado está almacenado en el atributo 'nombre' heredado
    }

    /**
     * Retorna todas las opciones de respuesta.
     * @return String[] - array con las 4 opciones
     */
    public String[] getOpciones() {
        return opciones; // Retorna el array de opciones
    }

    /**
     * Retorna el índice de la respuesta correcta.
     * @return int - índice (0-3)
     */
    public int getRespuestaCorrecta() {
        return respuestaCorrecta; // Retorna el índice correcto
    }

    /**
     * Retorna la explicación de la respuesta correcta.
     * @return String - explicación
     */
    public String getExplicacion() {
        return explicacion; // Retorna la explicación didáctica
    }
}
