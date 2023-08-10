package tankrotationexample.menus;

import tankrotationexample.GameConstants;
import tankrotationexample.Launcher;
import tankrotationexample.Resources.ResourceManager;
import tankrotationexample.game.Sound;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;

public class EndGamePanel extends JPanel {

    private final Launcher lf;
    private final BufferedImage tank1win = ResourceManager.getSprite("tank1win");
    private final BufferedImage tank2win = ResourceManager.getSprite("tank2win");
    private int winnerPlayer = 1;


    public EndGamePanel(Launcher lf) {
        this.lf = lf;
        this.setBackground(Color.BLACK);
        this.setLayout(null);

        JButton start = new JButton("Restart Game");
        start.setFont(new Font("Courier New", Font.BOLD, 24));
        start.setBounds((int) (0.5 * GameConstants.START_MENU_SCREEN_WIDTH), (int) (0.6 * GameConstants.START_MENU_SCREEN_HEIGHT), 250, 50);
        start.addActionListener((actionEvent -> {
            this.lf.setFrame("game");
        }));

        JButton mainMenu = new JButton("Main Menu");
        mainMenu.setFont(new Font("Courier New", Font.BOLD, 24));
        mainMenu.setBounds((int) (0.5 * GameConstants.START_MENU_SCREEN_WIDTH), (int) (0.6 * GameConstants.START_MENU_SCREEN_HEIGHT + 75), 250, 50);
        mainMenu.addActionListener((actionEvent -> {
            this.lf.setFrame("start");
        }));

        JButton exit = new JButton("Exit");
        exit.setFont(new Font("Courier New", Font.BOLD, 24));
        exit.setBounds((int) (0.5 * GameConstants.START_MENU_SCREEN_WIDTH), (int) (0.6 * GameConstants.START_MENU_SCREEN_HEIGHT + 150), 250, 50);
        exit.addActionListener((actionEvent -> this.lf.closeGame()));

        this.add(start);
        this.add(mainMenu);
        this.add(exit);
    }

    @Override
    public void paintComponent(Graphics g) {
        Graphics2D g2 = (Graphics2D) g;
        if (this.winnerPlayer == 1) {
            g2.drawImage(this.tank1win, 0, 0, null);
        } else {
            g2.drawImage(this.tank2win, 0, 0, null);
        }
    }

    public void setWinnerPlayer(int winnerPlayer) {
        this.winnerPlayer = winnerPlayer;
    }
}
