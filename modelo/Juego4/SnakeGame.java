package modelo.Juego4;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.LinkedList;
import java.util.Random;

public class SnakeGame extends JFrame {

    public SnakeGame() {
        setTitle("🐍 Snake Game");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setResizable(false);

        GamePanel panel = new GamePanel();
        add(panel);
        pack();
        setLocationRelativeTo(null);
        setVisible(true);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(SnakeGame::new);
    }
}

class GamePanel extends JPanel implements ActionListener, KeyListener {

    // --- Constantes ---
    static final int CELL_SIZE = 24;
    static final int COLS = 25;
    static final int ROWS = 25;
    static final int WIDTH  = COLS * CELL_SIZE;
    static final int HEIGHT = ROWS * CELL_SIZE;
    static final int DELAY  = 120; // ms entre ticks

    // Colores
    static final Color COLOR_BG        = new Color(15, 17, 26);
    static final Color COLOR_GRID      = new Color(25, 28, 42);
    static final Color COLOR_HEAD      = new Color(80, 220, 120);
    static final Color COLOR_BODY      = new Color(50, 170, 90);
    static final Color COLOR_BODY2     = new Color(40, 145, 75);
    static final Color COLOR_FOOD      = new Color(255, 80, 90);
    static final Color COLOR_FOOD_GLOW = new Color(255, 80, 90, 60);
    static final Color COLOR_TEXT      = new Color(200, 210, 230);
    static final Color COLOR_SCORE_BOX = new Color(25, 28, 42);

    // --- Estado del juego ---
    enum Direction { UP, DOWN, LEFT, RIGHT }
    enum State     { PLAYING, PAUSED, GAME_OVER, START }

    LinkedList<Point> snake = new LinkedList<>();
    Direction dir     = Direction.RIGHT;
    Direction nextDir = Direction.RIGHT;
    Point food;
    int score = 0;
    int highScore = 0;
    State state = State.START;
    Timer timer;
    Random rng = new Random();

    // Para animación de comida
    int foodPulse = 0;
    boolean pulseUp = true;

    public GamePanel() {
        setPreferredSize(new Dimension(WIDTH, HEIGHT + 50));
        setBackground(COLOR_BG);
        setFocusable(true);
        addKeyListener(this);

        timer = new Timer(DELAY, this);
        initGame();
    }

    void initGame() {
        snake.clear();
        snake.addFirst(new Point(COLS / 2,     ROWS / 2));
        snake.addFirst(new Point(COLS / 2 + 1, ROWS / 2));
        snake.addFirst(new Point(COLS / 2 + 2, ROWS / 2));
        dir = Direction.RIGHT;
        nextDir = Direction.RIGHT;
        score = 0;
        spawnFood();
    }

    void spawnFood() {
        Point p;
        do {
            p = new Point(rng.nextInt(COLS), rng.nextInt(ROWS));
        } while (snake.contains(p));
        food = p;
    }

    // ---- Lógica principal ----
    @Override
    public void actionPerformed(ActionEvent e) {
        if (state != State.PLAYING) return;

        dir = nextDir;

        Point head = snake.getFirst();
        Point newHead = switch (dir) {
            case UP    -> new Point(head.x, head.y - 1);
            case DOWN  -> new Point(head.x, head.y + 1);
            case LEFT  -> new Point(head.x - 1, head.y);
            case RIGHT -> new Point(head.x + 1, head.y);
        };

        // Colisión con paredes
        if (newHead.x < 0 || newHead.x >= COLS || newHead.y < 0 || newHead.y >= ROWS) {
            endGame(); return;
        }
        // Colisión consigo mismo
        if (snake.contains(newHead)) {
            endGame(); return;
        }

        snake.addFirst(newHead);

        if (newHead.equals(food)) {
            score += 10;
            if (score > highScore) highScore = score;
            spawnFood();
        } else {
            snake.removeLast();
        }

        // Animación comida
        if (pulseUp) { foodPulse += 3; if (foodPulse >= 12) pulseUp = false; }
        else         { foodPulse -= 3; if (foodPulse <= 0)  pulseUp = true;  }

        repaint();
    }

