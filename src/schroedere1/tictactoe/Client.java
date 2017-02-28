package schroedere1.tictactoe;

import java.util.Scanner;

/**
 * Created by edge on 2/27/17.
 */
public class Client {

    public static void printBoard(char[][] board){
        for (int i = 0; i < board.length; i++){
            System.out.print("  " + i + " ");
        }
        System.out.println();
        for (int i = 0; i < board.length; i++){
            System.out.print(i);
            for (int j = 0; j < board[i].length; j++){
                System.out.print(" " + board[i][j] + " ");
                if (j < board[i].length - 1){
                    System.out.print("|");
                }
            }
            System.out.println();
            if (i < board.length - 1) {
                System.out.print(" ");
                System.out.println(
                        new String(new char[3 * board.length + (board.length - 1)])
                                .replace("\0", "-"));
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



    }

}
