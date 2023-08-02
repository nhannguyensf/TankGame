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

    private final Launcher lf;
    List<GameObject> gobjs = new ArrayList<>(1000);
    private BufferedImage world;
    private Tank t1;
    private Tank t2;
    private long tick = 0;

    /**
     *
     */
    public GameWorld(Launcher lf) {
        this.lf = lf;
    }

    @Override
    public void run() {
        try {
//            Animation an = new Animation(300, 300, ResourceManager.getAnimation("bullet"))
            while (true) {
                this.tick++;
                this.t1.update(); // update tank
                this.t2.update(); // update tank

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
     * Reset game to its initial state.
     */
    public void resetGame() {
        this.tick = 0;
        this.t1.setX(300);
        this.t1.setY(300);
    }

    /**
     * Load all resources for Tank Wars Game. Set all Game Objects to their
     * initial state as well.
     */
    public void InitializeGame() {
        this.world = new BufferedImage(GameConstants.GAME_WORLD_WIDTH,
                GameConstants.GAME_WORLD_HEIGHT,
                BufferedImage.TYPE_INT_RGB);
/**
 * 0 --> Nothing
 * 9--> unbreakables BUT non-collidable
 * 3--> unbreakables
 * 2--> breakables
 * 4--> health
 * 5-->speed
 * 6-->shield
 */
        InputStreamReader isr = new InputStreamReader(Objects.requireNonNull(ResourceManager.class.getClassLoader().getResourceAsStream("maps/map1.csv")));
        try (BufferedReader mapReader = new BufferedReader(isr)) {
            int row = 0;
            String[] gameItems;
            while (mapReader.ready()) {
                gameItems = mapReader.readLine().strip().split(",");
                for (int col = 0; col < gameItems.length; col++) {
                    String gameObject = gameItems[col];
                    if ("0".equals(gameObject)) continue;
                    this.gobjs.add(GameObject.newInstance(gameObject, col * 30, row * 30));
                }
                row++;
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }


        t1 = new Tank(300, 300, 0, 0, (short) 0, ResourceManager.getSprite("tank1"));
        TankControl tc1 = new TankControl(t1, KeyEvent.VK_W, KeyEvent.VK_S, KeyEvent.VK_A, KeyEvent.VK_D, KeyEvent.VK_SPACE);
        this.lf.getJf().addKeyListener(tc1);
        t2 = new Tank(400, 300, 0, 0, (short) 180, ResourceManager.getSprite("tank2"));
        TankControl tc2 = new TankControl(t2, KeyEvent.VK_UP, KeyEvent.VK_DOWN, KeyEvent.VK_LEFT, KeyEvent.VK_RIGHT, KeyEvent.VK_NUMPAD0);
        this.lf.getJf().addKeyListener(tc2);
    }

    //    private void drawFloor(Graphics2D buffer) {
//        BufferedImage floor = ResourceManager.getSprite("floor");
//        buffer.drawImage(floor, 0, 0, null);
//    }
    private void drawFloor(Graphics2D buffer) {
        BufferedImage floor = ResourceManager.getSprite("floor");
        for (int i = 0; i < GameConstants.GAME_WORLD_WIDTH; i += 320) {
            for (int j = 0; j < GameConstants.GAME_WORLD_HEIGHT; j += 240) {
                buffer.drawImage(floor, i, j, null);
            }
        }
    }

    private void renderMinimap(Graphics2D g2, BufferedImage world) {
        BufferedImage mm = world.getSubimage(0, 0, GameConstants.GAME_WORLD_WIDTH, GameConstants.GAME_WORLD_HEIGHT);
        g2.scale(.1, .1);
        g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.7f));
        g2.drawImage(mm,
                (GameConstants.GAME_SCREEN_WIDTH * 10 - GameConstants.GAME_WORLD_WIDTH) / 2,
                (GameConstants.GAME_SCREEN_HEIGHT * 10 - GameConstants.GAME_WORLD_HEIGHT / 2) - 1100,
                null);
    }

    public void renderSplitScreen(Graphics2D g2, BufferedImage world) {
        BufferedImage lh = world.getSubimage((int) this.t1.getX(), (int) this.t1.getY(), GameConstants.GAME_SCREEN_WIDTH / 2, GameConstants.GAME_SCREEN_HEIGHT);
        BufferedImage rh = world.getSubimage((int) this.t2.getX(), (int) this.t2.getY(), GameConstants.GAME_SCREEN_WIDTH / 2, GameConstants.GAME_SCREEN_HEIGHT);
        g2.drawImage(lh, 0, 0, null);
        g2.drawImage(rh, GameConstants.GAME_SCREEN_WIDTH / 2 + 1, 0, null);
    }

    @Override
    public void paintComponent(Graphics g) {
        Graphics2D g2 = (Graphics2D) g;
        Graphics2D buffer = world.createGraphics();
        buffer.setColor(Color.BLACK);
        buffer.fillRect(0, 0, GameConstants.GAME_SCREEN_WIDTH, GameConstants.GAME_SCREEN_HEIGHT);
        this.drawFloor(buffer);
        this.gobjs.forEach(gameObject -> gameObject.drawImage(buffer));
        this.t1.drawImage(buffer);
        this.t2.drawImage(buffer);
        renderSplitScreen(g2, world);
        renderMinimap(g2, world);
    }
}
