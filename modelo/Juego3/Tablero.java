package modelo.Juego3;

import java.util.Random;

/**
 * ============================================================
 *  ENCAPSULAMIENTO
 * ============================================================
 *  Tablero encapsula TODA la lógica del juego.
 *  El exterior (la ventana) solo llama métodos públicos;
 *  no sabe cómo están distribuidas las minas ni cómo se
 *  calcula el flood-fill. Los detalles están ocultos aquí.
 * ============================================================
 */
public class Tablero {

    // ============================================================
    //  ENCAPSULAMIENTO — atributos privados
    // ============================================================
    private final int filas;
    private final int columnas;
    private final int totalMinas;
    private int celdasRestantes;   // celdas seguras sin revelar
    private boolean juegoTerminado;

    /**
     * ============================================================
     *  POLIMORFISMO con arrays
     * ============================================================
     *  El array es de tipo CeldaBase[][] (tipo padre).
     *  Cada posición puede contener un CeldaSegura O un CeldaMina.
     *  Cuando llamamos celda.revelar() o celda.esMina(), Java
     *  decide en tiempo de ejecución qué método ejecutar.
     *  Esto es polimorfismo de inclusión (Liskov Substitution).
     * ============================================================
     */
    private CeldaBase[][] celdas;

    /**
     * Constructor: inicializa el tablero con dimensiones y minas.
     * Encapsulamiento: toda la creación ocurre aquí.
     */
    public Tablero(int filas, int columnas, int totalMinas) {
        this.filas          = filas;
        this.columnas       = columnas;
        this.totalMinas     = totalMinas;
        this.celdasRestantes = (filas * columnas) - totalMinas;
        this.juegoTerminado = false;

        inicializarCeldas();
        colocarMinas();
        calcularAdyacentes();
    }

    /**
     * PRIVADO — detalle interno: crea celdas seguras por defecto.
     * Encapsulamiento: nadie fuera sabe cómo se inicializa.
     */
    private void inicializarCeldas() {
        celdas = new CeldaBase[filas][columnas];
        for (int f = 0; f < filas; f++) {
            for (int c = 0; c < columnas; c++) {
                // ================================================
                //  HERENCIA + POLIMORFISMO
                // ================================================
                //  Creamos CeldaSegura pero la guardamos como
                //  CeldaBase. El tipo de referencia es el padre;
                //  el objeto real es la subclase. Polimorfismo.
                // ================================================
                celdas[f][c] = new CeldaSegura(f, c);
            }
        }
    }

    /**
     * PRIVADO — coloca minas en posiciones aleatorias.
     */
    private void colocarMinas() {
        Random rand = new Random();
        int minasColocadas = 0;

        while (minasColocadas < totalMinas) {
            int f = rand.nextInt(filas);
            int c = rand.nextInt(columnas);

            // Solo reemplazar si todavía es celda segura
            if (!celdas[f][c].esMina()) {
                // ================================================
                //  POLIMORFISMO
                // ================================================
                //  Reemplazamos la CeldaSegura por CeldaMina.
                //  La referencia CeldaBase apunta ahora a una
                //  instancia diferente. El resto del código no
                //  necesita cambiar — solo llama a esMina().
                // ================================================
                celdas[f][c] = new CeldaMina(f, c);
                minasColocadas++;
            }
        }
    }

    /**
     * PRIVADO — calcula cuántas minas rodean cada celda segura.
     */
    private void calcularAdyacentes() {
        for (int f = 0; f < filas; f++) {
            for (int c = 0; c < columnas; c++) {
                // ================================================
                //  POLIMORFISMO — esMina() decide el tipo real
                // ================================================
                if (!celdas[f][c].esMina()) {
                    int count = contarMinasAlrededor(f, c);
                    // Downcasting seguro: sabemos que NO es mina
                    ((CeldaSegura) celdas[f][c]).setMinasAdyacentes(count);
                }
            }
        }
    }

