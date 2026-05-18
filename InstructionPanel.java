import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;

public class InstructionPanel extends JPanel {
    public InstructionPanel(GarageGame parent) {
        setLayout(new GridBagLayout());
        setBackground(new Color(10, 10, 20));

        JLabel title = new JLabel("PIGEON QUEST");
        title.setFont(new Font("Arial", Font.BOLD, 90));
        title.setForeground(new Color(0, 255, 255));

        JTextArea instructions = new JTextArea(
                "מטרת המשחק: לשרוד כמה שיותר זמן מבלי להתנגש בצינורות הניאון או ברצפה.\n\n" +
                        "תפעול המשחק:\n" +
                        "- מקש רווח (SPACE) או חץ למעלה (UP) כדי לעוף.\n" +
                        "- מקש P כדי להשהות / להמשיך את המשחק (Pause).\n\n" +
                        "הקושי יעלה ככל שתצברו יותר נקודות!"
        );
        instructions.setFont(new Font("Arial", Font.BOLD, 24));
        instructions.setForeground(Color.LIGHT_GRAY);
        instructions.setBackground(new Color(10, 10, 20));
        instructions.setEditable(false);
        instructions.setComponentOrientation(ComponentOrientation.RIGHT_TO_LEFT);

        JButton playBtn = new JButton("התחל משחק");
        playBtn.setFont(new Font("Arial", Font.BOLD, 30));
        playBtn.setPreferredSize(new Dimension(300, 80));
        playBtn.setBackground(new Color(0, 255, 150));
        playBtn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        playBtn.setFocusPainted(false);

        playBtn.addActionListener(e -> parent.startGame());

      
        InputMap im = getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW);
        ActionMap am = getActionMap();
        im.put(KeyStroke.getKeyStroke(KeyEvent.VK_SPACE, 0), "startGame");
        am.put("startGame", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                parent.startGame();
            }
        });

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.insets = new Insets(20, 0, 30, 0);

        gbc.gridy = 0; add(title, gbc);
        gbc.gridy = 1; add(instructions, gbc);
        gbc.gridy = 2; add(playBtn, gbc);
    }
}