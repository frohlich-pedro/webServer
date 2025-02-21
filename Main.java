import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;

public class Main {
    private static final short PORT = 3030;
    private static final String WWW_DIR = "C:\\Users\\pedro\\IdeaProjects\\untitled-1\\src\\www";

    public static void main(String[] args) {
        try (ServerSocket serverSocket = new ServerSocket(PORT)) {
            while (true) {
                try {
                    Socket clientSocket = serverSocket.accept();
                    ClientHandler clientHandler = new ClientHandler(clientSocket, WWW_DIR);
                    Thread thread = new Thread(clientHandler);
                    thread.start();
                } catch (IOException e) {
                    System.err.println(e.getMessage());
                }
            }
        } catch (IOException e) {
            System.err.println(e.getMessage());
        }
    }
}
