package modelo.Juego5; // Pertenece al paquete modelo

import java.util.ArrayList; // ArrayList para la colección polimórfica de entidades

public class EstadoJuego {

    // ─── SINGLETON ────────────────────────────────────────────────────────────────
    private static EstadoJuego instancia = null; // Única instancia

    // ─── ATRIBUTOS DE ESTADO ──────────────────────────────────────────────────────
    private int diaActual;           // Día actual del curso (1 al 5)
    private double notaAcumulada;    // Suma de notas de todos los días
    private double[] notasPorDia;    // Nota de cada día: [0]=día1 … [4]=día5

    // FLAGS DE FINALES
    private boolean finalMaloActivado;       // true = activó un final malo temprano
    private boolean rutaFinalOcultoActiva;   // true = aún puede llegar al final oculto
    private boolean roboInstaladores;        // true = robó los instaladores del profe
    private boolean insistioVidaPasada;      // true = preguntó por la vida de Sergio
    private boolean accedioComputador;       // true = husmeó el computador
    private int     opcionComputador;        // 1=System32, 2=instaladores, 3=Base de Datos
    private int     vecesInsistio;           // Veces que preguntó por la vida de Sergio

    // ─── COLECCIÓN POLIMÓRFICA (REQUISITO DEL PROYECTO) ──────────────────────────
    private ArrayList<Entidad> entidades; // Personajes + Exámenes registrados
    private Personaje maestroSergio;
    private Personaje jugador;

    // ─── CONSTRUCTOR PRIVADO ─────────────────────────────────────────────────────
    private EstadoJuego() {
        reiniciar(); // Inicializa todo al crear la instancia
    }

    // ─── SINGLETON ───────────────────────────────────────────────────────────────
    public static EstadoJuego getInstance() {
        if (instancia == null) {
            instancia = new EstadoJuego();
        }
        return instancia;
    }

    // ─────────────────────────────────────────────────────────────────────────────
    //  reiniciar(): NUEVA PARTIDA COMPLETA
    //  Borra TODO: notas, flags, día actual. Llamar solo al empezar de cero.
    // ─────────────────────────────────────────────────────────────────────────────
    public void reiniciar() {
        diaActual             = 1;           // Empieza en el día 1
        notaAcumulada         = 0.0;         // Sin notas
        notasPorDia           = new double[5]; // Array vacío de 5 días

        // Todos los flags en su valor inicial
        finalMaloActivado     = false;
        rutaFinalOcultoActiva = true;  // Al inicio, el final oculto ES posible
        roboInstaladores      = false;
        insistioVidaPasada    = false;
        accedioComputador     = false;
        opcionComputador      = 0;
        vecesInsistio         = 0;

        // Colección polimórfica
        entidades = new ArrayList<>();

        maestroSergio = new Personaje(
            "Sergio Mora",
            "El legendario maestro conocido como 'La Muerte Blanca'. " +
            "Ningún estudiante ha aprobado su curso intensivo... aún.",
            "La Muerte Blanca",
            "Sprite1",
            10
        );
        jugador = new Personaje(
            "Tú",
            "Estudiante sobreviviente de 10 semestres. Solo falta una materia... Algoritmos I.",
            "El Último Sobreviviente",
            "jugador",
            10
        );
        entidades.add(maestroSergio);
        entidades.add(jugador);
    }

    // ─────────────────────────────────────────────────────────────────────────────
    //  avanzarAlSiguienteDia(): entre días (NO borra notas ni flags)
    //  El ControladorJuego usa esto en lugar de reiniciar() entre días.
    // ─────────────────────────────────────────────────────────────────────────────
    public void avanzarAlSiguienteDia() {
        diaActual++; // Solo incrementa el día; las notas y flags se conservan
    }

    // ─── REGISTRAR NOTA DEL DÍA ──────────────────────────────────────────────────
    /**
     * Guarda la nota del día actual y la suma a la acumulada.
     * @param nota nota de 0.0 a 5.0
     */
    public void registrarNotaDia(double nota) {
        if (diaActual >= 1 && diaActual <= 5) {
            notasPorDia[diaActual - 1] = nota; // Índice 0-based: día 1 → posición 0
            notaAcumulada += nota;
        }
    }

    // ─── POLIMORFISMO OBLIGATORIO ─────────────────────────────────────────────────
    /**
     * Recorre el ArrayList<Entidad> con for-each y llama al método abstracto.
     * Requisito del proyecto: colección polimórfica + for-each.
     */
    public String mostrarTodasLasEntidades() {
        StringBuilder sb = new StringBuilder();
        for (Entidad e : entidades) {       // FOR-EACH sobre la colección polimórfica
            sb.append(e.mostrarInfo());     // Polimorfismo: cada subclase imprime distinto
            sb.append("\n---\n");
        }
        return sb.toString();
    }

