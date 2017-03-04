package schroedere1.tictactoe;

/**
 * E.J. Schroeder
 * Andy Gergel
 *
 * Assn #1 - Client-Server TicTacToe
 * CSC 460
 *
 * Simple immutable data class to store an index of a 2D array.
 */
class GridIndex {
    final int row;
    final int col;

    public GridIndex(int row, int col){
        this.row = row;
        this.col = col;
    }
}