import java.awt.*;

public class Pipe extends GameObject {
    private double speed;
    private boolean passed = false;

    public Pipe(double x, double y, int width, int height, double speed) {
        super(x, y, width, height);
        this.speed = speed;
    }

    @Override
    public void update() {
        x -= speed;
    }

    public void setSpeed(double speed) {
        this.speed = speed;
    }

    public boolean isOffScreen() {
        return x + width < 0;
    }

    public boolean isPassed() {
        return passed;
    }

    public void setPassed(boolean passed) {
        this.passed = passed;
    }

    @Override
    public void draw(Graphics2D g2d) {
        g2d.setPaint(new Color(30, 30, 30));
        g2d.fillRoundRect((int)x, (int)y, width, height, 15, 15);

        g2d.setStroke(new BasicStroke(4));
        g2d.setColor(new Color(0, 255, 200));
        g2d.drawRoundRect((int)x, (int)y, width, height, 15, 15);
    }
}