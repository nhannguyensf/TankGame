package tankrotationexample.game;

import tankrotationexample.GameConstants;
import tankrotationexample.Resources.ResourceManager;
//import tankrotationexample.Resources.ResourcePool;

import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;

public class Tank extends GameObject {

//    static ResourcePool<Bullet> bPool;
//
//    static {
//        bPool = new ResourcePool<>("bullet", 300);
//        bPool.fillPool(Bullet.class, 300);
//    }

    List<Bullet> ammo = new ArrayList<>();
    long timeSinceLastShot = 0L;
    long cooldown = 3000;

    private float x;
    private float y;
    private float screen_x, screen_y;
    private float vx;
    private float vy;
    private float angle;
    private float R = 2;
    private float ROTATIONSPEED = 2.0f;
    private BufferedImage img;
    private boolean UpPressed;
    private boolean DownPressed;
    private boolean RightPressed;
    private boolean LeftPressed;
    private boolean shootPressed;
    private Rectangle hitBox;

    Tank(float x, float y, float vx, float vy, float angle, BufferedImage img) {
        this.x = x;
        this.y = y;
        this.vx = vx;
        this.vy = vy;
        this.img = img;
        this.angle = angle;
        this.hitBox = new Rectangle((int) x, (int) y, this.img.getWidth(), this.img.getHeight());
    }

    // A helper function to clamp a value between a minimum and maximum
    static float clamp(float value, float min, float max) {
        return Math.max(min, Math.min(max, value));
    }

    public Rectangle getHitBox() {
        return this.hitBox.getBounds();
    }

    public float getX() {
        return this.x;
    }

    void setX(float x) {
        this.x = x;
    }

    public float getY() {
        return this.y;
    }

    void setY(float y) {
        this.y = y;
    }

    public float getScreen_x() {
        return screen_x;
    }

    public float getScreen_y() {
        return screen_y;
    }

    public void collides(GameObject with) {
        if (with instanceof Bullet) {
            //lose life
        } else if (with instanceof Wall) {
            //stop
        } else if (with instanceof PowerUp) {
            ((PowerUp) with).applyPowerUp(this);
        }
    }

    void toggleUpPressed() {
        this.UpPressed = true;
    }

    void toggleDownPressed() {
        this.DownPressed = true;
    }

    void toggleRightPressed() {
        this.RightPressed = true;
    }

    void toggleLeftPressed() {
        this.LeftPressed = true;
    }

    void unToggleUpPressed() {
        this.UpPressed = false;
    }

    void unToggleDownPressed() {
        this.DownPressed = false;
    }

    void unToggleRightPressed() {
        this.RightPressed = false;
    }

    void unToggleLeftPressed() {
        this.LeftPressed = false;
    }

    public void toggleShootPressed() {
        this.shootPressed = true;
    }

    public void unToggleShootPressed() {
        this.shootPressed = false;
    }

    void update() {
        if (this.UpPressed) {
            this.moveForwards();
        }

        if (this.DownPressed) {
            this.moveBackwards();
        }

        if (this.LeftPressed) {
            this.rotateLeft();
        }

        if (this.RightPressed) {
            this.rotateRight();
        }
        if (this.shootPressed && ((this.timeSinceLastShot + this.cooldown) < System.currentTimeMillis())) {
            this.timeSinceLastShot = System.currentTimeMillis();

            // Define the offset to keep the ammo further from the tank
            final float BULLET_OFFSET = 10.0f;
            // Calculate the offset for the x and y coordinates based on the angle
            float bulletX = x + ((float) img.getWidth() / 2 - BULLET_OFFSET / 2) + (float) (Math.cos(Math.toRadians(angle)) * ((img.getWidth() / 2) + BULLET_OFFSET));
            float bulletY = y + ((float) img.getHeight() / 2 - BULLET_OFFSET / 2) + (float) (Math.sin(Math.toRadians(angle)) * ((img.getWidth() / 2) + BULLET_OFFSET));
            this.ammo.add(new Bullet(bulletX, bulletY, ResourceManager.getSprite("bullet"), angle));
        }
        this.ammo.forEach(Bullet::update);
        this.hitBox.setLocation((int) x, (int) y);
    }

    private void rotateLeft() {
        this.angle -= this.ROTATIONSPEED;
    }

    private void rotateRight() {
        this.angle += this.ROTATIONSPEED;
    }

    private void moveBackwards() {
        vx = Math.round(R * Math.cos(Math.toRadians(angle)));
        vy = Math.round(R * Math.sin(Math.toRadians(angle)));
        x -= vx;
        y -= vy;
        checkBorder();
        centerScreen();
    }

    private void moveForwards() {
        vx = Math.round(R * Math.cos(Math.toRadians(angle)));
        vy = Math.round(R * Math.sin(Math.toRadians(angle)));
        x += vx;
        y += vy;
        checkBorder();
        centerScreen();
    }

    private void centerScreen() {
        // Clamp the tank's x and y to remain within the boundaries of the screen
        this.screen_x = clamp(this.x - (float) GameConstants.GAME_SCREEN_WIDTH / 4,
                0,
                GameConstants.GAME_WORLD_WIDTH - (float) GameConstants.GAME_SCREEN_WIDTH / 2);
        this.screen_y = clamp(this.y - (float) GameConstants.GAME_SCREEN_HEIGHT / 2,
                0,
                GameConstants.GAME_WORLD_HEIGHT - GameConstants.GAME_SCREEN_HEIGHT);
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
        return "Tank at: x=" + x + ", y=" + y + ", angle=" + angle;
    }

    public void drawImage(Graphics g) {
        AffineTransform rotation = AffineTransform.getTranslateInstance(x, y);
        rotation.rotate(Math.toRadians(angle), this.img.getWidth() / 2.0, this.img.getHeight() / 2.0);
        Graphics2D g2d = (Graphics2D) g;
        g2d.drawImage(this.img, rotation, null);
        g2d.setColor(Color.RED);
        this.ammo.forEach(bullet -> bullet.drawImage(g2d));

        g2d.setColor(Color.YELLOW);
        g2d.drawRect((int) x - 20, (int) y - 20, 100, 5);
        long currentWidth = 100 - ((this.timeSinceLastShot + this.cooldown) - System.currentTimeMillis()) / (this.cooldown / 100);
        if (currentWidth > 100) {
            currentWidth = 100;
        }
        g2d.fillRect((int) x - 20, (int) y - 20, (int) currentWidth, 5);
        if (currentWidth < 100) {
            g2d.drawString("Reloading...", (int) x + 82, (int) y - 12);
        }
    }
}
