import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Random;
import java.util.Set;
import java.util.Stack;

public class GamePanel extends JPanel {

    private static final int ROWS = 10;
    private static final int COLS = 20;
    private static final int BLOCK_SIZE = 30; // Size of each block
    private static final char[] LETTERS = {'A', 'B', 'C', 'D', 'E'};
    private char[][] gridLetters; // Array to store letters
    private InfoPanel infoPanel;
    private Map<Character, Color> letterColors;
    private Set<Point> selectedCells;
    private boolean blocksSelected; // Flag to track if blocks are selected
    private int score;
    private ScorePanel scorePanel;
    private Stack<GameState> history;

    public GamePanel(JFrame windowFrame) {
        setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();

        gridLetters = new char[ROWS][COLS]; // Initialize the grid with letters
        letterColors = new HashMap<>();  // Define colors for each letter
        selectedCells = new HashSet<>(); // Initialize selected cells set
        blocksSelected = false; // Initialize blocksSelected to false
        history = new Stack<>();

        letterColors.put('A', Color.BLUE);
        letterColors.put('B', Color.RED);
        letterColors.put('C', Color.MAGENTA);
        letterColors.put('D', Color.YELLOW);
        letterColors.put('E', Color.CYAN);



        // Create the grid panel
        JPanel gridPanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                drawGrid(g);
            }
        };
        gridPanel.setPreferredSize(new Dimension(COLS * BLOCK_SIZE, ROWS * BLOCK_SIZE));

        // Add a mouse listener to handle clicks
        gridPanel.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e){
                handleMouseClick(e.getX(), e.getY(), windowFrame);
            }
        });

        // Add a KeyListener to handle the ESC key for deselecting blocks
        addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ESCAPE) {
                    cancelSelection(); // Call the method to cancel the selection
                }
            }
        });

        setFocusable(true); // Make sure the game panel is focusable so it can receive key events

        // Place gridPanel in the center
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 3;
        gbc.insets = new Insets(20, 0, 20, 0);
        add(gridPanel, gbc);

        // Create the button panel using the new ButtonPanel class
        ButtonPanel buttonPanel = new ButtonPanel(
                e -> startNewGame(), // Action for "New Game"
                e -> undoLastMove(), // Action for "Undo"
                e -> System.exit(0)  // Action for "Exit"
        );

        // Place buttonPanel below the grid
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.gridwidth = 3;
        gbc.insets = new Insets(0, 0, 20, 0);
        add(buttonPanel, gbc);

        // Create and add the InfoBar at the very bottom
        infoPanel = new InfoPanel();
        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.gridwidth = 3;
        gbc.insets = new Insets(0, 0, 0, 0);
        add(infoPanel, gbc);

        // Create and add the ScorePanel at the top
        scorePanel = new ScorePanel();
        gbc.gridx = 0;
        gbc.gridy = 3;
        gbc.gridwidth = 3;
        gbc.insets = new Insets(10, 0, 0, 0); // Add some space between panels
        add(scorePanel, gbc);

        startNewGame();
    }

    private void fillGridWithRandomLetters() {
        Random random = new Random();
        for (int row = 0; row < ROWS; row++) {
            for (int col = 0; col < COLS; col++) {
                gridLetters[row][col] = LETTERS[random.nextInt(LETTERS.length)];
            }
        }
    }

    private void drawGrid(Graphics g) {
        // Set the font and size for the letters
        Font customFont = new Font("Arial", Font.BOLD, 20);
        g.setFont(customFont);

        for (int row = 0; row < ROWS; row++) {
            for (int col = 0; col < COLS; col++) {
                int x = col * BLOCK_SIZE;
                int y = row * BLOCK_SIZE;

                // Check if the cell is selected and set the color accordingly
                if (selectedCells.contains(new Point(col, row)) && selectedCells.size() > 1) {
                    g.setColor(Color.WHITE); // Highlight selected cells
                } else {
                    // Get the color associated with the letter in this block
                    char letter = gridLetters[row][col];
                    Color color = letterColors.get(letter);
                    g.setColor(color != null ? color : Color.LIGHT_GRAY);
                }

                // Draw the background
                g.fillRect(x, y, BLOCK_SIZE, BLOCK_SIZE);

                // Draw the grid lines
                g.setColor(Color.BLACK);
                g.drawRect(x, y, BLOCK_SIZE, BLOCK_SIZE);

                // Draw the letter in the center of the block
                String letterStr = String.valueOf(gridLetters[row][col]);
                FontMetrics fm = g.getFontMetrics();
                int textWidth = fm.stringWidth(letterStr);
                int textHeight = fm.getAscent();
                int textX = x + (BLOCK_SIZE - textWidth) / 2;
                int textY = y + (BLOCK_SIZE + textHeight) / 2 - 3;

                g.setColor(Color.BLACK);
                g.drawString(letterStr, textX, textY);
            }
        }
        // Set the color for the additional lines (e.g., red for better visibility)
       // g.setColor(Color.RED);

        // Draw an additional vertical line after the last column
        int lastColumnX = COLS * BLOCK_SIZE;  // The x-coordinate after the last column
        int panelHeight = ROWS * BLOCK_SIZE;  // Total height of the grid
        g.drawLine(lastColumnX - 1, 0, lastColumnX - 1, panelHeight);

        // Draw an additional horizontal line after the last row
        int lastRowY = ROWS * BLOCK_SIZE;     // The y-coordinate after the last row
        int panelWidth = COLS * BLOCK_SIZE;   // Total width of the grid
        g.drawLine(0, lastRowY - 1, panelWidth, lastRowY - 1);

        // Draw a small corner rectangle at the bottom right corner (optional)
        g.drawLine(lastColumnX - 1, lastRowY - 1, lastColumnX - 1, lastRowY);
    }

    private void handleMouseClick(int mouseX, int mouseY, JFrame windowFrame) {
        int col = mouseX / BLOCK_SIZE;
        int row = mouseY / BLOCK_SIZE;

        // Make sure the clicked coordinates are valid
        if (col >= 0 && col < COLS && row >= 0 && row < ROWS) {
            Point clickedPoint = new Point(col, row);

            // Check if there are already selected blocks
            if (blocksSelected) {
                // If clicked inside the selected cells, remove blocks
                if (selectedCells.contains(clickedPoint)) {
                    removeSelectedBlocks(windowFrame); // Remove the selected blocks
                } else {
                    // If clicked outside the selected cells, clear selection
                    selectedCells.clear();
                    blocksSelected = false;
                    // Proceed to select new blocks after clearing the selection
                    selectConnectedCells(row, col, gridLetters[row][col]);
                    if (!selectedCells.isEmpty() && selectedCells.size() > 1) {
                        blocksSelected = true;
                        // Update the possible score gain after selection
                        int possibleScoreGain = calculatePossibleScoreGain();
                        scorePanel.updatePossibleScore(possibleScoreGain);
                    }
                }
            } else {
                // No blocks were previously selected, proceed with selecting the connected blocks
                selectedCells.clear(); // Clear previous selections, just in case
                selectConnectedCells(row, col, gridLetters[row][col]);
                if (!selectedCells.isEmpty() && selectedCells.size() > 1) {
                    blocksSelected = true; // Mark the blocks as selected
                    // Update the possible score gain after selection
                    int possibleScoreGain = calculatePossibleScoreGain();
                    scorePanel.updatePossibleScore(possibleScoreGain);
                }
            }
            repaint(); // Repaint the grid to show the changes
        }
    }

    private void selectConnectedCells(int row, int col, char letter) {
        if (row < 0 || row >= ROWS || col < 0 || col >= COLS) return;
        if (gridLetters[row][col] != letter || selectedCells.contains(new Point(col, row)) || gridLetters[row][col] == ' ') return;
        selectedCells.add(new Point(col, row));

        // Check all four directions (up, down, left, right)
        selectConnectedCells(row - 1, col, letter); // Up
        selectConnectedCells(row + 1, col, letter); // Down
        selectConnectedCells(row, col - 1, letter); // Left
        selectConnectedCells(row, col + 1, letter); // Right
    }
    private void cancelSelection() {
        // Clear the selected cells and reset the selection state
        if (!selectedCells.isEmpty()) {
            selectedCells.clear(); // Clear the selection
            blocksSelected = false; // Reset the flag for block selection
            repaint(); // Repaint the grid to reflect the changes
        }
    }
    private boolean checkForPossibleMoves() {
        // Iterate through each cell in the grid
        for (int row = 0; row < ROWS; row++) {
            for (int col = 0; col < COLS; col++) {
                char letter = gridLetters[row][col];
                if (letter != ' ') {
                    // Check for adjacent blocks with the same letter (right and down directions)
                    if (col + 1 < COLS && gridLetters[row][col + 1] == letter) {
                        return true; // Found a move (right neighbor)
                    }
                    if (row + 1 < ROWS && gridLetters[row + 1][col] == letter) {
                        return true; // Found a move (down neighbor)
                    }
                }
            }
        }
        // No moves left
        return false;
    }

    private void removeSelectedBlocks(JFrame parentWindow) {
        saveCurrentState();
        int blocksRemoved = selectedCells.size();
        if (blocksRemoved > 1){
            int pointsGained = calculatePossibleScoreGain();
            score += pointsGained;
            // Update the current score in the ScorePanel
            scorePanel.updateCurrentScore(score);
            scorePanel.updatePossibleScore(0);
        }
        // Remove the selected blocks by setting them to an empty character
        for (Point p : selectedCells) {
            gridLetters[p.y][p.x] = ' '; // Empty character to represent removed block
        }
        selectedCells.clear(); // Clear the selection

        // Apply gravity first
        applyGravity();

        // Now shift left for any empty columns
        shiftColumnsLeft();

        // After shifting and gravity, update the InfoBar with the new counts
        updateInfoBar();

        // Check if there are any possible moves left
        if (!checkForPossibleMoves()) {
            // Show a "Finished!" message if no moves are left
            int heighestScore = ScoreFile.getHighestScore(parentWindow);
            JOptionPane.showMessageDialog(this, "Your final score is: " + score, "Game Over", JOptionPane.INFORMATION_MESSAGE);
            if (score > heighestScore){
                ScoreFile.saveHighestScore(score);
            }
        }
        repaint(); // Repaint the grid to show the changes
    }
    private int calculatePossibleScoreGain() {
        int blocksSelected = selectedCells.size();
        if (blocksSelected > 1) {
            return (int) Math.pow(blocksSelected, 2) - (3 * blocksSelected) + 4;
        }
        return 0;
    }
    private void updateInfoBar(){
        Map<Character, Integer> letterCounts = new HashMap<>();
        for (char letter : LETTERS){
            letterCounts.put(letter, 0);
        }
        for (int row=0; row<ROWS; row++){
            for (int col=0; col<COLS; col++){
                char letter = gridLetters[row][col];
                if (letter != ' '){
                    letterCounts.put(letter, letterCounts.get(letter) + 1);
                }
            }
        }
        StringBuilder infoText = new StringBuilder("Counts: ");
        for (char letter: LETTERS){
            infoText.append(letter).append("=").append(letterCounts.get(letter)).append(" ");
        }
        infoPanel.setInfo(infoText.toString().trim());
    }

    private void shiftColumnsLeft() {
        int targetCol = 0; // Target column to shift to

        for (int col = 0; col < COLS; col++) {
            // Check if the column has any non-empty cells
            boolean hasNonEmptyCell = false;
            for (int row = 0; row < ROWS; row++) {
                if (gridLetters[row][col] != ' ') {
                    hasNonEmptyCell = true;
                    break;
                }
            }

            // If the column is non-empty, move it to the target column position
            if (hasNonEmptyCell) {
                if (col != targetCol) { // Only shift if needed
                    for (int row = 0; row < ROWS; row++) {
                        gridLetters[row][targetCol] = gridLetters[row][col];
                        gridLetters[row][col] = ' '; // Clear the original column
                    }
                }
                targetCol++; // Move the target column to the right
            }
        }
        repaint();
    }

    private void applyGravity() {
        for (int col = 0; col < COLS; col++) {
            int emptyRow = ROWS - 1; // Start from the bottom-most row

            // Move upward through the column
            for (int row = ROWS - 1; row >= 0; row--) {
                if (gridLetters[row][col] != ' ') {
                    // If we find a non-empty cell, move it down to the current emptyRow
                    if (row != emptyRow) {
                        gridLetters[emptyRow][col] = gridLetters[row][col];
                        gridLetters[row][col] = ' '; // Set the original position to empty
                    }
                    emptyRow--; // Move the empty row pointer up
                }
            }
        }
        repaint(); // Repaint the grid to show the blocks falling down
    }

    public void start() {
        // Repaint the game panel to reflect the game start
        repaint();
    }

    private void startNewGame() {
        score = 0; // reset score
        scorePanel.updatePossibleScore(0);
        scorePanel.updateCurrentScore(0);
        // Logic for starting a new game
        fillGridWithRandomLetters();
        // Calculate the counts of each letter
        Map<Character, Integer> letterCounts = new HashMap<>();
        for (char letter : LETTERS) {
            letterCounts.put(letter, 0); // Initialize the count for each letter
        }
        for (int row = 0; row < ROWS; row++) {
            for (int col = 0; col < COLS; col++) {
                char letter = gridLetters[row][col];
                letterCounts.put(letter, letterCounts.get(letter) + 1);
            }
        }
        // Update the InfoBar with the counts
        StringBuilder infoText = new StringBuilder("Counts: ");
        for (char letter : LETTERS) {
            infoText.append(letter).append("=").append(letterCounts.get(letter)).append(" ");
        }
        infoPanel.setInfo(infoText.toString().trim());
        repaint();
    }
    // Function to save the current state to history before making a move
    private void saveCurrentState() {
        // Create a deep copy of the gridLetters array
        char[][] gridCopy = new char[ROWS][COLS];
        for (int row = 0; row < ROWS; row++) {
            System.arraycopy(gridLetters[row], 0, gridCopy[row], 0, COLS);
        }

        // Create a new GameState object and push it onto the stack
        history.push(new GameState(gridCopy, score));
    }
    private void undoLastMove() {
        if (!history.isEmpty()) {
            // Pop the last game state from the stack
            GameState previousState = history.pop();

            // Restore the grid and the score from the previous state
            gridLetters = previousState.getGridState();
            score = previousState.getScore();

            // Update the InfoBar with the current letter counts and score
            updateInfoBar();

            // Repaint the grid to reflect the changes
            repaint();

            // Optionally, you can show a message that the move was undone
            infoPanel.setInfo("Last move undone");
            scorePanel.updateCurrentScore(score);
            scorePanel.updatePossibleScore(0);
        } else {
            // If there's no history, show a message that undo is not available
            infoPanel.setInfo("No more moves to undo.");
        }
    }
}
