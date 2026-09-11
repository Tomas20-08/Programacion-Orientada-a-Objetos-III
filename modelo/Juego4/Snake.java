package modelo.Juego4;

import interfaces.Jugable;
import modelo.Juego;
import vista.VentanaPrincipal;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class Snake extends Juego implements Jugable {

    private int puntaje;

    public Snake() {
        super("Snake", "Juego de habilidad y reflejos donde se debe recoger alimento sin colisionar.");
        this.puntaje = 0;
    }

    @Override
    public void iniciar(VentanaPrincipal menu) {
        SwingUtilities.invokeLater(() -> {
            // Ventana contenedora fullscreen con el fondo oscuro del Snake
            JFrame wrapper = new JFrame();
            wrapper.setExtendedState(JFrame.MAXIMIZED_BOTH);
            wrapper.setUndecorated(true);
            wrapper.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
            wrapper.getContentPane().setBackground(new Color(15, 17, 26));
            wrapper.setLayout(new GridBagLayout()); // centra el juego

            // GamePanel es el juego real (no necesitamos el JFrame SnakeGame)
            GamePanel panel = new GamePanel();
            wrapper.add(panel);

            // ESC → cierra wrapper y vuelve al menú
            wrapper.getRootPane().registerKeyboardAction(
                e -> { wrapper.dispose(); menu.setVisible(true); },
                KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0),
                JComponent.WHEN_IN_FOCUSED_WINDOW
            );

            wrapper.pack();
            wrapper.setExtendedState(JFrame.MAXIMIZED_BOTH); // re-maximizar tras pack()
            wrapper.setVisible(true);
            panel.requestFocusInWindow(); // el panel recibe las teclas del juego
        });
    }

    @Override
    public String getInstrucciones() {
        return "Controles: [Flechas de dirección] o [WASD].\n"
             + "Evitar el contacto con los bordes del mapa y el propio cuerpo de la serpiente.\n"
             + "[P] para pausar.";
    }

    @Override public void mostrarPuntaje() { System.out.println("Puntaje: " + puntaje); }
    @Override public void reiniciar()      { puntaje = 0; System.out.println(getNombre() + " reiniciado."); }
}
