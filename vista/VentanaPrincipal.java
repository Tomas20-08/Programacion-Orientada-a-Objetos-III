package vista;

import logica.GestorJuegos;
import modelo.Juego;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.util.ArrayList;

// VISTA: Pantalla principal con bienvenida y menú de juegos
public class VentanaPrincipal extends JFrame {

    private GestorJuegos gestor;

    public VentanaPrincipal() {
        gestor = new GestorJuegos(this); // pasa 'this' para que los juegos puedan volver
        inicializarGUI();
    }

    private void inicializarGUI() {
        setTitle(" Arcade de Juegos");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setResizable(false);

        // ── PANTALLA COMPLETA ──────────────────────────────────
        setUndecorated(true);
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        // ──────────────────────────────────────────────────────

        JPanel panelPrincipal = new JPanel(new BorderLayout(10, 10));
        panelPrincipal.setBorder(BorderFactory.createEmptyBorder(20, 30, 20, 30));
        panelPrincipal.setBackground(new Color(0, 0, 0));

        // --- Encabezado ---
        JLabel lblTitulo = new JLabel(" ARCADE DE JUEGOS", SwingConstants.CENTER);
        lblTitulo.setFont(new Font("Arial", Font.BOLD, 22));
        lblTitulo.setForeground(new Color(255, 255, 255));

        JLabel lblBienvenida = new JLabel("¡Bienvenido! Elige un juego para comenzar", SwingConstants.CENTER);
        lblBienvenida.setFont(new Font("Arial", Font.PLAIN, 13));
        lblBienvenida.setForeground(new Color(180, 190, 210));

        JPanel panelHeader = new JPanel(new GridLayout(2, 1, 5, 5));
        panelHeader.setBackground(new Color(0, 0, 0));
        panelHeader.add(lblTitulo);
        panelHeader.add(lblBienvenida);

        // --- Menú de botones ---
        JPanel panelMenu = new JPanel(new GridLayout(6, 1, 8, 8));
        panelMenu.setBackground(new Color(0, 0, 0));

        ArrayList<Juego> juegos = gestor.getJuegos();
        for (int i = 0; i < juegos.size(); i++) {
            final int indice = i;
            Juego j = juegos.get(i);
            JButton btn = crearBoton("  " + j.getNombre(), new Color(49, 50, 68));
            btn.addActionListener(e -> abrirJuego(indice));
            panelMenu.add(btn);
        }

        JButton btnSalir = crearBoton("  Salir", new Color(80, 30, 40));
        btnSalir.addActionListener(e -> confirmarSalida());
        panelMenu.add(btnSalir);

        panelPrincipal.add(panelHeader, BorderLayout.NORTH);
        panelPrincipal.add(panelMenu, BorderLayout.CENTER);
        add(panelPrincipal);
    }

    private void abrirJuego(int indice) {
        String instrucciones = gestor.getInstrucciones(indice);
        int respuesta = JOptionPane.showConfirmDialog(this,
            instrucciones + "\n\n¿Deseas jugar?",
            "Instrucciones",
            JOptionPane.YES_NO_OPTION,
            JOptionPane.INFORMATION_MESSAGE);
        if (respuesta == JOptionPane.YES_OPTION) {
            gestor.iniciarJuego(indice);
        }
    }

    private void confirmarSalida() {
        int respuesta = JOptionPane.showConfirmDialog(this,
            "¿Seguro que quieres salir?", "Salir",
            JOptionPane.YES_NO_OPTION);
        if (respuesta == JOptionPane.YES_OPTION)
            System.exit(0);
    }

    private JButton crearBoton(String texto, Color fondo) {
        JButton btn = new JButton(texto);
        btn.setFont(new Font("Arial", Font.BOLD, 14));
        btn.setBackground(fondo);
        btn.setForeground(Color.WHITE);
        btn.setFocusPainted(false);
        btn.setBorderPainted(false);
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        return btn;
    }

    public Object getPanelNarrativa() {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    public void mostrarNarrativa() {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    public void mostrarExamen() {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    public Object getPanelExamen() {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    public void mostrarFinal(int i, String sergio_Ni_en_un_millón_de_años_hubieras_p) {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }
}