    /** Agrega un examen al ArrayList (lo guarda como Entidad → polimorfismo). */
    public void registrarExamen(Examen examen) {
        entidades.add(examen);
    }

    // ─── LÓGICA DE FINALES ────────────────────────────────────────────────────────

    /**
     * Verifica si el jugador cumple los TRES requisitos del Final Oculto:
     *  1. rutaFinalOcultoActiva = true (no preguntó por la vida de Sergio)
     *  2. roboInstaladores = true (robó los instaladores en el día 3)
     *  3. Todos los 5 días tuvieron nota exactamente 5.0
     *
     * CORRECCIÓN: antes se llamaba antes de registrar la nota del día 5.
     * Ahora el controlador registra la nota ANTES de llamar a determinarFinal().
     */
    public boolean cumpleRequisitosOculto() {
        if (!rutaFinalOcultoActiva) return false; // Insistió → perdió la ruta
        if (!roboInstaladores)      return false; // No robó instaladores → no aplica

        // Verifica que TODOS los días tengan 5.0 (ahora ya incluye el día 5)
        for (int i = 0; i < 5; i++) {
            if (notasPorDia[i] < 5.0) {
                System.out.println("[EstadoJuego] Día " + (i+1) + " no es 5.0 → " + notasPorDia[i]);
                return false;
            }
        }
        System.out.println("[EstadoJuego] ✓ Todos los requisitos del Final Oculto cumplidos.");
        return true;
    }

    /**
     * Determina qué final mostrar al acabar el día 5.
     * Llamar DESPUÉS de registrarNotaDia() del día 5.
     *
     * @return 0=FinalMalo, 1=FinalBueno, 2=FinalOculto
     */
    public int determinarFinal() {
        if (finalMaloActivado) return 0; // Ya activó un final malo antes

        double notaDia5 = notasPorDia[4]; // Nota del día 5 (índice 4)
        System.out.println("[EstadoJuego] Nota día 5: " + notaDia5);
        System.out.println("[EstadoJuego] Notas acumuladas: " + java.util.Arrays.toString(notasPorDia));
        System.out.println("[EstadoJuego] roboInstaladores=" + roboInstaladores
                         + " | rutaOculta=" + rutaFinalOcultoActiva);

        if (notaDia5 < 4.0) return 0;    // Reprobó el día 5 → Final Malo
        if (cumpleRequisitosOculto()) return 2; // Cumple todo → Final Oculto
        return 1;                          // Aprobó pero sin requisitos ocultos → Final Bueno
    }

    // ─── GETTERS Y SETTERS ────────────────────────────────────────────────────────

    public int    getDiaActual()       { return diaActual; }
    public double getNotaAcumulada()   { return notaAcumulada; }
    public double[] getNotasPorDia()   { return notasPorDia; }
    public double getNotaDia(int dia)  { return notasPorDia[dia - 1]; } // Día 1 = índice 0

    public boolean isFinalMaloActivado()      { return finalMaloActivado; }
    public void    setFinalMaloActivado(boolean v) { finalMaloActivado = v; }

    public boolean isRutaFinalOcultoActiva()       { return rutaFinalOcultoActiva; }
    public void    setRutaFinalOcultoActiva(boolean v) { rutaFinalOcultoActiva = v; }

    public boolean isRoboInstaladores()       { return roboInstaladores; }
    public void    setRoboInstaladores(boolean v) { roboInstaladores = v; }

    public boolean isInsistioVidaPasada()         { return insistioVidaPasada; }
    public void    setInsistioVidaPasada(boolean v)   { insistioVidaPasada = v; }

    public boolean isAccedioComputador()          { return accedioComputador; }
    public void    setAccedioComputador(boolean v)    { accedioComputador = v; }

    public int  getOpcionComputador()             { return opcionComputador; }
    public void setOpcionComputador(int v)        { opcionComputador = v; }

    public int  getVecesInsistio()                { return vecesInsistio; }

    /** Incrementa el contador de insistencias y pierde la ruta oculta. */
    public void incrementarInsistencia() {
        vecesInsistio++;
        rutaFinalOcultoActiva = false; // Perder la ruta desde la primera insistencia
        insistioVidaPasada    = true;
    }

    public Personaje           getMaestroSergio() { return maestroSergio; }
    public Personaje           getJugador()       { return jugador; }
    public ArrayList<Entidad>  getEntidades()     { return entidades; }

    /** Calcula el promedio de los días completados hasta ahora. */
    public double calcularPromedio() {
        int completados = diaActual - 1;
        if (completados <= 0) return 0.0;
        double suma = 0;
        for (int i = 0; i < completados; i++) suma += notasPorDia[i];
        return suma / completados;
    }
}