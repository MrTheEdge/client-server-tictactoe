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

    private int port;
    private char[][] board;
    int gridSize = 3;

    PrintWriter out;
    BufferedReader stdInput;
    BufferedReader input;

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

    public Client(int port){
        this.port = port;
    }

    public void start() {

        board = new char[gridSize][gridSize];

        initBoard(board);

        try(Socket s = new Socket("localhost", this.port))
        {
            out = new PrintWriter(s.getOutputStream(),true);
            stdInput = new BufferedReader(new InputStreamReader(System.in));
            input = new BufferedReader(new InputStreamReader(s.getInputStream()));

            String userInput;

            // Get the first line from the server
            String serverLine = input.readLine();
            if (!serverLine.equals("NONE")) {
                parseServerInput(serverLine); // Discard returned state, it's the first move
            }
            printBoard(board);
            GameState recievedServerState;
            while (true) {
                System.out.print("Enter a move: ");
                userInput = stdInput.readLine();
                GridIndex userMove = formatUserInput(userInput);
                board[userMove.row][userMove.col] = 'O';
                out.println("MOVE " + userMove.row + " " + userMove.col);
                printBoard(board); // Possibly remove this first print.

                serverLine = input.readLine();
                recievedServerState = parseServerInput(serverLine);
                if (recievedServerState != GameState.RUNNING){
                    break;
                }
                printBoard(board);
            }

            if (recievedServerState == GameState.COMP_WIN){
                System.out.println("The computer won!");
            } else if (recievedServerState == GameState.HUMAN_WIN){
                System.out.println("You Won!");
            } else {
                System.out.println("There was a tie..");
            }

        }
        catch (IOException ex) {
            ex.printStackTrace();
            System.out.println("Sum ting wong.");
        }
    }

    private void initBoard(char[][] board) {
        for (int i = 0; i < board.length; i++){
            for (int j = 0; j < board[i].length; j++){
                board[i][j] = ' ';
            }
        }
    }

    public static void main(String[] args) throws IOException{
        Client client = new Client(7788);
        client.start();
    }

    private static GridIndex formatUserInput(String userInput) throws NumberFormatException {
        String[] splitInput = userInput.trim().split(" ");

        int row = Integer.parseInt(splitInput[0]);
        int col = Integer.parseInt(splitInput[1]);

        return new GridIndex(row, col);
    }

    private GameState parseServerInput(String input){
        String[] splitInput = input.split(" ");

        int row = Integer.parseInt(splitInput[1]);
        int col = Integer.parseInt(splitInput[2]);

        board[row][col] = 'X';

        if (splitInput.length > 3){
            if (splitInput[3].equals("WIN"))
                return GameState.HUMAN_WIN;
            if (splitInput[3].equals("LOSS"))
                return GameState.COMP_WIN;

            return GameState.TIE;

        } else {
            return GameState.RUNNING;
        }
    }

    enum GameState {
        RUNNING, COMP_WIN, HUMAN_WIN, TIE
    }

}
