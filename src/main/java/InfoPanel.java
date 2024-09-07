import javax.swing.*;
import java.awt.*;

public class InfoPanel extends JPanel {
    private JLabel infoLabel;

    public InfoPanel() {
        setLayout(new BorderLayout());
        infoLabel = new JLabel("Welcome to the game!", JLabel.CENTER);
        add(infoLabel, BorderLayout.CENTER);
        setPreferredSize(new Dimension(800, 30)); // Adjust size as needed
    }

    public void setInfo(String info) {
        infoLabel.setText(info);
    }
}
