import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;

public class ButtonPanel extends JPanel {

    private JButton newGameButton;
    private JButton undoButton;
    private JButton exitButton;

    public ButtonPanel(ActionListener newGameAction, ActionListener undoAction, ActionListener exitAction) {
        setLayout(new FlowLayout(FlowLayout.CENTER, 20, 10));

        // Create buttons and assign action listeners
        newGameButton = new JButton("New Game");
        undoButton = new JButton("Undo");
        exitButton = new JButton("Exit");

        // Add action listeners passed from the Game class
        newGameButton.addActionListener(newGameAction);
        undoButton.addActionListener(undoAction);
        exitButton.addActionListener(exitAction);

        // Add buttons to the panel
        add(newGameButton);
        add(undoButton);
        add(exitButton);
    }
}
