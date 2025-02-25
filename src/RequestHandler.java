package src;

import java.io.*;

public class RequestHandler {
    private final BufferedReader in;
    private final OutputStream out;
    private final String requestLine;
    private final String wwwDir;

    public RequestHandler(BufferedReader in, OutputStream out, String requestLine, String wwwDir) {
        this.in = in;
        this.out = out;
        this.requestLine = requestLine;
        this.wwwDir = wwwDir;
    }

    public void handleRequest() {
        try {
            String[] requestParts = requestLine.split(" ");
            String method = requestParts[0];
            String path = requestParts[1];
    
            switch (method) {
                case "GET":
                    handleGetRequest(path);
                    break;
                case "POST":
                    handlePostRequest(path);
                    break;
                case "PUT":
                    handlePutRequest(path);
                    break;
                case "DELETE":
                    handleDeleteRequest(path);
                    break;
                case "PATCH":
                    handlePatchRequest(path);
                    break;
                case "HEAD":
                    handleHeadRequest(path);
                    break;
                default:
                    ResponseHandler.sendNotFound(out);
                    break;
            }
        } catch (IOException e) {
            System.err.println(e.getMessage());
        }
    }

    private void handleGetRequest(String path) throws IOException {
        ResponseHandler.handleGetRequest(out, path, wwwDir);
    }

    private void handlePostRequest(String path) throws IOException {
        String payload = readPayload();
        ResponseHandler.handlePostRequest(out, path, payload);
    }

    private void handlePutRequest(String path) throws IOException {
        String payload = readPayload();
        ResponseHandler.handlePutRequest(out, path, payload);
    }

    private void handleDeleteRequest(String path) throws IOException {
        ResponseHandler.handleDeleteRequest(out, path);
    }

    private void handlePatchRequest(String path) throws IOException {
        String payload = readPayload();
        ResponseHandler.handlePatchRequest(out, path, payload);
    }

    private void handleHeadRequest(String path) throws IOException {
        ResponseHandler.handleHeadRequest(out, path, wwwDir);
    }

    private String readPayload() throws IOException {
        StringBuilder payload = new StringBuilder();
        String line;
        while ((line = in.readLine()) != null && !line.isEmpty()) {
            payload.append(line).append("\n");
        }
        return payload.toString();
    }
}