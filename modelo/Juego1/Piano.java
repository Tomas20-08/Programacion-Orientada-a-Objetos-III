package modelo.Juego1;

import interfaces.Jugable;
import modelo.Juego;
import vista.VentanaPrincipal;

import javax.swing.*;
import java.awt.event.*;

public class Piano extends Juego implements Jugable {

    private int puntaje;

    public Piano() {
        super("Piano", "Simulador Virtual De Piano");
        this.puntaje = 0;
    }

    @Override
    public void iniciar(VentanaPrincipal menu) {
        SwingUtilities.invokeLater(() -> {
            try {
                UIManager.setLookAndFeel("javax.swing.plaf.metal.MetalLookAndFeel");
            } catch (Exception e) {}

            Form1 ventana = new Form1();

            // ESC → cierra el piano y vuelve al menú
            ventana.getRootPane().registerKeyboardAction(
                e -> { ventana.dispose(); menu.setVisible(true); },
                KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0),
                JComponent.WHEN_IN_FOCUSED_WINDOW
            );

            ventana.setVisible(true);
        });
    }

    @Override
    public String getInstrucciones() {
        return  "Controles:\n"
              + "\nOctava 4\n"
              + "Teclas Blancas: | Z | X | C | V | B | N | M | (Do Re Mi Fa Sol La Si)\n"
              + "Teclas Negras:  | S | D | G | H | J | (Do# Re# Fa# Sol# La#)\n"
              + "\nOctava 5\n"
              + "Teclas Blancas: | Q | W | E | R | T | Y | U | I | (Do Re Mi Fa Sol La Si Do)\n"
              + "Teclas Negras:  | 2 | 3 | 5 | 6 | 7 | (Do# Re# Fa# Sol# La#)";
    }

    @Override public void mostrarPuntaje() { System.out.println("Puntaje: " + puntaje); }
    @Override public void reiniciar()      { puntaje = 0; System.out.println(getNombre() + " reiniciado."); }
}
