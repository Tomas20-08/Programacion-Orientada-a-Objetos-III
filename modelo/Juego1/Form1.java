package modelo.Juego1;

import modelo.Juego1.ClassPiano;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.*;

public class Form1 extends JFrame {

    private ClassPiano Mipiano;
    private Map<Integer, String> mapaTeclas;
    private Set<Integer> teclasPresionadas;

    private JPanel PanelPiano;
    private JPanel PanelSustain1;
    private JLabel Title1;
    private JLabel Title2;
    private JLabel Image1;

    public Form1() {
        teclasPresionadas = new HashSet<>();
        mapaTeclas        = new HashMap<>();
        InitializeComponent();
        InicializarMapaTeclas();
        Form1_Load();
    }

    private void InitializeComponent() {
        setTitle("CodePlay Piano");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE); // DISPOSE, no EXIT
        setLayout(null);
        getContentPane().setBackground(Color.BLACK);
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        setUndecorated(true);
        setFocusable(true);

        PanelPiano = new JPanel() {
            @Override public boolean isOptimizedDrawingEnabled() { return false; }
        };
        PanelPiano.setLayout(null);
        PanelPiano.setBackground(new Color(240, 240, 240));
        PanelPiano.setBounds(585, 390, 750, 300);

        PanelSustain1 = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                PanelSustain1_Paint(g);
            }
        };
        PanelSustain1.setBackground(Color.BLACK);
        PanelSustain1.setBounds(1620, 780, 100, 100);

        Title1 = new JLabel("CodePlay Piano");
        Title1.setFont(new Font("Times New Roman", Font.BOLD | Font.ITALIC, 72));
        Title1.setForeground(new Color(128, 128, 128));
        Title1.setBounds(618, 133, 684, 108);

        Title2 = new JLabel("<html>Sustain<br>Pedal</html>");
        Title2.setFont(new Font("Times New Roman", Font.BOLD | Font.ITALIC, 36));
        Title2.setForeground(new Color(128, 128, 128));
        Title2.setBounds(1424, 776, 200, 130);

        Image1 = new JLabel();
        Image1.setBounds(152, 678, 302, 302);
        java.net.URL imgUrl = getClass().getResource("/resources/Logo.png");
        if (imgUrl != null) {
            ImageIcon icon  = new ImageIcon(imgUrl);
            Image    scaled = icon.getImage().getScaledInstance(302, 302, Image.SCALE_SMOOTH);
            Image1.setIcon(new ImageIcon(scaled));
        }

        add(Image1);
        add(Title2);
        add(Title1);
        add(PanelSustain1);
        add(PanelPiano);

        addKeyListener(new KeyAdapter() {
            @Override public void keyPressed(KeyEvent e)  { OnKeyDown(e); }
            @Override public void keyReleased(KeyEvent e) { OnKeyUp(e); }
        });

        // NOTA: el ESC ya NO está aquí — lo maneja Piano.java para volver al menú
    }

    private void Form1_Load() {
        String[] Negras  = { "C#4","D#4","_","F#4","G#4","A#4","_",
                             "C#5","D#5","_","F#5","G#5","A#5" };
        String[] Blancas = { "C4","D4","E4","F4","G4","A4","B4",
                             "C5","D5","E5","F5","G5","A5","B5","C6" };

        Mipiano = new ClassPiano();
        Mipiano.CrearTeclasBlancas(Blancas, PanelPiano);
        Mipiano.CrearTeclasNegras(Negras, PanelPiano);

        Component[] componentes = PanelPiano.getComponents();
        int totalComponentes = componentes.length;
        int indiceFrente = 0;
        int indiceFondo  = totalComponentes - 1;

        for (Component c : PanelPiano.getComponents()) {
            if (c instanceof ClassPiano.TeclaPiano && c.getForeground().equals(Color.WHITE))
                PanelPiano.setComponentZOrder(c, indiceFrente++);
        }
        for (Component c : PanelPiano.getComponents()) {
            if (c instanceof ClassPiano.TeclaPiano && c.getForeground().equals(Color.BLACK))
                PanelPiano.setComponentZOrder(c, indiceFondo--);
        }
        PanelPiano.revalidate();
        PanelPiano.repaint();
    }

    private void InicializarMapaTeclas() {
        mapaTeclas.put(KeyEvent.VK_Z, "C4");
        mapaTeclas.put(KeyEvent.VK_S, "C#4");
        mapaTeclas.put(KeyEvent.VK_X, "D4");
        mapaTeclas.put(KeyEvent.VK_D, "D#4");
        mapaTeclas.put(KeyEvent.VK_C, "E4");
        mapaTeclas.put(KeyEvent.VK_V, "F4");
        mapaTeclas.put(KeyEvent.VK_G, "F#4");
        mapaTeclas.put(KeyEvent.VK_B, "G4");
        mapaTeclas.put(KeyEvent.VK_H, "G#4");
        mapaTeclas.put(KeyEvent.VK_N, "A4");
        mapaTeclas.put(KeyEvent.VK_J, "A#4");
        mapaTeclas.put(KeyEvent.VK_M, "B4");
        mapaTeclas.put(KeyEvent.VK_Q, "C5");
        mapaTeclas.put(KeyEvent.VK_2, "C#5");
        mapaTeclas.put(KeyEvent.VK_W, "D5");
        mapaTeclas.put(KeyEvent.VK_3, "D#5");
        mapaTeclas.put(KeyEvent.VK_E, "E5");
        mapaTeclas.put(KeyEvent.VK_R, "F5");
        mapaTeclas.put(KeyEvent.VK_5, "F#5");
        mapaTeclas.put(KeyEvent.VK_T, "G5");
        mapaTeclas.put(KeyEvent.VK_6, "G#5");
        mapaTeclas.put(KeyEvent.VK_Y, "A5");
        mapaTeclas.put(KeyEvent.VK_7, "A#5");
        mapaTeclas.put(KeyEvent.VK_U, "B5");
        mapaTeclas.put(KeyEvent.VK_I, "C6");
    }

    protected void OnKeyDown(KeyEvent e) {
        int keyCode = e.getKeyCode();
        if (!teclasPresionadas.contains(keyCode)) {
            teclasPresionadas.add(keyCode);
            if (mapaTeclas.containsKey(keyCode)) {
                String nota = mapaTeclas.get(keyCode);
                ClassPiano.TeclaPiano tecla = BuscarTeclaPorNombre(nota);
                if (tecla != null) {
                    tecla.setPresionada(true);
                    Mipiano.ReproducirSonido(nota);
                }
            }
        }
        if (keyCode == KeyEvent.VK_CAPS_LOCK) PanelSustain1.repaint();
    }

    protected void OnKeyUp(KeyEvent e) {
        int keyCode = e.getKeyCode();
        teclasPresionadas.remove(keyCode);
        if (mapaTeclas.containsKey(keyCode)) {
            String nota = mapaTeclas.get(keyCode);
            ClassPiano.TeclaPiano tecla = BuscarTeclaPorNombre(nota);
            if (tecla != null) tecla.setPresionada(false);
        }
        if (keyCode == KeyEvent.VK_CAPS_LOCK) PanelSustain1.repaint();
    }

    private ClassPiano.TeclaPiano BuscarTeclaPorNombre(String nombre) {
        for (Component c : PanelPiano.getComponents()) {
            if (c instanceof ClassPiano.TeclaPiano && nombre.equals(c.getName()))
                return (ClassPiano.TeclaPiano) c;
        }
        return null;
    }

    private void PanelSustain1_Paint(Graphics g) {
        boolean capsLockOn = false;
        try {
            capsLockOn = Toolkit.getDefaultToolkit().getLockingKeyState(KeyEvent.VK_CAPS_LOCK);
        } catch (Exception ex) {}

        Graphics2D g2 = (Graphics2D) g;
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2.setColor(capsLockOn ? Color.RED : Color.DARK_GRAY);

        int w = PanelSustain1.getWidth(), h = PanelSustain1.getHeight();
        int d = Math.min(w, h);
        g2.fillOval((w - d) / 2, (h - d) / 2, d, d);
    }
}
