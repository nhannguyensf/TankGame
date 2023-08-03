package tankrotationexample.game;

import tankrotationexample.GameConstants;

import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;

public class Bullet extends GameObject {
    private int bulletDamage = 20;
    private boolean active;
    private float x;
    private float y;
    private float vx;
    private float vy;
    private float angle;
    private float R = 6;
    private BufferedImage img;
    private Rectangle hitBox;

    Bullet(float x, float y, BufferedImage img, float angle) {
        this.x = x;
        this.y = y;
        this.vx = 0;
        this.vy = 0;
        this.img = img;
        this.angle = angle;
        this.active = true;
        this.hitBox = new Rectangle((int) x, (int) y, this.img.getWidth(), this.img.getHeight());
    }

    public Rectangle getHitBox() {
        return this.hitBox.getBounds();
    }

    public float getX() {
        return this.x;
    }

    public float getY() {
        return this.y;
    }

    void update() {
        vx = Math.round(R * Math.cos(Math.toRadians(angle)));
        vy = Math.round(R * Math.sin(Math.toRadians(angle)));
        x += vx;
        y += vy;
        checkBorder();
        this.hitBox.setLocation((int) x, (int) y);
    }

    private void checkBorder() {
        if (x < 30) {
            x = 30;
        }
        if (x >= GameConstants.GAME_WORLD_WIDTH - 80) {
            x = GameConstants.GAME_WORLD_WIDTH - 80;
        }
        if (y < 40) {
            y = 40;
        }
        if (y >= GameConstants.GAME_WORLD_HEIGHT - 80) {
            y = GameConstants.GAME_WORLD_HEIGHT - 80;
        }
    }

    @Override
    public String toString() {
        return "x=" + x + ", y=" + y + ", angle=" + angle;
    }

    @Override
    public void collides(GameObject with) {
        if (with instanceof Wall || with instanceof BreakableWall) {
            //disappear bullet
            handleWallCollision();
            System.out.println("Bullet hits a wall");
            if (with instanceof BreakableWall) {
                handleBreakableWallCollision((BreakableWall) with);
                System.out.println("Bullet hits a BreakableWall");
            }
        } else if (with instanceof Tank) {
            //lose health
            handleTankCollision((Tank) with);
            System.out.println("Tank being hit!!!");
        }
    }

    private void handleTankCollision(Tank with) {
        with.health -= this.bulletDamage;

        if (with.health < 0) {
            with.health = 0;
            System.out.println("Tank health: " + with.health);
            // Handle the situation when the tank's health drops to zero, such as destroying the tank
        }
    }

    private void handleBreakableWallCollision(BreakableWall with) {
        this.active = false;
    }

    private void handleWallCollision() {
        this.active = false;
    }

    public void drawImage(Graphics g) {
        AffineTransform rotation = AffineTransform.getTranslateInstance(x, y);
        rotation.rotate(Math.toRadians(angle), this.img.getWidth() / 2.0, this.img.getHeight() / 2.0);
        rotation.scale(2, 2);
        Graphics2D g2d = (Graphics2D) g;
        g2d.drawImage(this.img, rotation, null);
    }

    public int getDamage() {
        return this.bulletDamage;
    }

    public boolean isActive() {
        return this.active;
    }
}
