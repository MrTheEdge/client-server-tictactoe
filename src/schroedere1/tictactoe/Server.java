package schroedere1.tictactoe;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * E.J. Schroeder
 * Andy Gergel
 *
 * Assn #2 - Client-Server TicTacToe Multithreaded Version
 * CSC 460
 */
public class Server {

    private int port;
    private ExecutorService executor;

    public Server(int port, int sizeOfThreadPool){
        this.port = port;
        this.executor = Executors.newFixedThreadPool(sizeOfThreadPool);
    }

    public void start(){

        try (ServerSocket serverSocket = new ServerSocket(port)) {

            while (true){
                System.out.println("Accepting connections on port " + port);
                Socket socket = serverSocket.accept();
                System.out.println("Connected to: " + socket.getInetAddress());
                try {
                    executor.submit(new RunnableGame(socket));
                } catch (IOException ex){
                    // Nested try block will keep server running if the runnable task fails.
                    ex.printStackTrace();
                }
            }
        } catch (IOException ex) {
            ex.printStackTrace();
        }

    }



    public static void main(String[] args) {
        Server server = new Server(7788, 10);
        server.start();
    }

}
