package tankrotationexample.game;

import java.awt.*;
import java.awt.image.BufferedImage;

public class Health extends GameObject implements PowerUp {
    private static final int healthBoost = 20;
    float x, y;
    BufferedImage img;
    private Rectangle hitBox;
    private boolean isActive;

    public Health(float x, float y, BufferedImage img) {
        this.x = x;
        this.y = y;
        this.img = img;
        this.hitBox = new Rectangle((int) x, (int) y, this.img.getWidth(), this.img.getHeight());
        this.isActive = true;
    }

    public static int getHealthBoost() {
        return healthBoost;
    }

    public Rectangle getHitBox() {
        return this.hitBox.getBounds();
    }

    @Override
    public void collides(GameObject obj2) {

    }

    public void drawImage(Graphics buffer) {
        buffer.drawImage(this.img, (int) x, (int) y, null);
    }

    @Override
    public String toString() {
        return "Health item at: x=" + x + ", y=" + y;
    }

    @Override
    public void applyPowerUp(Tank tank) {
        System.out.println("x= " + this.x + " ,y= " + this.y);
        System.out.println("Tank is healing");
        tank.increaseHealth();
        this.isActive = false;
    }

    @Override
    public boolean isActive() {
        return this.isActive;
    }
}
