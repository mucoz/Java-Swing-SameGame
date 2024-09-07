import java.awt.*;
import javax.swing.*;
import java.net.URL;

public class WindowFrame extends JFrame {  // Inherit from JFrame
    private static final int frameWidth = 1000;
    private static final int frameHeight = 600;
    private static final int minFrameWidth = 1000;
    private static final int minFrameHeight = 600;
    private JPanel mainPanel;
    public WindowFrame() {
        try {
            UIManager.setLookAndFeel("javax.swing.plaf.nimbus.NimbusLookAndFeel");
        } catch (Exception e) {
            e.printStackTrace();
        }
        // Since WindowFrame extends JFrame, no need to create a separate JFrame object
        setTitle("Same Game"); // Set window title
        setSize(frameWidth, frameHeight);
        setMinimumSize(new Dimension(minFrameWidth, minFrameHeight));
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        // Create the main panel (MenuPanel)
        mainPanel = new MenuPanel(this);  // Use the new MenuPanel class
        // Set layout and add the panel
        setLayout(new BorderLayout());
        add(mainPanel, BorderLayout.CENTER);
        pack();
        setSize(frameWidth, frameHeight); // Set the frame size after packing
        setLocationRelativeTo(null);  // Center the window
        URL iconURL = getClass().getResource("/images/logo.png");
        if (iconURL != null) {
            ImageIcon icon = new ImageIcon(iconURL);
            setIconImage(icon.getImage());  // Set the window icon
        } else {
            System.out.println("Icon image not found!");
        }
        setVisible(true);
    }

    // Method to start a new game
    public void startNewGame() {
        // Remove the current panel
        remove(mainPanel);
        // Create a new game panel
        GamePanel gamePanel = new GamePanel(this);
        // Add the game panel to the frame
        add(gamePanel, BorderLayout.CENTER);
        // Refresh the frame
        revalidate();
        repaint();
        // Start the game
        gamePanel.start();
    }
}
