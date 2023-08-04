package tankrotationexample.game;

import tankrotationexample.GameConstants;
import tankrotationexample.Resources.ResourceManager;

import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;

public class Tank extends GameObject {
    private static final int MAX_HEALTH = 100;
    private static final float MIN_SPEED = 2;
    private static final float MAX_SPEED = MIN_SPEED + Speed.getSpeedBoost();
    private static final long SPEED_INCREASE_DURATION = 3000;
    private static float SPEED = MIN_SPEED;
    private final long reloadAmmo = 3000;
    private final float ROTATIONSPEED = 2.0f;
    private final Rectangle hitBox;
    private final BufferedImage img;
    List<Bullet> ammo = new ArrayList<>();
    int health = MAX_HEALTH;
    private long speedBoostDuration = 1500; // 10 seconds in milliseconds
    private long lastSpeedIncreaseTime = 0L;
    private long timeSinceLastShot = 0L;
    private float x;
    private float y;
    private float vx;
    private float vy;
    private float angle;
    private boolean UpPressed;
    private boolean DownPressed;
    private boolean RightPressed;
    private boolean LeftPressed;
    private boolean shootPressed;

    Tank(float x, float y, float vx, float vy, float angle, BufferedImage img) {
        this.x = x;
        this.y = y;
        this.vx = vx;
        this.vy = vy;
        this.img = img;
        this.angle = angle;
        this.hitBox = new Rectangle((int) x, (int) y, this.img.getWidth(), this.img.getHeight());
    }

    public Rectangle getHitBox() {
        return this.hitBox.getBounds();
    }

    @Override
    public boolean isActive() {
        return true;
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

    @Override
    public void collides(GameObject with) {
        if (with instanceof Wall || with instanceof BreakableWall) {
            //stop
            handleAllWallCollision(with);
            System.out.println("Tank is hitting a type of wall");
        } else if (with instanceof PowerUp) {
            ((PowerUp) with).applyPowerUp(this);
        }
    }

    private void handleAllWallCollision(GameObject allWallType) {
        Rectangle wallBounds = allWallType.getHitBox();

        // Determine the depth of the overlap along both axes
        float xOverlap = (float) Math.min(this.hitBox.getMaxX() - wallBounds.getMinX(), wallBounds.getMaxX() - this.hitBox.getMinX());
        float yOverlap = (float) Math.min(this.hitBox.getMaxY() - wallBounds.getMinY(), wallBounds.getMaxY() - this.hitBox.getMinY());

        // Correct the position based on which overlap is smallest
        if (xOverlap < yOverlap) {
            // Resolve collision along the x-axis
            if (this.hitBox.getCenterX() < wallBounds.getCenterX()) {
                // Move the tank to the left of the wall
                this.x -= xOverlap;
            } else {
                // Move the tank to the right of the wall
                this.x += xOverlap;
            }
        } else {
            // Resolve collision along the y-axis
            if (this.hitBox.getCenterY() < wallBounds.getCenterY()) {
                // Move the tank above the wall
                this.y -= yOverlap;
            } else {
                // Move the tank below the wall
                this.y += yOverlap;
            }
        }

        // Update the tank's hitBox to the new position
        this.hitBox.setLocation((int) x, (int) y);
    }

    public void increaseHealth() {
        this.health -= Health.getHealthBoost();
        System.out.println("Health = " + this.health);
        if (this.health > MAX_HEALTH) {
            this.health = MAX_HEALTH;
        }
    }

    public void increaseSpeed() {
        if (SPEED < MAX_SPEED && (this.lastSpeedIncreaseTime + lastSpeedIncreaseTime) > System.currentTimeMillis()) {
            SPEED = MAX_SPEED;
            this.lastSpeedIncreaseTime = System.currentTimeMillis();
            System.out.println("Speed = " + SPEED + " at " + System.currentTimeMillis() + "ms");
        }
    }
//    public void increaseSpeed() {
//        if (SPEED < MAX_SPEED || (System.currentTimeMillis() - this.lastSpeedIncreaseTime) < speedBoostDuration) {
//            SPEED = MAX_SPEED;
//            this.lastSpeedIncreaseTime = System.currentTimeMillis();
//            System.out.println("Speed = " + SPEED + " at " + System.currentTimeMillis() + "ms");
//        }
//    }

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

        if (this.shootPressed && ((this.timeSinceLastShot + this.reloadAmmo) < System.currentTimeMillis())) {
            this.timeSinceLastShot = System.currentTimeMillis();

            // Define the offset to keep the ammo further from the tank
            final float BULLET_OFFSET = 10.0f;
            // Calculate the offset for the x and y coordinates based on the angle
            float bulletX = x + ((float) img.getWidth() / 2 - BULLET_OFFSET / 2) + (float) (Math.cos(Math.toRadians(angle)) * ((img.getWidth() / 2) + BULLET_OFFSET));
            float bulletY = y + ((float) img.getHeight() / 2 - BULLET_OFFSET / 2) + (float) (Math.sin(Math.toRadians(angle)) * ((img.getWidth() / 2) + BULLET_OFFSET));
            this.ammo.add(new Bullet(bulletX, bulletY, ResourceManager.getSprite("bullet"), angle));
        }
//        this.ammo.forEach(Bullet::update);
//        for (Bullet bullet : this.ammo) {
//            if (bullet.isActive()) {
//                bullet.update();
//                bullet.drawImage(g);
//            }
//        }
        this.hitBox.setLocation((int) x, (int) y);

        if ((this.lastSpeedIncreaseTime + this.SPEED_INCREASE_DURATION) < System.currentTimeMillis()) {
            this.lastSpeedIncreaseTime = System.currentTimeMillis();
            if ((SPEED > MIN_SPEED)) {
                SPEED = MIN_SPEED;
                System.out.println("Reset speed = " + SPEED + " at " + System.currentTimeMillis() + "ms");
            }
        }

//        if ((System.currentTimeMillis() - this.lastSpeedIncreaseTime) > speedBoostDuration && SPEED > MIN_SPEED) {
//            SPEED = MIN_SPEED;
//            System.out.println("Reset speed = " + SPEED + " at " + System.currentTimeMillis() + "ms");
//        }
    }

