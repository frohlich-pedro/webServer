import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class ResponseHandler {

    public static void handleGetRequest(OutputStream out, String path, String wwwDir) throws IOException {
        if (path.equals("/")) {
            path = "/index.html";
        }
        String filePath = wwwDir + path;
        Path filePathObj = Paths.get(filePath);
        if (Files.exists(filePathObj)) {
            String contentType = Files.probeContentType(filePathObj);
            serveFile(out, filePath, contentType);
        } else {
            sendNotFound(out);
        }
    }

    public static void handlePostRequest(OutputStream out, String path, String payload) throws IOException {
        String response = "HTTP/1.1 200 OK\r\n" +
                "Content-Type: text/plain\r\n" +
                "Connection: close\r\n" +
                "\r\n" +
                "POST request to " + path + " received. Payload: " + payload;
        out.write(response.getBytes());
        out.flush();
    }

    public static void handlePutRequest(OutputStream out, String path, String payload) throws IOException {
        String response = "HTTP/1.1 200 OK\r\n" +
                "Content-Type: text/plain\r\n" +
                "Connection: close\r\n" +
                "\r\n" +
                "PUT request to " + path + " received. Payload: " + payload;
        out.write(response.getBytes());
        out.flush();
    }

    public static void handleDeleteRequest(OutputStream out, String path) throws IOException {
        String response = "HTTP/1.1 200 OK\r\n" +
                "Content-Type: text/plain\r\n" +
                "Connection: close\r\n" +
                "\r\n" +
                "DELETE request to " + path + " received.";
        out.write(response.getBytes());
        out.flush();
    }

    public static void handlePatchRequest(OutputStream out, String path, String payload) throws IOException {
        String response = "HTTP/1.1 200 OK\r\n" +
                "Content-Type: text/plain\r\n" +
                "Connection: close\r\n" +
                "\r\n" +
                "PATCH request to " + path + " received. Payload: " + payload;
        out.write(response.getBytes());
        out.flush();
    }

    public static void handleHeadRequest(OutputStream out, String path, String wwwDir) throws IOException {
        if (path.equals("/")) {
            path = "/index.html";
        }
        String filePath = wwwDir + path;
        Path filePathObj = Paths.get(filePath);
        if (Files.exists(filePathObj)) {
            String contentType = Files.probeContentType(filePathObj);
            sendHeadResponse(out, filePath, contentType);
        } else {
            sendNotFound(out);
        }
    }

    private static void serveFile(OutputStream out, String filePath, String contentType) throws IOException {
        File file = new File(filePath);
        byte[] fileBytes = Files.readAllBytes(file.toPath());
        String response = "HTTP/1.1 200 OK\r\n" +
                "Content-Type: " + contentType + "\r\n" +
                "Content-Length: " + fileBytes.length + "\r\n" +
                "Connection: close\r\n" +
                "\r\n";
        out.write(response.getBytes());
        out.write(fileBytes);
        out.flush();
    }

    private static void sendHeadResponse(OutputStream out, String filePath, String contentType) throws IOException {
        File file = new File(filePath);
        byte[] fileBytes = Files.readAllBytes(file.toPath());
        String response = "HTTP/1.1 200 OK\r\n" +
                "Content-Type: " + contentType + "\r\n" +
                "Content-Length: " + fileBytes.length + "\r\n" +
                "Connection: close\r\n" +
                "\r\n";
        out.write(response.getBytes());
        out.flush();
    }

    public static void sendNotFound(OutputStream out) throws IOException {
        String response = "HTTP/1.1 404 Not Found\r\n" +
                "Content-Type: text/plain\r\n" +
                "Connection: close\r\n" +
                "\r\n" +
                "404 Not Found";
        out.write(response.getBytes());
        out.flush();
    }
}
