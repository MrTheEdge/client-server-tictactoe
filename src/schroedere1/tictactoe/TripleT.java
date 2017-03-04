package schroedere1.tictactoe;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

/**
 * E.J. Schroeder
 * Andy Gergel
 *
 * Assn #1 - Client-Server TicTacToe
 * CSC 460
 */

public class TripleT {

    private int gridSize;
    private Cell[] board; // Yes. This should have been a 2d array.

    private State currentState;
    private Player currentPlayer;
    private Player computerPlayer;

    private int moveCount;

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
//            List<Integer> openCells = getOpenCells();
//            int index = rand.nextInt(openCells.size());
//            int randomCell = openCells.get(index);

            int bestCell = getBestMove();

            makeMove(bestCell);

            return new GridIndex(bestCell / gridSize, bestCell % gridSize);
        }

        return null;
    }

    public Player getWinner(){
        if (isActive() || currentState == State.DRAW)
            return null;

        return currentState == State.X_WON ? Player.ONE : Player.TWO;
    }

    private List<Integer> getOpenCells(Cell[] board){
        List<Integer> openCells = new ArrayList<>(gridSize * gridSize);
        for(int i = 0; i < board.length; i++){
            if (board[i] == Cell.EMPTY)
                openCells.add(i);
        }
        return openCells;
    }

    public List<Integer> getOpenCells(){
        return getOpenCells(board);
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

    private int getBestMove(){
        int bestMoveValue = Integer.MIN_VALUE;
        int bestMove = -1;

        List<Integer> openCells = getOpenCells();
        Cell[] boardWithPossibleMove;
        for (int cell : openCells){

            boardWithPossibleMove = getBoardForMove(board, cell, computerPlayer);

            // We just made the computers move, now it's the opponents turn.
            int moveValue = minimax(boardWithPossibleMove, 0, false);

            if (moveValue > bestMoveValue){
                bestMove = cell;
                bestMoveValue = moveValue;
            }
        }

        return bestMove;
    }

    private int minimax(Cell[] board, int depth, boolean isComputer){
        int score = score(board);

        if (score == Integer.MAX_VALUE) return score - depth;
        if (score == Integer.MIN_VALUE) return depth + Integer.MIN_VALUE;
        if (!hasOpenCells(board)) return 0;

        int best = isComputer ? Integer.MIN_VALUE : Integer.MAX_VALUE;
        Player nextMovePlayer;
        if (isComputer){
            nextMovePlayer = computerPlayer;
        } else {
            // Player for next move is the human player
            nextMovePlayer = computerPlayer == Player.ONE ? Player.TWO : Player.ONE;
        }

        List<Integer> openCells = getOpenCells(board);

        for (int cell : openCells){
            Cell[] newBoard = getBoardForMove(board, cell, nextMovePlayer);
            if (isComputer){
                best = Math.max(best, minimax(newBoard, depth+1, !isComputer));
            } else {
                best = Math.min(best, minimax(newBoard, depth+1, !isComputer));
            }
        }

        return best;
    }

    private boolean hasOpenCells(Cell[] board){
        for (int i = 0; i < board.length; i++){
            if (board[i] == Cell.EMPTY) return true;
        }
        return false;
    }

    // TODO This is a lot of duplicated(ish) code. Should probably fix...
    private int score(Cell[] board){
        Cell computerCell = cellForPlayer(computerPlayer);
        Cell playerCell = computerCell == Cell.X ? Cell.O : Cell.X;
        int lowScore = Integer.MIN_VALUE;
        int highScore = Integer.MAX_VALUE;

        // Horizontal scan
        Cell prevCell = null;
        boolean win = false;
        outerloop:
        for (int i = 0; i < gridSize * gridSize; i += gridSize){
            // If the first row does not have N in a row, the loop breaks, ignoring the other N-1 rows.
            prevCell = null;
            boolean winInCol = true;
            for (int j = i; j < i + gridSize; j++){
                if (prevCell == null) {
                    prevCell = board[j];
                } else {
                    if (prevCell == Cell.EMPTY || prevCell != board[j]) {
                        winInCol = false;
                        break;
                    } else {
                        prevCell = board[j];
                    }
                }
            }
            if (winInCol){
                win = true;
                break;
            }
        }
        if (win) {
            //System.out.println("Win Detected!: " + Arrays.toString(board));
            return prevCell == computerCell ? highScore : lowScore;
        }

        // Vertical scan
        win = false;
        outerloop:
        for (int i = 0; i < gridSize; i++){
            prevCell = null;
            boolean winInRow = true;
            for (int j = i; j < gridSize * gridSize; j += gridSize){
                if (prevCell == null) {
                    prevCell = board[j];
                } else {
                    if (prevCell == Cell.EMPTY || prevCell != board[j]) {
                        winInRow = false;
                        break;
                    } else {
                        prevCell = board[j];
                    }
                }
            }
            if (winInRow){
                win = true;
                break;
            }
        }
        if (win) {

            return prevCell == computerCell ? highScore : lowScore;
        }

        // Diagonal down right scan
        prevCell = null;
        win = true;
        for (int i = 0; i < gridSize * gridSize; i += gridSize + 1){

            if (prevCell != null && board[i] != prevCell) {
                win = false;
                break;
            } else {
                prevCell = board[i];
            }

        }
        if (win) {
            return prevCell == computerCell ? highScore : lowScore;
        }

        // Diagonal down left scan
        prevCell = null;
        win = true;
        for (int i = gridSize - 1; i < gridSize * gridSize - 1; i += gridSize - 1){

            if (prevCell != null && board[i] != prevCell) {
                win = false;
                break;
            } else {
                prevCell = board[i];
            }

        }
        if (win) {
            return prevCell == computerCell ? highScore : lowScore;
        }

        return 0;
    }

}
