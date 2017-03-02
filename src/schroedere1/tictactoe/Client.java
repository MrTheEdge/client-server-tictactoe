package schroedere1.tictactoe;

import javax.swing.*;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

/**
 * Created by edge on 2/27/17.
 */
public class Client{

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

    public static void main(String[] args) throws IOException{
        PrintWriter out;
        BufferedReader stdInput;
        BufferedReader input;
        String[] splitMove;

        int gridSize = 3;
        char[][] board = new char[gridSize][gridSize];

        for (int i = 0; i < board.length; i++){
            for (int j = 0; j < board[i].length; j++){
                board[i][j] = ' ';
            }
        }

        try(Socket s = new Socket("localhost", 7788);) //put this in a try catch block!
        {
            out = new PrintWriter(s.getOutputStream(),true);
            stdInput = new BufferedReader(new InputStreamReader(System.in));
            input = new BufferedReader(new InputStreamReader(s.getInputStream()));
            String userInput;
            while (true) {
                String serverResponse = input.readLine();
                splitMove = serverResponse.split(" ");
                if (splitMove[0].equals("MOVE")&& !splitMove[1].equals("-1")) {
                    board[Integer.parseInt(splitMove[1])][Integer.parseInt(splitMove[2])] = 'X';
                }


                System.out.println(serverResponse);

                printBoard(board);

                if(splitMove.length == 4){
                    break;
                }

                userInput = stdInput.readLine();
                out.println(userInput);
                splitMove = userInput.split(" ");
                if (splitMove[0].equals("MOVE") && !splitMove[1].equals("-1")){
                    board[Integer.parseInt(splitMove[1])][Integer.parseInt(splitMove[2])] = 'O';
                }

            }
            if (splitMove[3].equals("TIE")){
                System.out.println("The game ended in a tie");
            }
            else if(splitMove[3].equals("LOSS")){
                System.out.println("You lost to a computer");
            }
            else if(splitMove[3].equals("WIN")){
                System.out.println("You cheated");
            }
        }
    }

}
