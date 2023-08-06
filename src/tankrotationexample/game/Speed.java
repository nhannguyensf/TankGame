package tankrotationexample.game;

import java.awt.*;
import java.awt.image.BufferedImage;

public class Speed extends GameObject implements PowerUp {
    private static final int speedBoost = 3;
    float x, y;
    BufferedImage img;
    private Rectangle hitBox;
    private boolean isActive;

    public Speed(float x, float y, BufferedImage img) {
        this.x = x;
        this.y = y;
        this.img = img;
        this.hitBox = new Rectangle((int) x, (int) y, this.img.getWidth(), this.img.getHeight());
        this.isActive = true;
    }

    public static int getSpeedBoost() {
        return speedBoost;
    }

    public Rectangle getHitBox() {
        return this.hitBox.getBounds();
    }

    @Override
    public void applyPowerUp(Tank tank) {
        tank.increaseSpeed();
        this.isActive = false;
        System.out.println("Tank is speeding up");
    }

    @Override
    public boolean isActive() {
        return this.isActive;
    }

    @Override
    public void collides(GameObject obj2) {

    }

    public void drawImage(Graphics buffer) {
        buffer.drawImage(this.img, (int) x, (int) y, null);
    }

    @Override
    public String toString() {
        return "Speed item at: x=" + x + ", y=" + y;
    }
}
