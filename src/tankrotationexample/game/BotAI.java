package tankrotationexample.game;

import tankrotationexample.GameConstants;
import tankrotationexample.Resources.ResourceManager;

import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;

public class BotAI extends Tank {
    private static final float SPEED = .6f;
    private final float ROTATIONSPEED = 2.0f;
    private final long reloadAmmo = 3000;
    private final Rectangle hitBox;
    private final BufferedImage img;
    private long timeSinceLastShot = 0L;
    private float x;
    private float y;
    private float vx;
    private float vy;
    private float angle;
    private GameWorld gameWorld;

    BotAI(float x, float y, float vx, float vy, float angle, BufferedImage img, GameWorld gameWorld) {
        this.x = x;
        this.y = y;
        this.vx = vx;
        this.vy = vy;
        this.img = img;
        this.angle = angle;
        this.hitBox = new Rectangle((int) x, (int) y, this.img.getWidth(), this.img.getHeight());
        this.gameWorld = gameWorld;
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

    @Override
    public void collides(GameObject with) {
//        if (with instanceof Wall || with instanceof BreakableWall) {
//            //stop
//            handleAllWallCollision(with);
//            System.out.println("TankAI is hitting a type of wall");
//        } else
        if (with instanceof Tank) {
            // Handle collision with player's tank
            System.out.println("TankAI is hitting a player's tank");
//            handleTankAICollision((Tank) with);
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

    void update(Tank tank) {
        attackPlayer(tank);
        this.hitBox.setLocation((int) x, (int) y);
    }

    public void attackPlayer(Tank playerTank) {
        float deltaX = playerTank.getX() - x;
        float deltaY = playerTank.getY() - y;
        pursuitPlayer(deltaX, deltaY);
        shootPlayer(deltaX, deltaY);
    }

    private void pursuitPlayer(float deltaX, float deltaY) {
        float targetAngle = (float) Math.toDegrees(Math.atan2(deltaY, deltaX));

        if (Math.abs(targetAngle - angle) > ROTATIONSPEED) {
            if (angle < targetAngle) this.rotateRight();
            else this.rotateLeft();
        } else {
            this.moveForwards();
        }
    }

    private void shootPlayer(float deltaX, float deltaY) {
        float distanceToPlayer = (float) Math.sqrt(deltaX * deltaX + deltaY * deltaY);
        if ((distanceToPlayer < 300) && ((this.timeSinceLastShot + this.reloadAmmo) < System.currentTimeMillis())) {
            this.timeSinceLastShot = System.currentTimeMillis();

            // Define the offset to keep the ammo further from the tank
            final float BULLET_OFFSET = 10.0f;
            // Calculate the offset for the x and y coordinates based on the angle
            float bulletX = x + ((float) img.getWidth() / 2 - BULLET_OFFSET / 2) + (float) (Math.cos(Math.toRadians(angle)) * ((img.getWidth() / 2) + BULLET_OFFSET));
            float bulletY = y + ((float) img.getHeight() / 2 - BULLET_OFFSET / 2) + (float) (Math.sin(Math.toRadians(angle)) * ((img.getWidth() / 2) + BULLET_OFFSET));
            Bullet bullet = new Bullet(bulletX, bulletY, ResourceManager.getSprite("bullet"), angle, 6);
            this.ammo.add(bullet);
            gameWorld.addGameObject(bullet);
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
        for (Bullet bullet : this.ammo) {
            if (bullet.isActive()) {
                bullet.update();
                bullet.drawImage(g2d);
            }
        }
        drawReloadBar(g2d);
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
}
