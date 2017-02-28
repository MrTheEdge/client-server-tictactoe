package schroedere1.tictactoe;

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