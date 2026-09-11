package modelo.Juego3;

import java.awt.Color;

/**
 * ============================================================
 *  HERENCIA
 * ============================================================
 *  CeldaMina también hereda de CeldaBase.
 *  Comparte la misma estructura que CeldaSegura (fila, columna,
 *  marcada, revelada, toggleMarca...) pero tiene comportamiento
 *  completamente distinto al revelarse.
 *
 *  Dos clases hermanas, mismo padre → herencia múltiple de
 *  implementación NO existe en Java, pero sí de una sola clase.
 * ============================================================
 */
public class CeldaMina extends CeldaBase {

    // ============================================================
    //  ENCAPSULAMIENTO
    // ============================================================
    //  detonada: estado privado, solo cambia dentro de esta clase.
    // ============================================================
    private boolean detonada;

    /**
     * Constructor: delega al padre con super().
     */
    public CeldaMina(int fila, int columna) {
        super(fila, columna); // ← HERENCIA
        this.detonada = false;
    }

    // ============================================================
    //  POLIMORFISMO — Sobrescritura (Override)
    // ============================================================
    //  revelar() tiene un comportamiento TOTALMENTE diferente
    //  al de CeldaSegura: muestra la bomba y pinta de rojo.
    //  El compilador no sabe cuál versión ejecutar hasta el
    //  momento de correr el programa (dynamic dispatch).
    // ============================================================
    @Override
    public void revelar() {
        if (!revelada) {
            revelada  = true;
            detonada  = true;
            setEnabled(false);
            setText("💣");
            setBackground(new Color(255, 80, 80));
        }
    }

    /**
     * Muestra la mina sin marcarla como detonada (para el
     * final del juego: revelar todas las minas no pisadas).
     */
    public void revelarSinDetonar() {
        if (!revelada) {
            revelada = true;
            setEnabled(false);
            setText("💣");
            setBackground(new Color(255, 180, 80));
        }
    }

    // ============================================================
    //  POLIMORFISMO
    // ============================================================
    //  esMina() retorna true → opuesto a CeldaSegura.
    //  Permite: if (celda.esMina()) sin saber el tipo real.
    // ============================================================
    @Override
    public boolean esMina() {
        return true;
    }

    // Encapsulamiento: solo lectura desde afuera
    public boolean isDetonada() {
        return detonada;
    }
}
