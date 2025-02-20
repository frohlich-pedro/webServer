import java.io.*;
import java.net.Socket;

public class ClientHandler implements Runnable {
    private final Socket clientSocket;
    private final String pathToHtml;

    public ClientHandler(Socket clientSocket, String pathToHtml) {
        this.clientSocket = clientSocket;
        this.pathToHtml = pathToHtml;
    }

    @Override
    public void run() {
        try (BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
             OutputStream out = clientSocket.getOutputStream()) {

            String requestLine = in.readLine();
            if (requestLine != null) {
                System.out.println("Request received: " + requestLine);

                String headerLine;
                while (!(headerLine = in.readLine()).isEmpty()) {
                    System.out.println(headerLine);
                }

                String[] requestParts = requestLine.split(" ");
                String method = requestParts[0];
                String path = requestParts[1];

                if (method.equals("GET") && path.equals("/")) {
                    StringBuilder contentBuilder = new StringBuilder();
                    try (BufferedReader br = new BufferedReader(new FileReader(pathToHtml))) {
                        String currentLine;
                        while ((currentLine = br.readLine()) != null) {
                            contentBuilder.append(currentLine).append("\n");
                        }
                    } catch (IOException e) {
                        System.err.println(e.getMessage());
                    }

                    String htmlContent = contentBuilder.toString();
                    String response = """
                            HTTP/1.1 200 OK\r
                            Content-Type: text/html\r
                            Connection: close\r
                            \r
                            """ +
                            htmlContent;

                    out.write(response.getBytes());
                    out.flush();
                } else {
                    String response = """
                            HTTP/1.1 404 Not Found\r
                            Content-Type: text/html\r
                            Connection: close\r
                            \r
                            <html><h1>404 not found</h1></html>
                            """;

                    out.write(response.getBytes());
                    out.flush();
                }
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
