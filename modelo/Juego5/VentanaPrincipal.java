package modelo.Juego5;

import javax.swing.*;
import java.awt.*;

public class VentanaPrincipal extends JFrame {

    private static final int JUEGO_ANCHO  = 1024;
    private static final int JUEGO_ALTO   = 680;

    private CardLayout cardLayout;
    private JPanel     panelContenedor;

    private PanelNarrativa panelNarrativa;
    private PanelExamen    panelExamen;
    private PanelFinal     panelFinal;

    public static final String PANTALLA_NARRATIVA = "NARRATIVA";
    public static final String PANTALLA_EXAMEN    = "EXAMEN";
    public static final String PANTALLA_FINAL     = "FINAL";

    public VentanaPrincipal() {
        super("Sergio's Programming - El Curso Intensivo");

        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        GestorRecursos.getInstance().precargarTodo();

        inicializarPaneles();

        // Panel exterior negro que ocupa toda la pantalla y centra el juego
        JPanel panelFondo = new JPanel(new GridBagLayout());
        panelFondo.setBackground(Color.BLACK);

        // El juego siempre mide 1024x680, sin importar el monitor
        panelContenedor.setPreferredSize(new Dimension(JUEGO_ANCHO, JUEGO_ALTO));
        panelContenedor.setMinimumSize(new Dimension(JUEGO_ANCHO, JUEGO_ALTO));
        panelContenedor.setMaximumSize(new Dimension(JUEGO_ANCHO, JUEGO_ALTO));

        panelFondo.add(panelContenedor); // GridBagLayout lo centra automáticamente

        add(panelFondo);
    }

    private void inicializarPaneles() {
        cardLayout     = new CardLayout();
        panelContenedor = new JPanel(cardLayout);
        panelContenedor.setBackground(Color.BLACK);

        panelNarrativa = new PanelNarrativa(this);
        panelExamen    = new PanelExamen(this);
        panelFinal     = new PanelFinal(this);

        panelContenedor.add(panelNarrativa, PANTALLA_NARRATIVA);
        panelContenedor.add(panelExamen,    PANTALLA_EXAMEN);
        panelContenedor.add(panelFinal,     PANTALLA_FINAL);

        cardLayout.show(panelContenedor, PANTALLA_NARRATIVA);
    }

    // ─── Navegación ───────────────────────────────────────────────────────────────

    public void mostrarNarrativa() {
        cardLayout.show(panelContenedor, PANTALLA_NARRATIVA);
        panelNarrativa.requestFocusInWindow();
    }

    public void mostrarExamen() {
        cardLayout.show(panelContenedor, PANTALLA_EXAMEN);
    }

    public void mostrarFinal(int tipoFinal, String mensajeExtra) {
        cardLayout.show(panelContenedor, PANTALLA_FINAL);
        panelFinal.iniciarFinal(tipoFinal, mensajeExtra);
    }

    public void cerrarAbruptamente() {
        System.exit(0);
    }

    // ─── Getters ──────────────────────────────────────────────────────────────────

    public PanelNarrativa getPanelNarrativa() { return panelNarrativa; }
    public PanelExamen    getPanelExamen()     { return panelExamen; }
    public PanelFinal     getPanelFinal()      { return panelFinal; }
}
