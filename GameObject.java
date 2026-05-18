import java.awt.*;

public abstract class GameObject {
    protected double x, y;
    protected int width, height;

    public GameObject(double x, double y, int width, int height) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
    }

    public abstract void update();
    public abstract void draw(Graphics2D g2d);

    public Rectangle getBounds() {
        return new Rectangle((int)x, (int)y, width, height);
    }
}