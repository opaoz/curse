package ru.opa.pack.server;

import ru.opa.pack.controllers.RequestManager;
import ru.opa.pack.views.ServerUI;

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
    private ServerUI ui;

    protected ClientThread(Socket socket, ServerUI ui) throws IOException {
        this.socket = socket;
        this.inputStream = socket.getInputStream();
        this.outputStream = socket.getOutputStream();
        this.ui = ui;
        new Thread(this).start();
    }

    public void run() {
        try {
            readInputHeaders();
            if (!request.equals("none")) {
                writeResponse(new RequestManager(request).toString());
            }
        } catch (IOException t) {
            ui.println("Socket error");
            ui.println(t.toString());
        } finally {
            try {
                socket.close();
            } catch (Throwable t) {
                ui.println("Socket closing error");
            }
        }
        ui.println("Client processing finished");
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
        ui.println(request);
    }
}
