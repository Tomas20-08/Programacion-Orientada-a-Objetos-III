package modelo.Juego1;

import modelo.Juego1.Form1;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;

/**
 * Clase principal - equivalente a Program.cs de C#
 */
public class Main {
    public static void main(String[] args) {
        // Usar MetalLookAndFeel (Java puro) para que respete los colores de los botones
        try {
            UIManager.setLookAndFeel("javax.swing.plaf.metal.MetalLookAndFeel");
        } catch (Exception e) {
            // Si falla, usa el look and feel por defecto de Java
        }

        // Equivalente a Application.Run(new Form1())
        SwingUtilities.invokeLater(() -> {
            Form1 ventana = new Form1();
            ventana.setVisible(true);
        });
    }
}
