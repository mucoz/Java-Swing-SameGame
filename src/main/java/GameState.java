public class GameState {
    private char[][] gridState; // The grid letters
    private int score;          // The score

    // Constructor
    public GameState(char[][] gridState, int score) {
        this.gridState = new char[gridState.length][gridState[0].length];
        for (int row = 0; row < gridState.length; row++) {
            System.arraycopy(gridState[row], 0, this.gridState[row], 0, gridState[row].length);
        }
        this.score = score;
    }

    // Getter for grid state
    public char[][] getGridState() {
        return gridState;
    }

    // Getter for score
    public int getScore() {
        return score;
    }
}
