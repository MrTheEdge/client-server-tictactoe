package test;


import java.io.IOException;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Calendar;


/**
 * Created by edge on 2/21/17.
 */
public class Server {

    public static void main(String[] args){

        boolean isRunning = true;
        String receivedMessage;

        try (ServerSocket serverSocket = new ServerSocket(7788)) {
            while (true) {
                Socket socket = serverSocket.accept();
                System.out.println("Client connected");
                PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
                out.println(Calendar.getInstance().toString());
            }
        } catch (IOException ex) {
            System.out.println("Wow something happened.");
        }

    }

}
