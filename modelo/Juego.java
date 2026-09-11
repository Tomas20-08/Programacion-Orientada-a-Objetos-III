package modelo;

import vista.VentanaPrincipal;

// CLASE ABSTRACTA: núcleo del sistema
public abstract class Juego {

    private String nombre;
    private String descripcion;

    public Juego(String nombre, String descripcion) {
        this.nombre = nombre;
        this.descripcion = descripcion;
    }

    public String getNombre()     { return nombre; }
    public String getDescripcion(){ return descripcion; }

    // Cada juego implementa cómo lanzarse y cómo volver al menú
    public abstract void iniciar(VentanaPrincipal menu);
    public abstract String getInstrucciones();
}
