package modelo.Juego2;

import interfaces.Jugable;
import modelo.Juego;
import vista.VentanaPrincipal;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class Memoria extends Juego implements Jugable {

    private int puntaje;

    public Memoria() {
        super("Memoria", "Juego de emparejamiento de fichas para evaluar la retentiva visual.");
        this.puntaje = 0;
    }

    @Override
    public void iniciar(VentanaPrincipal menu) {
        SwingUtilities.invokeLater(() -> {
            // Ventana contenedora fullscreen
            JFrame wrapper = new JFrame();
            wrapper.setExtendedState(JFrame.MAXIMIZED_BOTH);
            wrapper.setUndecorated(true);
            wrapper.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
            wrapper.getContentPane().setBackground(Color.WHITE);
            wrapper.setLayout(new GridBagLayout()); // centra el juego automáticamente

            // Juego real dentro del wrapper
            Main juegoMemoria = new Main();
            juegoMemoria.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
            wrapper.add(juegoMemoria.getContentPane());

            // ESC → cierra wrapper y vuelve al menú
            wrapper.getRootPane().registerKeyboardAction(
                e -> { wrapper.dispose(); menu.setVisible(true); },
                KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0),
                JComponent.WHEN_IN_FOCUSED_WINDOW
            );

            wrapper.setVisible(true);
        });
    }

    @Override
    public String getInstrucciones() {
        return "Controles: [Click] para voltear dos fichas.\n"
             + "Si coinciden, permanecen visibles; si no, se ocultan nuevamente.";
    }

    @Override public void mostrarPuntaje() { System.out.println("Puntaje: " + puntaje); }
    @Override public void reiniciar()      { puntaje = 0; System.out.println(getNombre() + " reiniciado."); }
}
