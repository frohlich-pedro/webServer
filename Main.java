import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import javax.swing.*;
import javax.swing.plaf.FontUIResource;

public class Main {
    private static final short PORT = 8080;
    private static final String WWW_DIR = "www";
    private static final int FRAME_WIDTH = 320;
    private static final int FRAME_HEIGHT = 240;
    private static final int BUTTON_WIDTH = 200;
    private static final int BUTTON_HEIGHT = 50;

    private static JFrame frame;
    private static JButton button0;
    private static JButton button1;
    private static boolean running = false;

    public static void main(String[] args) {
        frame = new JFrame("Hello, World!");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setResizable(false);
        frame.setSize(FRAME_WIDTH, FRAME_HEIGHT);
        frame.setLayout(null);

        button0 = new JButton("Start Server");
        button0.setBounds(FRAME_WIDTH / 2 - BUTTON_WIDTH / 2, FRAME_HEIGHT / 2 - BUTTON_HEIGHT / 2 - 45, BUTTON_WIDTH, BUTTON_HEIGHT);
        button0.addActionListener(e -> startServer());
        button0.setFont(new FontUIResource("arial", 1, 15));
        frame.add(button0);

        button1 = new JButton("Stop Server");
        button1.setBounds(FRAME_WIDTH / 2 - BUTTON_WIDTH / 2, FRAME_HEIGHT / 2 - BUTTON_HEIGHT / 2 + 15, BUTTON_WIDTH, BUTTON_HEIGHT);
        button1.addActionListener(e -> stopServer());
        button1.setFont(new FontUIResource("arial", 1, 15));
        frame.add(button1);

        frame.setVisible(true);
    }

    private static void startServer() {
        running = true;
        new Thread(() -> {
            try (ServerSocket serverSocket = new ServerSocket(PORT)) {
                System.out.println("Server started on port " + PORT);
                while (running) {
                    try {
                        Socket clientSocket = serverSocket.accept();
                        ClientHandler clientHandler = new ClientHandler(clientSocket, WWW_DIR);
                        Thread thread = new Thread(clientHandler);
                        thread.start();
                    } catch (IOException e) {
                        if (running) {
                            System.err.println("Error accepting client connection: " + e.getMessage());
                        }
                    }
                }
            } catch (IOException e) {
                System.err.println("Could not start server: " + e.getMessage());
            }
        }).start();
    }

    private static void stopServer() {
        running = false;
        System.out.println("Server stopped.");
    }
}
