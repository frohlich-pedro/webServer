import java.io.*;
import java.net.Socket;

public class ClientHandler extends Thread {
    private final Socket clientSocket;
    private final String wwwDir;

    public ClientHandler(Socket clientSocket, String wwwDir) {
        this.clientSocket = clientSocket;
        this.wwwDir = wwwDir;
    }

    @Override
    public void run() {
        try {
            BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
            OutputStream out = clientSocket.getOutputStream();
            String requestLine = in.readLine();
            if (requestLine != null) {
                System.out.println("Request received: " + requestLine);

                String headerLine;
                while (!(headerLine = in.readLine()).isEmpty()) {
                    System.out.println(headerLine);
                }

                RequestHandler requestHandler = new RequestHandler(in, out, requestLine, wwwDir);
                requestHandler.handleRequest();
            }
        } catch (IOException e) {
            System.err.println(e.getMessage());
        } finally {
            try {
                clientSocket.close();
            } catch (IOException e) {
                System.err.println(e.getMessage());
            }
        }
    }
}
