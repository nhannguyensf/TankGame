package tankrotationexample.game;


import tankrotationexample.GameConstants;
import tankrotationexample.Launcher;
import tankrotationexample.Resources.ResourceManager;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class GameWorld extends JPanel implements Runnable {

    static double scaleFactor = 0.1;
    private final Launcher lf;
    List<Animation> animations = new ArrayList<>();
    private List<GameObject> gobjs = new ArrayList<>(1000);
    private BufferedImage world;
    private Tank t1;
    private Tank t2;
    private BotAI bot1;
    private BotAI bot2;
    private long tick = 0;

    /**
     *
     */
    public GameWorld(Launcher lf) {
        this.lf = lf;
    }

    /**
     * Reset game to its initial state.
     */
    public void resetGame() {
        this.tick = 0;
        this.t1.setX(300);
        this.t1.setY(300);
    }

    @Override
    public void run() {
        try {
            while (true) {
                this.tick++;
                this.t1.update(); // update tank
                this.t2.update(); // update tank
                this.bot1.update(t1); // update AI tank1 to follow t1
                this.bot2.update(t2); // update AI tank2 to follow t2
                this.animations.forEach(animation -> animation.update());
                checkCollision();
                if (t1.getLive() <= 0) {
                    lf.getEndGamePanel().setWinnerPlayer(2);
                    lf.setFrame("end");
                    break;
                } else if (t2.getLive() <= 0) {
                    lf.getEndGamePanel().setWinnerPlayer(1);
                    lf.setFrame("end");
                    break;
                }
                this.repaint();   // redraw game
                /*
                 * Sleep for 1000/144 ms (~6.9ms). This is done to have our
                 * loop run at a fixed rate per/sec.
                 */
                Thread.sleep(1000 / 144);
            }
        } catch (InterruptedException ignored) {
            System.out.println(ignored);
        }
    }

    /**
     * Load all resources for Tank Wars Game. Set all Game Objects to their
     * initial state as well.
     */
    public void InitializeGame() {
        this.world = new BufferedImage(GameConstants.GAME_WORLD_WIDTH,
                GameConstants.GAME_WORLD_HEIGHT,
                BufferedImage.TYPE_INT_RGB);
/*
  0 --> Nothing
  9--> unbreakables BUT non-collidable
  3--> unbreakables
  2--> breakables
  4--> health
  5-->speed
  6-->shield
 */
        InputStreamReader isr = new InputStreamReader(Objects.requireNonNull(ResourceManager.class.getClassLoader().getResourceAsStream("maps/map1.csv")));
//        this.animations.add(new Animation(300, 300, ResourceManager.getAnimation("bullethit")));
//        this.animations.add(new Animation(350, 300, ResourceManager.getAnimation("bulletshoot")));
//        this.animations.add(new Animation(400, 300, ResourceManager.getAnimation("powerpick")));
//        this.animations.add(new Animation(450, 300, ResourceManager.getAnimation("puffsmoke")));
//        this.animations.add(new Animation(500, 300, ResourceManager.getAnimation("rocketflame")));
//        this.animations.add(new Animation(550, 300, ResourceManager.getAnimation("rockethit")));

        try (BufferedReader mapReader = new BufferedReader(isr)) {
            int row = 0;
            String[] gameItems;
            while (mapReader.ready()) {
                gameItems = mapReader.readLine().strip().split(",");
                for (int col = 0; col < gameItems.length; col++) {
                    String gameObject = gameItems[col];
                    if ("0".equals(gameObject)) continue;
                    this.gobjs.add(GameObject.newInstance(gameObject, col * 30, row * 30, this));
                }
                row++;
            }
        } catch (IOException e) {
            System.out.println("Error while reading map");
            System.exit(-2);
        }

        t1 = (Tank) GameObject.newInstance("11", 100, 100, this);
        TankControl tc1 = new TankControl(t1, KeyEvent.VK_W, KeyEvent.VK_S, KeyEvent.VK_A, KeyEvent.VK_D, KeyEvent.VK_SPACE);
        this.lf.getJf().addKeyListener(tc1);

        t2 = (Tank) GameObject.newInstance("22", GameConstants.GAME_WORLD_WIDTH - 100, GameConstants.GAME_WORLD_HEIGHT - 100, this);
        TankControl tc2 = new TankControl(t2, KeyEvent.VK_UP, KeyEvent.VK_DOWN, KeyEvent.VK_LEFT, KeyEvent.VK_RIGHT, KeyEvent.VK_NUMPAD0);
        this.lf.getJf().addKeyListener(tc2);

        bot1 = (BotAI) GameObject.newInstance("33", 100, GameConstants.GAME_WORLD_HEIGHT - 100, this);
        bot2 = (BotAI) GameObject.newInstance("44", GameConstants.GAME_WORLD_WIDTH - 100, 100, this);

        this.gobjs.add(bot1);
        this.gobjs.add(bot2);
        this.gobjs.add(t1);
        this.gobjs.add(t2);
    }

    private void checkCollision() {
        for (int i = 0; i < this.gobjs.size(); i++) {
            GameObject obj1 = this.gobjs.get(i);
            if (obj1 instanceof BotAI ||
                    obj1 instanceof Wall ||
                    obj1 instanceof BreakableWall ||
                    obj1 instanceof Health ||
                    obj1 instanceof Speed ||
                    obj1 instanceof LivesUp) continue;
            for (int j = 0; j < this.gobjs.size(); j++) {
                if (i == j) continue;
                GameObject obj2 = this.gobjs.get(j);
                if (obj1.getClass() == obj2.getClass() || obj2 instanceof BotAI) continue;
                if (obj1.getHitBox().intersects(obj2.getHitBox())) {
                    System.out.println(obj1 + " HAS HIT " + obj2);
                    obj1.collides(obj2);
                }
            }
        }
        this.gobjs.removeIf(gameObject -> !gameObject.isActive());
    }

    public void addGameObject(GameObject obj) {
        this.gobjs.add(obj);
    }

    public void addAnimations(Animation animation) {
        this.animations.add(animation);
    }

    private void drawFloor(Graphics2D buffer) {
        BufferedImage floor = ResourceManager.getSprite("floor");
        buffer.drawImage(floor, 0, 0, null);
    }
//    private void drawFloor(Graphics2D buffer) {
//        BufferedImage floor = ResourceManager.getSprite("floor");
//        for (int i = 0; i < GameConstants.GAME_WORLD_WIDTH; i += 320) {
//            for (int j = 0; j < GameConstants.GAME_WORLD_HEIGHT; j += 240) {
//                buffer.drawImage(floor, i, j, null);
//            }
//        }
//    }

    private void renderMinimap(Graphics2D g2) {
        BufferedImage mm = this.world.getSubimage(0, 0,
                GameConstants.GAME_WORLD_WIDTH,
                GameConstants.GAME_WORLD_HEIGHT);
        g2.scale(scaleFactor, scaleFactor);
        g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.7f));
        g2.drawImage(mm,
                (GameConstants.GAME_SCREEN_WIDTH * 10 - GameConstants.GAME_WORLD_WIDTH) / 2,
                (GameConstants.GAME_SCREEN_HEIGHT * 10 - GameConstants.GAME_WORLD_HEIGHT / 2) - 1100,
                null);
