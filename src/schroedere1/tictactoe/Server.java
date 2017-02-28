package schroedere1.tictactoe;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * Created by edge on 2/24/17.
 */
public class Server {

    private int port;
    private BufferedReader in;
    private PrintWriter out;

    enum MessageType {
        WIN, TIE, LOSS, DEFAULT
    }

    public Server(int port){
        this.port = port;
    }

    public void start(){

        try (ServerSocket serverSocket = new ServerSocket(port)) {

            while (true){
                Socket socket = serverSocket.accept();
                playTicTacToe(socket);
            }

        } catch (IOException ex) {
            ex.printStackTrace();
        }

    }

    private void playTicTacToe(Socket socket) throws IOException {

        in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        out = new PrintWriter(socket.getOutputStream());

        // 50/50 chance computer goes first
        boolean isComputerFirst = Math.random() > .5;
        TripleT game = new TripleT(3, isComputerFirst);

        // If the computer goes first, get it's move and send it.
        if (isComputerFirst){
            GridIndex compMove = game.getComputerMove();
            sendMove(compMove.row, compMove.col, MessageType.DEFAULT);
        } else {
            out.println("NONE");
        }

        GridIndex userMove;
        GridIndex computerMove;
        boolean userLastMove = false;
        while (game.isActive()){

            // Get user input
            boolean isValidMove = false;
            while (!isValidMove){
                String line = in.readLine();
                userMove = parseUserMove(line);
                if (userMove != null){
                    isValidMove = game.makeHumanMove(userMove.row, userMove.col);
                }
            }

            // Check if that move won the game
            if (!game.isActive()){
                if (game.getWinner() == null) // Null means no winner/tie
                    sendMove(0, 0, MessageType.TIE);
                else // If it wasnt a tie, the player just won
                    sendMove(0, 0, MessageType.WIN);

                break;
            }

            // Get the computer's move
            computerMove = game.getComputerMove();
            if (computerMove != null) {

                if (game.isActive()) {
                    sendMove(computerMove.row, computerMove.col, MessageType.DEFAULT);
                } else {
                    if (game.getWinner() == null) // Null means no winner/tie
                        sendMove(computerMove.row, computerMove.col, MessageType.TIE);
                    else // It it wasn't a tie, the computer just won.
                        sendMove(computerMove.row, computerMove.col, MessageType.LOSS);
                }

            } else {
                System.out.println("You done messed up A-Aron!");
            }

            in.close();
            out.close();
        }

    }

    private GridIndex parseUserMove(String input){
        // Split on a space. "MOVE 2 2"
        String[] splitMove = input.split(" ");

        // If the message does not match the protocol, return null
        if (splitMove.length < 3 || splitMove.length > 3) return null;
        if (!splitMove[0].equals("MOVE")) return null;

        int row, col;

        try {

            row = Integer.parseInt(splitMove[1]);
            col = Integer.parseInt(splitMove[2]);

        } catch (NumberFormatException ex){
            // Numbers couldn't be parsed, so return null for invalid input
            return null;
        }

        return new GridIndex(row, col);

    }

    private void sendMove(int row, int col, MessageType type) {
        // MessageType indicates if the message is a WIN/LOSS/TIE or normal move message
        String suffix = type == MessageType.DEFAULT ? "" : " " + type.toString();

        out.println("MOVE " + row + " " + col + suffix);

    }

    public static void main(String[] args) {
        Server server = new Server(7788);
        server.start();
    }

}
