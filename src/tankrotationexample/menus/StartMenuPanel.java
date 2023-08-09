package tankrotationexample.menus;


import tankrotationexample.GameConstants;
import tankrotationexample.Launcher;
import tankrotationexample.Resources.ResourceManager;
import tankrotationexample.game.Sound;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;

public class StartMenuPanel extends JPanel {

    private final Launcher lf;
    private final BufferedImage menuBackground;
    Sound bg = ResourceManager.getSound("bg");

    public StartMenuPanel(Launcher lf) {
        bg.setLooping();
        bg.playSound();

        this.lf = lf;
        menuBackground = ResourceManager.getSprite("menu");
        this.setBackground(Color.BLACK);
        this.setLayout(null);

        JButton start = new JButton("Start");
        start.setFont(new Font("Courier New", Font.BOLD, 24));
        start.setBounds((int) (0.61 * GameConstants.START_MENU_SCREEN_WIDTH), (int) (0.6 * GameConstants.START_MENU_SCREEN_HEIGHT), 150, 50);
        start.addActionListener(actionEvent -> {
            bg.stopSound();
            this.lf.setFrame("game");
        });

        JButton instructions = new JButton("Controls");
        instructions.setFont(new Font("Courier New", Font.BOLD, 24));
        instructions.setBounds((int) (0.61 * GameConstants.START_MENU_SCREEN_WIDTH), (int) (0.6 * GameConstants.START_MENU_SCREEN_HEIGHT + 75), 150, 50);
        instructions.addActionListener(actionEvent -> showInstructions());
        this.add(instructions);

        JButton exit = new JButton("Exit");
        exit.setSize(new Dimension(200, 100));
        exit.setFont(new Font("Courier New", Font.BOLD, 24));
        exit.setBounds((int) (0.61 * GameConstants.START_MENU_SCREEN_WIDTH), (int) (0.6 * GameConstants.START_MENU_SCREEN_HEIGHT + 150), 150, 50);
        exit.addActionListener((actionEvent -> this.lf.closeGame()));

        this.add(start);
        this.add(exit);
    }

    private void showInstructions() {
        // This is a simple way to display instructions. Consider updating this to display the instructions in a more user-friendly manner.
        String instructionText = "Player 1:\n" +
                "Move with W, A, S, D\n" +
                "Shoot with SPACE\n" +
                "\n" +
                "Player 2:\n" +
                "Move with Arrow keys\n" +
                "Shoot with NUMPAD0\n" +
                "\nCollect items for Speed Boost, Health, Lives!";
        JOptionPane.showMessageDialog(this, instructionText, "Game Instructions", JOptionPane.INFORMATION_MESSAGE);
    }

    @Override
    public void paintComponent(Graphics g) {
        Graphics2D g2 = (Graphics2D) g;
        g2.drawImage(this.menuBackground, 0, 0, null);
        // Drawing the credit
        g2.setColor(Color.LIGHT_GRAY);
        g2.setFont(new Font("Arial", Font.PLAIN, 12));
        String creditText = "Developed by Nhan Nguyen";
        // Center the text
        int yPosition = this.getHeight() - 10;
        int stringWidth = g2.getFontMetrics().stringWidth(creditText);
        int xPosition = (this.getWidth() - stringWidth) / 2;

        g2.drawString(creditText, xPosition, yPosition);
    }
}