//        var mmX = GameConstants.GAME_SCREEN_WIDTH / 2 - (GameConstants.GAME_WORLD_WIDTH * scaleFactor);
//        var mmY = GameConstants.GAME_SCREEN_HEIGHT / 2 - (GameConstants.GAME_WORLD_HEIGHT * scaleFactor);
//        AffineTransform mmTransform = AffineTransform.getTranslateInstance(mmX, mmY);
//        mmTransform.scale(scaleFactor, scaleFactor);
//        g2.drawImage(mm, mmTransform, null);
    }

    public void renderSplitScreen(Graphics2D g2) {
        int subImageWidth = GameConstants.GAME_SCREEN_WIDTH / 2;
        int subImageHeight = GameConstants.GAME_SCREEN_HEIGHT;

        int t1CornerX = (int) (t1.getX() - subImageWidth / 2);
        int t1CornerY = (int) (t1.getY() - subImageHeight / 2);
        int t2CornerX = (int) (t2.getX() - subImageWidth / 2);
        int t2CornerY = (int) (t2.getY() - subImageHeight / 2);

        t1CornerX = Math.max(0, Math.min(t1CornerX, GameConstants.GAME_WORLD_WIDTH - subImageWidth));
        t1CornerY = Math.max(0, Math.min(t1CornerY, GameConstants.GAME_WORLD_HEIGHT - subImageHeight));
        t2CornerX = Math.max(0, Math.min(t2CornerX, GameConstants.GAME_WORLD_WIDTH - subImageWidth));
        t2CornerY = Math.max(0, Math.min(t2CornerY, GameConstants.GAME_WORLD_HEIGHT - subImageHeight));

        BufferedImage lh = this.world.getSubimage(t1CornerX, t1CornerY, subImageWidth, subImageHeight);
        BufferedImage rh = this.world.getSubimage(t2CornerX, t2CornerY, subImageWidth, subImageHeight);
        g2.drawImage(lh, 0, 0, null);
        g2.drawImage(rh, GameConstants.GAME_SCREEN_WIDTH / 2 + 2, 0, null);
    }

    @Override
    public void paintComponent(Graphics g) {
        Graphics2D g2 = (Graphics2D) g;
        Graphics2D buffer = world.createGraphics();
        buffer.setColor(Color.BLACK);
        buffer.fillRect(0, 0, GameConstants.GAME_SCREEN_WIDTH, GameConstants.GAME_SCREEN_HEIGHT);
        this.drawFloor(buffer);
        this.gobjs.forEach(gameObject -> gameObject.drawImage(buffer));
//        for (GameObject gameObject : this.gobjs) {
//            if (gameObject.isActive()) {
//                gameObject.drawImage(buffer);
//            }
//        }
        this.t1.drawImage(buffer);
        this.t2.drawImage(buffer);
        this.animations.forEach(animations -> animations.drawImage(buffer));
        renderSplitScreen(g2);
        renderMinimap(g2);
    }
}