    /**
     * PRIVADO — cuenta minas en las 8 celdas vecinas.
     */
    private int contarMinasAlrededor(int f, int c) {
        int count = 0;
        for (int df = -1; df <= 1; df++) {
            for (int dc = -1; dc <= 1; dc++) {
                int nf = f + df, nc = c + dc;
                if (nf >= 0 && nf < filas && nc >= 0 && nc < columnas) {
                    if (celdas[nf][nc].esMina()) count++;
                }
            }
        }
        return count;
    }

    // ============================================================
    //  MÉTODO PÚBLICO — interfaz controlada (Encapsulamiento)
    // ============================================================
    /**
     * Procesa el clic izquierdo del usuario sobre una celda.
     * @return true si el juego continúa, false si el usuario perdió.
     */
    public boolean revelarCelda(int f, int c) {
        CeldaBase celda = celdas[f][c];

        if (celda.isRevelada() || celda.isMarcada()) return true;

        // ========================================================
        //  POLIMORFISMO — celda.revelar() ejecuta el método
        //  correcto según si es CeldaMina o CeldaSegura.
        //  No necesitamos if/else para checar el tipo.
        // ========================================================
        celda.revelar();

        if (celda.esMina()) {
            juegoTerminado = true;
            revelarTodasLasMinas((CeldaMina) celda);
            return false; // perdió
        }

        // Si es celda segura con 0 minas adyacentes → flood fill
        CeldaSegura segura = (CeldaSegura) celda;
        celdasRestantes--;

        if (segura.getMinasAdyacentes() == 0) {
            expandirVacias(f, c);
        }

        if (celdasRestantes == 0) {
            juegoTerminado = true;
        }

        return true;
    }

    /**
     * Flood-fill recursivo: revela celdas vacías conectadas.
     * Encapsulamiento: algoritmo interno, no visible desde fuera.
     */
    private void expandirVacias(int f, int c) {
        for (int df = -1; df <= 1; df++) {
            for (int dc = -1; dc <= 1; dc++) {
                int nf = f + df, nc = c + dc;
                if (nf < 0 || nf >= filas || nc < 0 || nc >= columnas) continue;

                CeldaBase vecina = celdas[nf][nc];
                if (vecina.isRevelada() || vecina.esMina() || vecina.isMarcada()) continue;

                vecina.revelar(); // Polimorfismo: siempre CeldaSegura aquí
                celdasRestantes--;

                if (((CeldaSegura) vecina).getMinasAdyacentes() == 0) {
                    expandirVacias(nf, nc); // recursión
                }
            }
        }
    }

    /**
     * Al perder: muestra todas las minas del tablero.
     */
    private void revelarTodasLasMinas(CeldaMina minaDetonada) {
        for (int f = 0; f < filas; f++) {
            for (int c = 0; c < columnas; c++) {
                // ================================================
                //  POLIMORFISMO — esMina() identifica el tipo real
                // ================================================
                if (celdas[f][c].esMina()) {
                    CeldaMina mina = (CeldaMina) celdas[f][c];
                    if (mina == minaDetonada) {
                        mina.revelar();          // la que pisó: rojo
                    } else {
                        mina.revelarSinDetonar(); // las demás: naranja
                    }
                }
            }
        }
    }

    /**
     * Alterna bandera (clic derecho del usuario).
     */
    public void marcarCelda(int f, int c) {
        if (!celdas[f][c].isRevelada()) {
            celdas[f][c].toggleMarca(); // heredado de CeldaBase
        }
    }

    // ============================================================
    //  GETTERS — Encapsulamiento: acceso de solo lectura
    // ============================================================
    public CeldaBase getCelda(int f, int c) { return celdas[f][c]; }
    public int getFilas()          { return filas; }
    public int getColumnas()       { return columnas; }
    public boolean isJuegoTerminado() { return juegoTerminado; }
    public boolean isGanado()      { return celdasRestantes == 0 && !juegoTerminado || (juegoTerminado && celdasRestantes == 0); }
}
