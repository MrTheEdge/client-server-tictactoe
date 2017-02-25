package schroedere1.tictactoe;

import java.util.Scanner;

/**
 * Created by edge on 2/24/17.
 */
public class Server {


    public static void printBoard(char[][] board){
        for (int i = 0; i < board.length; i++){
            for (int j = 0; j < board[i].length; j++){
                System.out.print(" " + board[i][j] + " ");
                if (j < board[i].length - 1){
                    System.out.print("|");
                }
            }
            System.out.println();
            if (i < board.length - 1) {
                System.out.println(new String(new char[3 * 3 + (3 - 1)]).replace("\0", "-"));
            }
        }
        System.out.println();
    }

    public static void main(String[] args){

        int gridSize = 3;
        char[][] board = new char[gridSize][gridSize];

        for (int i = 0; i < board.length; i++){
            for (int j = 0; j < board[i].length; j++){
                board[i][j] = ' ';
            }
        }

        TripleT game = new TripleT(gridSize, false);
        printBoard(board);

        Scanner in = new Scanner(System.in);
        while (game.isActive()){

            String[] vals = in.nextLine().split(" ");
            int row = Integer.parseInt(vals[0]);
            int col = Integer.parseInt(vals[1]);

            game.makeHumanMove(row, col);
            board[row][col] = 'X';

            if (!game.isActive()){
                printBoard(board);
                break;
            }

            GridIndex index = game.getComputerMove();

            board[index.row][index.col] = 'O';

            printBoard(board);
        }

        if (game.getCurrentState() == TripleT.State.O_WON){
            System.out.println("O WON!");
        } else if (game.getCurrentState() == TripleT.State.X_WON){
            System.out.println("X WON!");
        } else {
            System.out.println("THERE WAS A DRAW!");
        }

    }
}
