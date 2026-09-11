package modelo.Juego5;

import interfaces.Jugable;
import modelo.Juego;
import vista.VentanaPrincipal; // menú principal

import javax.swing.*;
import java.awt.event.*;

public class NovelaGrafica extends Juego implements Jugable {

    private int puntaje;

    public NovelaGrafica() {
        super("Novela Grafica", "Narrativa interactiva basada en texto y toma de decisiones.");
        this.puntaje = 0;
    }

    @Override
    public void iniciar(VentanaPrincipal menu) {
        SwingUtilities.invokeLater(() -> {
            // Usamos nombre completo para evitar conflicto con vista.VentanaPrincipal
            modelo.Juego5.VentanaPrincipal ventana = new modelo.Juego5.VentanaPrincipal();
            ventana.setResizable(true);
            ventana.setUndecorated(true);
            ventana.setExtendedState(JFrame.MAXIMIZED_BOTH);
            ventana.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

            // ── CORRECCIÓN: detener música al cerrar la ventana (ESC o dispose) ────
            // WindowListener detecta cuando la ventana se cierra de cualquier forma
            ventana.addWindowListener(new WindowAdapter() {
                @Override
                public void windowClosed(WindowEvent e) {
                    // Detiene la música inmediatamente al salir al menú principal
                    GestorMusica.getInstance().detener();
                    menu.setVisible(true);
                }
            });

            // ESC → cierra la novela (el WindowListener de arriba se encarga de lo demás)
            ventana.getRootPane().registerKeyboardAction(
                e -> ventana.dispose(),
                KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0),
                JComponent.WHEN_IN_FOCUSED_WINDOW
            );

            ventana.setVisible(true);

            ControladorJuego controlador = ControladorJuego.getInstance(ventana);
            controlador.iniciarJuego();
        });
    }

    @Override
    public String getInstrucciones() {
        return "Controles: [Click] para avanzar.\n"
             + "Selección de opciones mediante puntero para ramificar la historia.";
    }

    @Override public void mostrarPuntaje() { System.out.println("Puntaje: " + puntaje); }
    @Override public void reiniciar()      { puntaje = 0; System.out.println(getNombre() + " reiniciado."); }
}