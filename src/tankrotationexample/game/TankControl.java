package tankrotationexample.game;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class TankControl implements KeyListener {
    private final Tank tank;
    private int up;
    private int down;
    private int right;
    private int left;
    private int shoot;

    public TankControl(Tank tank, int up, int down, int left, int right, int shoot) {
        this.tank = tank;
        this.up = up;
        this.down = down;
        this.right = right;
        this.left = left;
        this.shoot = shoot;
    }

    @Override
    public void keyTyped(KeyEvent ke) {

    }

    @Override
    public void keyPressed(KeyEvent ke) {
        int keyPressed = ke.getKeyCode();
        if (keyPressed == up) {
            this.tank.toggleUpPressed();
        }
        if (keyPressed == down) {
            this.tank.toggleDownPressed();
        }
        if (keyPressed == left) {
            this.tank.toggleLeftPressed();
        }
        if (keyPressed == right) {
            this.tank.toggleRightPressed();
        }
        if (keyPressed == shoot) {
            this.tank.toggleShootPressed();
        }
    }

    @Override
    public void keyReleased(KeyEvent ke) {
        int keyReleased = ke.getKeyCode();
        if (keyReleased == up) {
            this.tank.unToggleUpPressed();
        }
        if (keyReleased == down) {
            this.tank.unToggleDownPressed();
        }
        if (keyReleased == left) {
            this.tank.unToggleLeftPressed();
        }
        if (keyReleased == right) {
            this.tank.unToggleRightPressed();
        }
        if (keyReleased == shoot) {
            this.tank.unToggleShootPressed();
        }
    }
}
