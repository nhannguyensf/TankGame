package tankrotationexample.game;

import tankrotationexample.GameConstants;

import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;

public class BotAI extends GameObject {
    //    private static final int MAX_HEALTH = 100;
//    private static final float MIN_SPEED = 2;
//    private static final float MAX_SPEED = MIN_SPEED + Speed.getSpeedBoost();
//    private static final long SPEED_INCREASE_DURATION = 3000;
    private static final float SPEED = .6f;
    //    private final long reloadAmmo = 3000;
    private final float ROTATIONSPEED = 1.0f;
    private final Rectangle hitBox;
    private final BufferedImage img;
    //    List<Bullet> ammo = new ArrayList<>();
//    int health = MAX_HEALTH;
//    private long speedBoostDuration = 1500; // 10 seconds in milliseconds
//    private long lastSpeedIncreaseTime = 0L;
//    private long timeSinceLastShot = 0L;
    private float x;
    private float y;
    private float vx;
    private float vy;
    private float angle;
//    private boolean UpPressed;
//    private boolean DownPressed;
//    private boolean RightPressed;
//    private boolean LeftPressed;
//    private boolean shootPressed;

    BotAI(float x, float y, float vx, float vy, float angle, BufferedImage img) {
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

    void update() {
        this.hitBox.setLocation((int) x, (int) y);
    }

    public void moveAI(float targetX, float targetY) {
        float deltaX = targetX - x;
        float deltaY = targetY - y;
        float targetAngle = (float) Math.toDegrees(Math.atan2(deltaY, deltaX));

        if (Math.abs(targetAngle - angle) > ROTATIONSPEED) {
            if (angle < targetAngle) rotateRight();
            else rotateLeft();
        } else {
            moveForwards();
        }
    }

    @Override
    public String toString() {
        return "botAI at: x=" + x + ", y=" + y + ", angle=" + angle;
    }

    public void drawImage(Graphics g) {
        AffineTransform rotation = AffineTransform.getTranslateInstance(x, y);
        rotation.rotate(Math.toRadians(angle), this.img.getWidth() / 2.0, this.img.getHeight() / 2.0);
        Graphics2D g2d = (Graphics2D) g;
        g2d.drawImage(this.img, rotation, null);
        g2d.setColor(Color.RED);
    }
}
