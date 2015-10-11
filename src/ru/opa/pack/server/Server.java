package ru.opa.pack.server;

import ru.opa.pack.references.References;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * Created by Vladimir_Levin on 11.10.2015.
 */
public class Server {
    private ServerSocket serverSocket;

    public Server(int port) throws IOException {
        if (port < 0 || port > 60000) {
            port = References.DEFAULT_SERVER_PORT;
            System.out.println("Server port changet to default value - " + port);
        }
        serverSocket = new ServerSocket(port);
    }

    public void accept() throws IOException {
        while (true) {
            Socket socket = serverSocket.accept();
            System.out.println("Client connected with " + socket.getLocalSocketAddress());
            new ClientThread(socket);
        }
    }
}
