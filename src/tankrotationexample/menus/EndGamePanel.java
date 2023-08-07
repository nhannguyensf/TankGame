package tankrotationexample.menus;

import tankrotationexample.Launcher;
import tankrotationexample.Resources.ResourceManager;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;

public class EndGamePanel extends JPanel {

    private final Launcher lf;
    private final BufferedImage menuBackground;
    private final BufferedImage tank1win;
    private final BufferedImage tank2win;
    private int winnerPlayer = 1;


    public EndGamePanel(Launcher lf) {
        this.lf = lf;
        menuBackground = ResourceManager.getSprite("menu");
        tank1win = ResourceManager.getSprite("tank1win");
        tank2win = ResourceManager.getSprite("tank2win");
        this.setBackground(Color.BLACK);
        this.setLayout(null);

        JButton start = new JButton("Restart Game");
        start.setFont(new Font("Courier New", Font.BOLD, 24));
        start.setBounds(110, 300, 250, 50);
        start.addActionListener((actionEvent -> this.lf.setFrame("game")));


        JButton exit = new JButton("Exit");
        exit.setFont(new Font("Courier New", Font.BOLD, 24));
        exit.setBounds(110, 375, 250, 50);
        exit.addActionListener((actionEvent -> this.lf.closeGame()));

        this.add(start);
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
