package modelo.Juego3;

import interfaces.Jugable;
import modelo.Juego;
import vista.VentanaPrincipal;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class Buscaminas extends Juego implements Jugable {

    private int puntaje;

    public Buscaminas() {
        super("Buscaminas", "Puzzle de lógica y despeje de campos mediante indicadores numéricos.");
        this.puntaje = 0;
    }

    @Override
    public void iniciar(VentanaPrincipal menu) {
        SwingUtilities.invokeLater(() -> {
            // Ventana contenedora fullscreen con el fondo oscuro del juego
            JFrame wrapper = new JFrame();
            wrapper.setExtendedState(JFrame.MAXIMIZED_BOTH);
            wrapper.setUndecorated(true);
            wrapper.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
            wrapper.getContentPane().setBackground(new Color(45, 45, 48));
            wrapper.setLayout(new GridBagLayout()); // centra el juego

            // Juego real dentro del wrapper
            VentanaBuscaMinas juego = new VentanaBuscaMinas();
            juego.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
            juego.setVisible(false); // no mostramos su ventana propia
            wrapper.add(juego.getContentPane());

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
        return "Controles: [Click Izquierdo] para revelar casilla; [Click Derecho] para marcar mina.\n"
             + "El número indica bombas adyacentes.";
    }

    @Override public void mostrarPuntaje() { System.out.println("Puntaje: " + puntaje); }
    @Override public void reiniciar()      { puntaje = 0; System.out.println(getNombre() + " reiniciado."); }
}
