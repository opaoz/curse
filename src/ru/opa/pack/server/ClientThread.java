package ru.opa.pack.server;

import ru.opa.pack.controllers.RequestManager;

import java.io.*;
import java.net.Socket;

/**
 * Created by Vladimir_Levin on 11.10.2015.
 */
public class ClientThread implements Runnable {
    private Socket socket;
    private InputStream inputStream;
    private OutputStream outputStream;
    private String request;

    protected ClientThread(Socket socket) throws IOException {
        this.socket = socket;
        this.inputStream = socket.getInputStream();
        this.outputStream = socket.getOutputStream();
        new Thread(this).start();
    }

    public void run() {
        try {
            readInputHeaders();
            if (!request.equals("none")) {
                writeResponse(new RequestManager(request).toString());
            }
        } catch (IOException t) {
            System.out.println("Socket error");
            System.out.println(t);
        } finally {
            try {
                socket.close();
            } catch (Throwable t) {
                System.out.println("Socket closing error");
            }
        }
        System.err.println("Client processing finished");
    }

    private void writeResponse(String message) throws IOException {
        String response = "HTTP/1.1 200 OK\r\n" +
                "Server: YarServer/2009-09-09\r\n" +
                "Content-Type: text/json\r\n" +
                "Content-Length: " + message.length() + "\r\n" +
                "Access-Control-Allow-Origin: *\r\n" +
                "Connection: close\r\n\r\n";
        String result = response + message;

        outputStream.write(result.getBytes());
        outputStream.flush();
    }

    private void readInputHeaders() throws IOException {
        BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        String line = in.readLine();
        if (line == null) {
            in.close();
            request = "none";
            return;
        }
        StringBuilder headers = new StringBuilder();
        StringBuilder body = new StringBuilder();
        boolean isPost = line.startsWith("POST");
        int contentLength = 0;

        headers.append("" + line);

        while (!(line = in.readLine()).equals("")) {
            headers.append('\n' + line);

            if (isPost) {
                String contentHeader = "Content-Length: ";
                if (line.startsWith(contentHeader)) {
                    contentLength = Integer.parseInt(line.substring(contentHeader.length()));
                }
            }
        }

        if (isPost) {
            int c;
            for (int i = 0; i < contentLength; i++) {
                c = in.read();
                body.append((char) c);
            }
        }

        request = body.toString();
        System.out.println(request);
    }
}
