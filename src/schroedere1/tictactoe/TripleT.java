package schroedere1.tictactoe;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

/**
 * Created by edge on 2/24/17.
 */

public class TripleT {

    private int gridSize;
    private Cell[] board;

    private State currentState;
    private Player currentPlayer;
    private Player computerPlayer;

    private int moveCount;

    private Random rand = new Random();

    enum Cell {
        X, O, EMPTY
    }

    enum State {
        PLAYING, DRAW, X_WON, O_WON
    }

    enum Player {
        ONE, TWO
    }


    public TripleT(int gridSize, boolean isComputerFirst){
        this.gridSize = gridSize;
        board = new Cell[gridSize * gridSize];
        moveCount = 0;
        currentPlayer = Player.ONE;

        computerPlayer = isComputerFirst ? Player.ONE : Player.TWO;

        for (int i = 0; i < board.length; i++){
            board[i] = Cell.EMPTY;
        }

        currentState = State.PLAYING;
    }

    public State getCurrentState() {
        return currentState;
    }

    public boolean isValidMove(int row, int col){
        return isValidMove(row * gridSize + col);
    }

    /**
     * Records a move for the player. Only succeeds if it is the players turn, the game is still
     * active, and the requested cell is open.
     *
     * @param row The 0 based row of the requested move
     * @param col The 0 based column of the requested move
     * @return True if the move succeeded, false if it failed.
     */
    public boolean makeHumanMove(int row, int col) {

        if (isActive() && isValidMove(row, col) && currentPlayer != computerPlayer) {
            makeMove(row * gridSize + col);
            return true;
        }

        return false;
    }

    /**
     * Gets the index of the move that was made by the computer. The method will return
     * null if it is not the computers turn or the game has ended.
     *
     * @return a grid index containing the row and column of the move the computer made.
     */
    public GridIndex getComputerMove() {
        return computerMove();
    }

    /**
     * Method to determine if the game is still active.
     *
     * @return true if the game is still running, false if over
     */
    public boolean isActive(){
        return currentState == State.PLAYING;
    }

    private GridIndex computerMove() {
        if (isActive() && currentPlayer == computerPlayer) {
            List<Integer> openCells = getOpenCells();
            int index = rand.nextInt(openCells.size());
            int randomCell = openCells.get(index);

            makeMove(randomCell);

            return new GridIndex(randomCell / gridSize, randomCell % gridSize);
        }

        return null;
    }

    private List<Integer> getOpenCells(){
        List<Integer> openCells = new ArrayList<>(gridSize * gridSize);
        for(int i = 0; i < board.length; i++){
            if (board[i] == Cell.EMPTY)
                openCells.add(i);
        }
        return openCells;
    }

    private void makeMove(int cell){
        board[cell] = cellForCurrentPlayer();
        moveCount++;
        checkForGameOver(cell);
        endTurn();
    }

    private boolean isValidMove(int cell){
        return cell >= 0 && cell < board.length && board[cell] == Cell.EMPTY;
    }

    private void endTurn(){
        currentPlayer = currentPlayer == Player.ONE ? Player.TWO : Player.ONE;
    }

    private void checkForGameOver(int cell) {
        Cell targetCell = cellForCurrentPlayer();
        State winState = targetCell == Cell.X ? State.X_WON : State.O_WON;
        int row = cell / gridSize;
        int col = cell % gridSize;

        // Check for vertical win
        boolean win = true;
        for (int i = col; i < gridSize * gridSize; i += gridSize){
            if (board[i] != targetCell){
                win = false;
                break;
            }
        }
        if (win){
            currentState = winState;
            return;
        }

        // Check for horizontal win
        win = true;
        int firstInRow = row * gridSize;
        for (int i = firstInRow; i < firstInRow + gridSize; i++){
            if (board[i] != targetCell){
                win = false;
                break;
            }
        }
        if (win){
            currentState = winState;
            return;
        }

        // Check for down-right diagonal win
        if (row == col){
            win = true;
            for (int i = 0; i < gridSize * gridSize; i += gridSize + 1){
                if (board[i] != targetCell){
                    win = false;
                    break;
                }
            }
            if (win){
                currentState = winState;
                return;
            }
        }


        // Check for down-left diagonal win
        if (row + col == gridSize - 1){
            win = true;
            int startCell = gridSize - 1;
            for (int i = startCell; i < gridSize * gridSize - 1; i += gridSize - 1){
                if (board[i] != targetCell){
                    win = false;
                    break;
                }
            }
            if (win){
                currentState = winState;
                return;
            }
        }


        // Check for tie if the move count equals the total number of cells
        if (moveCount == gridSize * gridSize){
            currentState = State.DRAW;
            // No winner, keep winner null
        }
    }

    private Cell cellForCurrentPlayer(){
        return cellForPlayer(currentPlayer);
    }

    private Cell cellForPlayer(Player player){ return player == Player.ONE ? Cell.X : Cell.O; }

    private Cell[] getBoardForMove(Cell[] board, int cell, Player player){
        Cell c = cellForPlayer(player);

        Cell[] newBoard = Arrays.copyOf(board, board.length);

        newBoard[cell] = c;

        return newBoard;
    }

    // TODO In progress...
    private int score(Cell[] board){
        Cell computerCell = cellForPlayer(computerPlayer);

        return -1;

    }

}

/**
 * Simple immutable data class to store an index of a 2D array. Has two publicly accessible
 * integer data members, row and col.
 */
class GridIndex {
    final int row;
    final int col;

    public GridIndex(int row, int col){
        this.row = row;
        this.col = col;
    }
}
