import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class MenuPanel extends JPanel {
    private JButton newGameButton;
    private JButton scoreButton;
    private JButton exitButton;

    private JPanel gamePanel;

    public MenuPanel(WindowFrame windowFrame) {  // Use WindowFrame instead of Window
        setLayout(new GridBagLayout());

        // Button dimensions
        Dimension buttonSize = new Dimension(150, 50);

        // Create buttons
        newGameButton = new JButton("New Game");
        scoreButton = new JButton("Highest Score");
        exitButton = new JButton("Exit");

        // Set button sizes
        newGameButton.setPreferredSize(buttonSize);
        scoreButton.setPreferredSize(buttonSize);
        exitButton.setPreferredSize(buttonSize);

        // Layout constraints
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = GridBagConstraints.RELATIVE;
        gbc.insets = new Insets(10, 0, 10, 0); // Spacing between buttons
        gbc.anchor = GridBagConstraints.CENTER;

        // Add buttons to panel
        add(newGameButton, gbc);
        add(scoreButton, gbc);
        add(exitButton, gbc);

        // Add action listeners to buttons
        newGameButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                windowFrame.startNewGame();  // Call startNewGame() from WindowFrame class
            }
        });

        scoreButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int highestScore = ScoreFile.getHighestScore(windowFrame); // windowFrame.getHighestScore(); // Get the highest score from WindowFrame class
                JOptionPane.showMessageDialog(windowFrame, "Highest Score: " + highestScore,
                        "Highest Score", JOptionPane.INFORMATION_MESSAGE);
            }
        });

        exitButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.exit(0);  // Exit the application
            }
        });
    }
}
