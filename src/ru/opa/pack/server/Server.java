package ru.opa.pack.server;

import ru.opa.pack.controllers.RequestManager;
import ru.opa.pack.references.References;
import ru.opa.pack.views.ServerUI;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * Created by Vladimir_Levin on 11.10.2015.
 */
public class Server {
    private ServerSocket serverSocket;
    private ServerUI ui;

    public Server(int port, ServerUI ui) throws IOException {
        if (port < 0 || port > 65535) {
            port = References.DEFAULT_SERVER_PORT;
            ui.println("Server port changed to default value - " + port);
        }

        this.ui = ui;
        serverSocket = new ServerSocket(port);
        ui.println("Server started...");
        RequestManager.sayHello();
    }

    public void start() throws IOException {
        while (true) {
            Socket socket = serverSocket.accept();
            //ui.println("Client connected with " + socket.getLocalSocketAddress());
            new ClientThread(socket, ui);
        }
    }
}
