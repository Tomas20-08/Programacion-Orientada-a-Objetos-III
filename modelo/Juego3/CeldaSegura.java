package modelo.Juego3;

import java.awt.Color;

/**
 * ============================================================
 *  HERENCIA
 * ============================================================
 *  CeldaSegura HEREDA de CeldaBase con "extends".
 *  Recibe automáticamente: fila, columna, revelada, marcada,
 *  toggleMarca(), getFila(), getColumna(), isRevelada().
 *  Solo agrega lo que la hace especial: el número de minas
 *  adyacentes y su forma específica de revelarse.
 * ============================================================
 */
public class CeldaSegura extends CeldaBase {

    // ============================================================
    //  ENCAPSULAMIENTO
    // ============================================================
    //  minasAdyacentes es private: solo esta clase lo modifica.
    //  Se expone de forma controlada con getMinasAdyacentes().
    // ============================================================
    private int minasAdyacentes;

    // Colores por número (como en el Buscaminas clásico)
    private static final Color[] COLORES_NUMERO = {
        new Color(180, 180, 180), // 0 — sin número
        new Color(0,   0,   255), // 1 — azul
        new Color(0,   128, 0),   // 2 — verde
        new Color(255, 0,   0),   // 3 — rojo
        new Color(0,   0,   128), // 4 — azul oscuro
        new Color(128, 0,   0),   // 5 — marrón
        new Color(0,   128, 128), // 6 — cian
        new Color(0,   0,   0),   // 7 — negro
        new Color(128, 128, 128)  // 8 — gris
    };

    /**
     * Constructor: llama al constructor de la clase padre con super().
     * Herencia: reutiliza la inicialización de CeldaBase.
     */
    public CeldaSegura(int fila, int columna) {
        super(fila, columna); // ← HERENCIA: constructor del padre
        this.minasAdyacentes = 0;
    }

    // ============================================================
    //  POLIMORFISMO
    // ============================================================
    //  revelar() está definido como abstracto en CeldaBase.
    //  Aquí damos la implementación ESPECÍFICA para CeldaSegura.
    //  Si llamamos celda.revelar() sobre una referencia CeldaBase,
    //  Java decide en tiempo de ejecución cuál versión ejecutar.
    //  Eso es polimorfismo dinámico (late binding).
    // ============================================================
    @Override
    public void revelar() {
        if (!revelada && !marcada) {
            revelada = true;
            setEnabled(false);

            if (minasAdyacentes == 0) {
                setText("");
                setBackground(new Color(210, 210, 210));
            } else {
                setText(String.valueOf(minasAdyacentes));
                setBackground(new Color(210, 210, 210));
                setForeground(COLORES_NUMERO[minasAdyacentes]);
            }
        }
    }

    // ============================================================
    //  POLIMORFISMO
    // ============================================================
    //  esMina() retorna false para celdas seguras.
    //  La misma llamada sobre CeldaMina retorna true.
    //  Mismo método, comportamiento diferente → polimorfismo.
    // ============================================================
    @Override
    public boolean esMina() {
        return false;
    }

    // Encapsulamiento: setter controlado
    public void setMinasAdyacentes(int n) {
        this.minasAdyacentes = n;
    }

    // Encapsulamiento: getter
    public int getMinasAdyacentes() {
        return minasAdyacentes;
    }
}
