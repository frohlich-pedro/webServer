package src;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import javax.swing.*;
import javax.swing.plaf.FontUIResource;

public class Main {
    private static final short PORT = 8080;
    private static final String WWW_DIR = "src" + File.separator + "www";
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
        button0.addActionListener(e ->  {
            startServer();
            JOptionPane.showMessageDialog(frame, "Server Started at port: " + PORT);
        });
        button0.setFont(new FontUIResource("Comic Sans MS", 0, 18));
        frame.add(button0);

        button1 = new JButton("Stop Server");
        button1.setBounds(FRAME_WIDTH / 2 - BUTTON_WIDTH / 2, FRAME_HEIGHT / 2 - BUTTON_HEIGHT / 2 + 15, BUTTON_WIDTH, BUTTON_HEIGHT);
        button1.addActionListener(e -> {
            int response = JOptionPane.showConfirmDialog(frame, "Are you sure you want to close server?", ":(", JOptionPane.YES_NO_OPTION);
            if (response == JOptionPane.YES_OPTION) {
                stopServer();
                JOptionPane.showMessageDialog(frame, "Server Stopped");
            }
        });
        button1.setFont(new FontUIResource("Comic Sans MS", 0, 18));
        frame.add(button1);

        frame.setVisible(true);
    }

    private static void startServer() {
        running = true;
        new Thread(() -> {
            try (ServerSocket serverSocket = new ServerSocket(PORT)) {
                while (running) {
                    try {
                        Socket clientSocket = serverSocket.accept();
                        ClientHandler clientHandler = new ClientHandler(clientSocket, WWW_DIR);
                        Thread thread = new Thread(clientHandler);
                        thread.start();
                    } catch (IOException e) {
                        if (running) {
                            System.err.println(e.getMessage());
                        }
                    }
                }
            } catch (IOException e) {
                System.err.println(e.getMessage());
            }
        }).start();
    }

    private static void stopServer() {
        running = false;
    }
}