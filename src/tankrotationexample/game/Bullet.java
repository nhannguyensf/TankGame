package tankrotationexample.game;

import tankrotationexample.Resources.ResourceManager;

import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;

public class Bullet extends GameObject {
    private int bulletDamage = 20;
    private boolean isActive;
    private float x;
    private float y;
    private float vx;
    private float vy;
    private float angle;
    private float R;
    private BufferedImage img;
    private Rectangle hitBox;
    private GameWorld gameWorld;

    Bullet(float x, float y, BufferedImage img, float angle, float bulletSpeed, GameWorld gameWorld) {
        this.x = x;
        this.y = y;
        this.vx = 0;
        this.vy = 0;
        this.img = img;
        this.angle = angle;
        this.R = bulletSpeed;
        this.isActive = true;
        this.hitBox = new Rectangle((int) x, (int) y, this.img.getWidth(), this.img.getHeight());
        this.gameWorld = gameWorld;
    }

    public Rectangle getHitBox() {
        return this.hitBox.getBounds();

    }

    @Override
    public boolean isActive() {
        return this.isActive;
    }

    void update() {
        if (this.isActive) {
            vx = Math.round(R * Math.cos(Math.toRadians(angle)));
            vy = Math.round(R * Math.sin(Math.toRadians(angle)));
            x += vx;
            y += vy;
            this.hitBox.setLocation((int) x, (int) y);
        }
    }

    @Override
    public String toString() {
        return "A bullet";
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

    private void handleTankCollision(Tank playerTank) {
        this.gameWorld.addAnimations(new Animation(playerTank.getX(), playerTank.getY() - 5, ResourceManager.getAnimation("rockethit")));
        ResourceManager.getSound("explosion").playSound();
        this.isActive = false;
        playerTank.health -= this.bulletDamage;
        System.out.println("Tank health: " + playerTank.health);
        if (playerTank.health <= 0) {
            playerTank.isDead = true;
        }
    }

    private void handleBreakableWallCollision(BreakableWall with) {
        this.gameWorld.addAnimations(new Animation(with.getX(), with.getY() - 5, ResourceManager.getAnimation("bullethit")));
        ResourceManager.getSound("explosion").playSound();
        this.isActive = false;
        with.setActive(false);
    }

    private void handleWallCollision() {
        ResourceManager.getSound("explosion").playSound();
        this.gameWorld.addAnimations(new Animation(x, y, ResourceManager.getAnimation("puffsmoke")));
        this.isActive = false;
    }

    public void drawImage(Graphics g) {
        AffineTransform rotation = AffineTransform.getTranslateInstance(x, y);
        rotation.rotate(Math.toRadians(angle), this.img.getWidth() / 2.0, this.img.getHeight() / 2.0);
        rotation.scale(2, 2);
        Graphics2D g2d = (Graphics2D) g;
        if (this.isActive) {
            g2d.drawImage(this.img, rotation, null);
        }
    }
}
