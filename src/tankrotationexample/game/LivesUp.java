package tankrotationexample.game;

import java.awt.*;
import java.awt.image.BufferedImage;

public class LivesUp extends GameObject implements PowerUp {
    private static final int lives = 1;
    float x, y;
    BufferedImage img;
    private Rectangle hitBox;
    private boolean isActive;

    public LivesUp(float x, float y, BufferedImage img) {
        this.x = x;
        this.y = y;
        this.img = img;
        this.hitBox = new Rectangle((int) x, (int) y, this.img.getWidth(), this.img.getHeight());
        this.isActive = true;
    }

    public static int getLivesBoost() {
        return lives;
    }

    public Rectangle getHitBox() {
        return this.hitBox.getBounds();
    }

    @Override
    public void applyPowerUp(Tank tank) {
        System.out.println("x= " + this.x + " ,y= " + this.y);
        System.out.println("Tank has more life");
        tank.increaseLife();
        this.isActive = false;
    }

    @Override
    public boolean isActive() {
        return this.isActive;
    }

    @Override
    public void collides(GameObject obj2, GameWorld gameWorld) {
    }

    public void drawImage(Graphics buffer) {
        buffer.drawImage(this.img, (int) x, (int) y, null);
    }

    @Override
    public String toString() {
        return "Shield item at: x=" + x + ", y=" + y;
    }
}