    void endGame() {
        state = State.GAME_OVER;
        timer.stop();
        repaint();
    }

    // ---- Dibujado ----
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g;
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        drawGrid(g2);
        drawFood(g2);
        drawSnake(g2);
        drawHUD(g2);

        if (state == State.START)     drawOverlay(g2, "SNAKE", "Presiona ENTER para jugar", "WASD o flechas para moverse");
        if (state == State.PAUSED)    drawOverlay(g2, "PAUSA", "Presiona P para continuar", "");
        if (state == State.GAME_OVER) drawOverlay(g2, "GAME OVER", "Puntuación: " + score, "Presiona ENTER para reiniciar");
    }

    void drawGrid(Graphics2D g) {
        g.setColor(COLOR_GRID);
        for (int x = 0; x <= COLS; x++)
            g.drawLine(x * CELL_SIZE, 0, x * CELL_SIZE, ROWS * CELL_SIZE);
        for (int y = 0; y <= ROWS; y++)
            g.drawLine(0, y * CELL_SIZE, WIDTH, y * CELL_SIZE);
    }

    void drawFood(Graphics2D g) {
        int glow = foodPulse;
        int fx = food.x * CELL_SIZE;
        int fy = food.y * CELL_SIZE;

        // Halo
        g.setColor(COLOR_FOOD_GLOW);
        g.fillOval(fx - glow, fy - glow, CELL_SIZE + glow * 2, CELL_SIZE + glow * 2);

        // Cuerpo
        g.setColor(COLOR_FOOD);
        int pad = 3;
        g.fillRoundRect(fx + pad, fy + pad, CELL_SIZE - pad * 2, CELL_SIZE - pad * 2, 8, 8);

        // Brillo
        g.setColor(new Color(255, 200, 200, 160));
        g.fillOval(fx + 6, fy + 5, 5, 4);
    }

    void drawSnake(Graphics2D g) {
        int size = snake.size();
        for (int i = size - 1; i >= 0; i--) {
            Point p = snake.get(i);
            int px = p.x * CELL_SIZE;
            int py = p.y * CELL_SIZE;
            int pad = 2;

            if (i == 0) {
                // Cabeza
                g.setColor(COLOR_HEAD);
                g.fillRoundRect(px + 1, py + 1, CELL_SIZE - 2, CELL_SIZE - 2, 10, 10);

                // Ojos
                g.setColor(COLOR_BG);
                Point next = size > 1 ? snake.get(1) : null;
                drawEyes(g, p, next, px, py);

            } else {
                // Cuerpo con efecto de brillo alternado
                g.setColor(i % 2 == 0 ? COLOR_BODY : COLOR_BODY2);
                g.fillRoundRect(px + pad, py + pad, CELL_SIZE - pad * 2, CELL_SIZE - pad * 2, 7, 7);
            }
        }
    }

    void drawEyes(Graphics2D g, Point head, Point prev, int px, int py) {
        // Posición de ojos según dirección
        int[][] eyes;
        switch (dir) {
            case RIGHT -> eyes = new int[][]{{px+16, py+5}, {px+16, py+14}};
            case LEFT  -> eyes = new int[][]{{px+5,  py+5}, {px+5,  py+14}};
            case UP    -> eyes = new int[][]{{px+5,  py+5}, {px+14, py+5}};
            default    -> eyes = new int[][]{{px+5,  py+14},{px+14, py+14}};
        }
        for (int[] eye : eyes) {
            g.fillOval(eye[0], eye[1], 4, 4);
        }
    }

    void drawHUD(Graphics2D g) {
        int hudY = ROWS * CELL_SIZE;

        // Fondo HUD
        g.setColor(COLOR_SCORE_BOX);
        g.fillRect(0, hudY, WIDTH, 50);
        g.setColor(new Color(40, 45, 65));
        g.drawLine(0, hudY, WIDTH, hudY);

        // Puntuación
        g.setColor(COLOR_TEXT);
        g.setFont(new Font("Monospaced", Font.BOLD, 14));
        g.drawString("SCORE: " + score, 18, hudY + 20);
        g.setColor(new Color(255, 200, 80));
        g.drawString("BEST:  " + highScore, 18, hudY + 38);

        // Tamaño serpiente
        g.setColor(new Color(120, 140, 180));
        g.setFont(new Font("Monospaced", Font.PLAIN, 12));
        String len = "LONGITUD: " + snake.size();
        int tw = g.getFontMetrics().stringWidth(len);
        g.drawString(len, WIDTH - tw - 15, hudY + 20);

        // Instrucción pausa
        g.setColor(new Color(80, 95, 130));
        String hint = state == State.PLAYING ? "[P] Pausa" : "";
        int hw = g.getFontMetrics().stringWidth(hint);
        g.drawString(hint, WIDTH - hw - 15, hudY + 38);
    }

    void drawOverlay(Graphics2D g, String title, String sub1, String sub2) {
        // Fondo semitransparente
        g.setColor(new Color(10, 12, 20, 200));
        g.fillRect(0, 0, WIDTH, ROWS * CELL_SIZE);

        // Caja central
        int bw = 320, bh = sub2.isEmpty() ? 110 : 140;
        int bx = (WIDTH - bw) / 2, by = (ROWS * CELL_SIZE - bh) / 2;
        g.setColor(new Color(30, 34, 52));
        g.fillRoundRect(bx, by, bw, bh, 18, 18);
        g.setColor(new Color(60, 80, 130));
        g.setStroke(new BasicStroke(2));
        g.drawRoundRect(bx, by, bw, bh, 18, 18);

        // Título
        g.setFont(new Font("Monospaced", Font.BOLD, 28));
        g.setColor(COLOR_HEAD);
        FontMetrics fm = g.getFontMetrics();
        g.drawString(title, bx + (bw - fm.stringWidth(title)) / 2, by + 45);

        // Subtítulo
        g.setFont(new Font("Monospaced", Font.PLAIN, 13));
        g.setColor(COLOR_TEXT);
        fm = g.getFontMetrics();
        g.drawString(sub1, bx + (bw - fm.stringWidth(sub1)) / 2, by + 75);

        if (!sub2.isEmpty()) {
            g.setColor(new Color(120, 140, 180));
            g.drawString(sub2, bx + (bw - fm.stringWidth(sub2)) / 2, by + 100);
        }
    }

    // ---- Teclado ----
    @Override
    public void keyPressed(KeyEvent e) {
        int k = e.getKeyCode();

        if (state == State.START || state == State.GAME_OVER) {
            if (k == KeyEvent.VK_ENTER) {
                initGame();
                state = State.PLAYING;
                timer.start();
                repaint();
            }
            return;
        }

        if (k == KeyEvent.VK_P) {
            if (state == State.PLAYING) { state = State.PAUSED;  timer.stop(); }
            else                        { state = State.PLAYING; timer.start(); }
            repaint();
            return;
        }

        if (state != State.PLAYING) return;

        switch (k) {
            case KeyEvent.VK_UP,    KeyEvent.VK_W -> { if (dir != Direction.DOWN)  nextDir = Direction.UP;    }
            case KeyEvent.VK_DOWN,  KeyEvent.VK_S -> { if (dir != Direction.UP)    nextDir = Direction.DOWN;  }
            case KeyEvent.VK_LEFT,  KeyEvent.VK_A -> { if (dir != Direction.RIGHT) nextDir = Direction.LEFT;  }
            case KeyEvent.VK_RIGHT, KeyEvent.VK_D -> { if (dir != Direction.LEFT)  nextDir = Direction.RIGHT; }
        }
    }

    @Override public void keyReleased(KeyEvent e) {}
    @Override public void keyTyped(KeyEvent e) {}
}
