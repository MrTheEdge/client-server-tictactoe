package schroedere1.tictactoe;

import java.util.EventObject;

/**
 * Created by edge on 3/3/17.
 */
public class MoveReceivedEvent extends EventObject {

    private final int col;
    private final int row;

    /**
     * Constructs a prototypical Event.
     *
     * @param source The object on which the Event initially occurred.
     * @throws IllegalArgumentException if source is null.
     */
    public MoveReceivedEvent(Object source, int row, int col) {
        super(source);
        this.row = row;
        this.col = col;
    }

    public int getRow() {
        return row;
    }

    public int getCol() {
        return col;
    }
}
