package modelo.Juego3;

import java.awt.Color;
import javax.swing.JButton;

/**
 * ============================================================
 *  ABSTRACCIÓN
 * ============================================================
 *  CeldaBase es una clase abstracta. Define el "contrato" o
 *  esqueleto de lo que TODA celda debe tener, sin implementar
 *  todos los detalles. No se puede instanciar directamente.
 *
 *  Abstracción = mostrar solo lo esencial, ocultar complejidad.
 * ============================================================
 */
public abstract class CeldaBase extends JButton {

    // ============================================================
    //  ENCAPSULAMIENTO
    // ============================================================
    //  Los atributos son "protected" (no private) para que las
    //  subclases los hereden, pero siguen siendo inaccesibles
    //  desde fuera de la jerarquía de clases sin métodos get/set.
    //  Esto protege el estado interno del objeto.
    // ============================================================
    protected int fila;
    protected int columna;
    protected boolean revelada;
    protected boolean marcada;   // bandera del usuario

    /**
     * Constructor base: inicializa la posición y el estado visual.
     * Herencia: las subclases llaman a este constructor con super().
     */
    public CeldaBase(int fila, int columna) {
        this.fila     = fila;
        this.columna  = columna;
        this.revelada = false;
        this.marcada  = false;

        // Estilo visual común para todas las celdas
        setBackground(new Color(180, 180, 180));
        setFocusPainted(false);
        setFont(getFont().deriveFont(14f));
    }

    // ============================================================
    //  MÉTODO ABSTRACTO (Abstracción)
    // ============================================================
    //  revelar() no tiene implementación aquí porque cada tipo
    //  de celda (mina vs segura) se revela de forma diferente.
    //  Las subclases ESTÁN OBLIGADAS a implementarlo.
    // ============================================================
    public abstract void revelar();

    // ============================================================
    //  MÉTODO ABSTRACTO (Abstracción)
    // ============================================================
    //  esMina() tampoco se puede saber en la clase base;
    //  cada subclase lo define según su naturaleza.
    // ============================================================
    public abstract boolean esMina();

    // ============================================================
    //  ENCAPSULAMIENTO — Getters y Setters
    // ============================================================
    //  Acceso controlado al estado interno mediante métodos.
    //  Nadie puede cambiar fila/columna después de construir.
    // ============================================================
    public int getFila()     { return fila; }
    public int getColumna()  { return columna; }
    public boolean isRevelada() { return revelada; }

    public boolean isMarcada() { return marcada; }

    /**
     * Alterna la marca (bandera) de la celda.
     * Encapsulamiento: la lógica de marcado está aquí, no dispersa.
     */
    public void toggleMarca() {
        if (!revelada) {
            marcada = !marcada;
            if (marcada) {
                setText("🚩");
                setBackground(new Color(255, 220, 100));
            } else {
                setText("");
                setBackground(new Color(180, 180, 180));
            }
        }
    }
}
