import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Random;

public class GamePanel extends JPanel implements Runnable, KeyListener {
    private Thread gameThread;
    private boolean running = false;
    private boolean paused = false;
    private boolean gameStarted = false;

    private Pigeon pigeon;
    private List<Pipe> pipes;

    private int score = 0, level = 1;
    private double gameSpeed = 7.0;
    private int gapSize = 220;
    private float screenShake = 0;

    private GarageGame parent;
    private Random rand = new Random();


    private GradientPaint backgroundGradient;
    private int lastWidth = 0;
    private int lastLevel = 1;

    public GamePanel(GarageGame parent) {
        this.parent = parent;
        setFocusable(true);
        addKeyListener(this);
    }

    public void initGame() {
        pigeon = new Pigeon(150, getHeight() / 2.0);
        pipes = new ArrayList<>();
        score = 0;
        level = 1;
        gameSpeed = 7.0;
        gapSize = 220;
        gameStarted = false;
        paused = false;
        running = true;

        spawnPipe(getWidth() > 0 ? getWidth() : 1200);

        if (gameThread == null || !gameThread.isAlive()) {
            gameThread = new Thread(this);
            gameThread.start();
        }
    }

    private void spawnPipe(int xPos) {
        int gapY = 100 + rand.nextInt(Math.max(1, getHeight() - gapSize - 200));
        pipes.add(new Pipe(xPos, 0, 85, gapY, gameSpeed));
        pipes.add(new Pipe(xPos, gapY + gapSize, 85, getHeight(), gameSpeed));
    }

    @Override
    public void run() {

        final double TARGET_FPS = 60.0;
        final double OPTIMAL_TIME = 1000000000 / TARGET_FPS;
        long lastLoopTime = System.nanoTime();

        while (running) {
            long now = System.nanoTime();
            long updateLength = now - lastLoopTime;
            lastLoopTime = now;

            if (gameStarted && !paused) {
                updateGame();
            }
            if (screenShake > 0) screenShake -= 0.5f;

            repaint();


            long sleepTime = (long) ((lastLoopTime - System.nanoTime() + OPTIMAL_TIME) / 1000000);

            if (sleepTime > 0) {
                try {
                    Thread.sleep(sleepTime);
                } catch (InterruptedException e) {}
            }
        }
    }

    private void updateGame() {
        pigeon.update();

        Iterator<Pipe> iterator = pipes.iterator();

        while (iterator.hasNext()) {
            Pipe pipe = iterator.next();
            pipe.setSpeed(gameSpeed);
            pipe.update();

            if (pigeon.getBounds().intersects(pipe.getBounds())) {
                triggerGameOver();
                return;
            }

            if (!pipe.isPassed() && pipe.x + pipe.width < pigeon.x) {
                pipe.setPassed(true);
                score += 5;

                if (score % 100 == 0) {
                    level++;
                    gameSpeed += 0.8;
                    gapSize = Math.max(150, 230 - (level * 8));
                }
            }

            if (pipe.isOffScreen()) {
                iterator.remove();
            }
        }

        if (!pipes.isEmpty()) {
            Pipe lastPipe = pipes.get(pipes.size() - 1);
            if (lastPipe.x < getWidth() - 400) {
                spawnPipe(getWidth() + 100);
            }
        }

        if (pigeon.y > getHeight() || pigeon.y < 0) {
            triggerGameOver();
        }
    }

    private void triggerGameOver() {
        screenShake = 15;
        running = false;

        new Thread(() -> {
            try { Thread.sleep(1000); } catch (InterruptedException e) {}
            SwingUtilities.invokeLater(() -> {
                JOptionPane.showMessageDialog(this, "המשחק נגמר! הניקוד שלך: " + score);
                parent.returnToMenu();
            });
        }).start();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g;


        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2d.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_SPEED);

        if (screenShake > 0) {
            g2d.translate(rand.nextInt((int)screenShake) - screenShake/2, rand.nextInt((int)screenShake) - screenShake/2);
        }

        drawVibrantBackground(g2d);

        if (pipes != null) {
            for (Pipe pipe : pipes) {
                pipe.draw(g2d);
            }
        }

        if (pigeon != null) {
            pigeon.draw(g2d);
        }

        g2d.setColor(new Color(0, 0, 0, 150));
        g2d.fillRoundRect(20, 20, 180, 80, 20, 20);
        g2d.setColor(Color.WHITE);
        g2d.setFont(new Font("Arial", Font.BOLD, 20));
        g2d.drawString("SCORE: " + score, 40, 50);
        g2d.drawString("LEVEL: " + level, 40, 80);

        if (!gameStarted) {
            g2d.setFont(new Font("Arial", Font.BOLD, 50));
            g2d.drawString("READY? PRESS SPACE", getWidth()/2 - 250, getHeight()/2);
        } else if (paused) {
            g2d.setColor(new Color(0, 0, 0, 150));
            g2d.fillRect(0, 0, getWidth(), getHeight());
            g2d.setColor(Color.YELLOW);
            g2d.setFont(new Font("Arial", Font.BOLD, 70));
            g2d.drawString("PAUSED", getWidth()/2 - 120, getHeight()/2);
        }


        Toolkit.getDefaultToolkit().sync();
    }

    private void drawVibrantBackground(Graphics2D g2d) {
        int currentWidth = getWidth();


        if (backgroundGradient == null || lastWidth != currentWidth || lastLevel != level) {
            Color colorA = (level % 2 == 0) ? new Color(20, 10, 40) : new Color(0, 50, 100);
            Color colorB = (level % 2 == 0) ? new Color(80, 20, 80) : new Color(0, 150, 200);
            backgroundGradient = new GradientPaint(0, 0, colorA, 0, getHeight(), colorB);
            lastWidth = currentWidth;
            lastLevel = level;
        }

        g2d.setPaint(backgroundGradient);
        g2d.fillRect(0, 0, getWidth(), getHeight());

        g2d.setColor(new Color(255, 255, 255, 30));
        for(int i = 0; i < 15; i++) {
            g2d.fillOval((i * 150 + (score * -2)) % (getWidth() + 200), 100 + (i * 40), 5, 5);
        }
    }

    @Override
    public void keyPressed(KeyEvent e) {
        if (e.getKeyCode() == KeyEvent.VK_SPACE || e.getKeyCode() == KeyEvent.VK_UP) {
            if (!gameStarted) {
                gameStarted = true;
                pipes.clear();
                spawnPipe(getWidth() + 100);
            }
            if (!paused) {
                pigeon.jump();
            }
        }

        if (e.getKeyCode() == KeyEvent.VK_P) {
            if (gameStarted && running) {
                paused = !paused;
            }
        }
    }

    @Override public void keyReleased(KeyEvent e) {}
    @Override public void keyTyped(KeyEvent e) {}
}