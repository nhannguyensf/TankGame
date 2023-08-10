package tankrotationexample.game;

import java.awt.*;
import java.awt.image.BufferedImage;

public class Wall extends GameObject {
    float x, y;
    BufferedImage img;
    private Rectangle hitBox;

    public Wall(float x, float y, BufferedImage img) {
        this.x = x;
        this.y = y;
        this.img = img;
        this.hitBox = new Rectangle((int) x, (int) y, this.img.getWidth(), this.img.getHeight());

    }

    public Rectangle getHitBox() {
        return this.hitBox.getBounds();
    }

    @Override
    public boolean isActive() {
        return true;
    }

    @Override
    public void collides(GameObject obj2, GameWorld gameWorld) {

    }

    public void drawImage(Graphics buffer) {
        buffer.drawImage(this.img, (int) x, (int) y, null);
    }

    @Override
    public String toString() {
        return "Wall at: x=" + x + ", y=" + y;
    }
}

