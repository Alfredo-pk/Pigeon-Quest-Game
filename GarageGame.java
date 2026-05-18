import javax.swing.*;
import java.awt.*;

public class GarageGame extends JFrame {
    private CardLayout cardLayout = new CardLayout();
    private JPanel mainPanel = new JPanel(cardLayout);
    private GamePanel gamePanel;

    public GarageGame() {
        setTitle("PIGEON QUEST: REBORN");
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        InstructionPanel menuPanel = new InstructionPanel(this);
        gamePanel = new GamePanel(this);

        mainPanel.add(menuPanel, "Menu");
        mainPanel.add(gamePanel, "Game");

        add(mainPanel);
        setVisible(true);
    }

    public void startGame() {
        cardLayout.show(mainPanel, "Game");
        gamePanel.initGame();
        gamePanel.requestFocusInWindow();
    }

    public void returnToMenu() {
        cardLayout.show(mainPanel, "Menu");
    }
}