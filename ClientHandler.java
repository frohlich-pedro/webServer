import java.io.*;
import java.net.Socket;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class ClientHandler implements Runnable {
    private final Socket clientSocket;
    private final String wwwDir;

    public ClientHandler(Socket clientSocket, String wwwDir) {
        this.clientSocket = clientSocket;
        this.wwwDir = wwwDir;
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

                switch (method) {
                    case "GET":
                        handleGetRequest(out, path);
                        break;
                    case "POST":
                        handlePostRequest(in, out, path);
                        break;
                    case "PUT":
                        handlePutRequest(in, out, path);
                        break;
                    case "DELETE":
                        handleDeleteRequest(out, path);
                        break;
                    default:
                        sendNotFound(out);
                        break;
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

    private void handleGetRequest(OutputStream out, String path) {
        try {
            if (path.equals("/")) {
                path = "/index.html";
            }
            String filePath = wwwDir + path;
            Path path1 = Paths.get(filePath);
            if (Files.exists(path1)) {
                String contentType = Files.probeContentType(path1);
                serveFile(out, filePath, contentType);
            } else {
                sendNotFound(out);
            }
        } catch (IOException e) {
            System.err.println(e.getMessage());
        }
    }

    private void handlePostRequest(BufferedReader in, OutputStream out, String path) {
        try {
            StringBuilder payload = new StringBuilder();
            String line;
            while ((line = in.readLine()) != null && !line.isEmpty()) {
                payload.append(line).append("\n");
            }

            String response = "HTTP/1.1 200 OK\r\n" +
                    "Content-Type: text/plain\r\n" +
                    "Connection: close\r\n" +
                    "\r\n" +
                    "POST request to " + path + " received. Payload: " + payload;
            out.write(response.getBytes());
            out.flush();
        } catch (IOException e) {
            System.err.println(e.getMessage());
        }
    }

    private void handlePutRequest(BufferedReader in, OutputStream out, String path) {
        try {
            StringBuilder payload = new StringBuilder();
            String line;
            while ((line = in.readLine()) != null && !line.isEmpty()) {
                payload.append(line).append("\n");
            }

            String response = "HTTP/1.1 200 OK\r\n" +
                    "Content-Type: text/plain\r\n" +
                    "Connection: close\r\n" +
                    "\r\n" +
                    "PUT request to " + path + " received. Payload: " + payload;
            out.write(response.getBytes());
            out.flush();
        } catch (IOException e) {
            System.err.println(e.getMessage());
        }
    }

    private void handleDeleteRequest(OutputStream out, String path) {
        try {
            String response = "HTTP/1.1 200 OK\r\n" +
                    "Content-Type: text/plain\r\n" +
                    "Connection: close\r\n" +
                    "\r\n" +
                    "DELETE request to " + path + " received.";
            out.write(response.getBytes());
            out.flush();
        } catch (IOException e) {
            System.err.println(e.getMessage());
        }
    }

    private void serveFile(OutputStream out, String filePath, String contentType) {
        try {
            byte[] fileContent = Files.readAllBytes(Paths.get(filePath));
            String response = "HTTP/1.1 200 OK\r\n" +
                    "Content-Type: " + contentType + "\r\n" +
                    "Content-Length: " + fileContent.length + "\r\n" +
                    "Connection: close\r\n" +
                    "\r\n";
            out.write(response.getBytes());
            out.write(fileContent);
            out.flush();
        } catch (IOException e) {
            System.err.println(e.getMessage());
        }
    }

    private void sendNotFound(OutputStream out) {
        try {
            String response = "HTTP/1.1 404 Not Found\r\n" +
                    "Content-Type: text/html\r\n" +
                    "Connection: close\r\n" +
                    "\r\n" +
                    "<html>404 not found</html>";
            out.write(response.getBytes());
            out.flush();
        } catch (IOException e) {
            System.err.println(e.getMessage());
        }
    }
}
