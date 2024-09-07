import javax.swing.*;
import java.awt.*;

public class ScorePanel extends JPanel {
    private JLabel currentScoreLabel;
    private JLabel possibleScoreLabel;

    public ScorePanel() {
        setLayout(new GridLayout(1, 2)); // Two labels in a single row

        // Initialize the labels
        currentScoreLabel = new JLabel("Current Score: 0");
        possibleScoreLabel = new JLabel("Possible Score Gain: 0");

        // Add the labels to the panel
        add(currentScoreLabel);
        add(possibleScoreLabel);
    }

    // Method to update the current score
    public void updateCurrentScore(int score) {
        currentScoreLabel.setText("Current Score: " + score);
    }

    // Method to update the possible score gain
    public void updatePossibleScore(int possibleScore) {
        possibleScoreLabel.setText("Possible Score Gain: " + possibleScore);
    }
}