    private void rotateLeft() {
        this.angle -= this.ROTATIONSPEED;
    }

    private void rotateRight() {
        this.angle += this.ROTATIONSPEED;
    }

    private void moveBackwards() {
        vx = Math.round(SPEED * Math.cos(Math.toRadians(angle)));
        vy = Math.round(SPEED * Math.sin(Math.toRadians(angle)));
        x -= vx;
        y -= vy;
        checkBorder();
    }

    private void moveForwards() {
        vx = Math.round(SPEED * Math.cos(Math.toRadians(angle)));
        vy = Math.round(SPEED * Math.sin(Math.toRadians(angle)));
        x += vx;
        y += vy;
        checkBorder();
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
//        this.ammo.forEach(bullet -> bullet.drawImage(g2d));
        for (Bullet bullet : this.ammo) {
            if (bullet.isActive()) {
                bullet.update();
                bullet.drawImage(g2d);
            }
        }
        drawReloadBar(g2d);
        drawHealthBar(g2d);
    }

    private void drawReloadBar(Graphics2D g2d) {
        g2d.setColor(Color.YELLOW);
        g2d.drawRect((int) x - 20, (int) y - 20, 100, 5);
        long currentWidth = 100 - ((this.timeSinceLastShot + this.reloadAmmo) - System.currentTimeMillis()) / (this.reloadAmmo / 100);
        if (currentWidth > 100) {
            currentWidth = 100;
        }
        g2d.fillRect((int) x - 20, (int) y - 20, (int) currentWidth, 5);
        if (currentWidth < 100) {
            g2d.drawString("Reloading...", (int) x + 82, (int) y - 12);
        }
    }

    private void drawHealthBar(Graphics2D g2d) {
        g2d.setColor(Color.GREEN);
        g2d.drawRect((int) x - 20, (int) y - 40, 100, 20);
        g2d.fillRect((int) x - 20, (int) y - 40, this.health, 20);
        if (this.health < 100) {
            g2d.setColor(Color.ORANGE);
            g2d.drawString("Low health!!!", (int) x + 82, (int) y - 12);
        }
//        else if (this.health < 50) {
//            g2d.setColor(Color.RED);
//        }
    }
}
