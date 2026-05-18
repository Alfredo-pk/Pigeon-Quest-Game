import java.awt.*;

public class Pigeon extends GameObject {
    private double velocity = 0;
    private final double gravity = 0.52;

    public Pigeon(double x, double y) {
        super(x, y, 40, 28);
    }

    @Override
    public void update() {
        velocity += gravity;
        y += velocity;
    }

    public void jump() {
        velocity = -9.2;
    }

    @Override
    public void draw(Graphics2D g2d) {
        g2d.setColor(Color.WHITE);
        g2d.fillOval((int)x, (int)y, width, height);

        int wingMovement = (int)(Math.sin(System.currentTimeMillis() / 80.0) * 12);
        g2d.setColor(new Color(230, 230, 230));
        g2d.fillOval((int)x + 5, (int)y + 5 + wingMovement, 20, 10);

        g2d.setColor(Color.WHITE);
        g2d.fillOval((int)x + 28, (int)y - 5, 18, 18);
        g2d.setColor(Color.BLACK);
        g2d.fillOval((int)x + 36, (int)y + 2, 4, 4);

        g2d.setColor(new Color(200, 200, 200));
        g2d.fillRect((int)x - 10, (int)y + 10, 15, 8);
    }
}