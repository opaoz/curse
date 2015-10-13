package ru.opa.pack;

import ru.opa.pack.references.References;
import ru.opa.pack.server.Server;
import ru.opa.pack.views.ServerUI;

import java.io.IOException;

/**
 * Created by Vladimir_Levin on 11.10.2015.
 */
public class Main {
    public static ServerUI ui = new ServerUI();

    public static void main(String[] args) {
        try {
            ui.setVisible(true);
            new Server(References.SERVER_PORT, ui).start();
        } catch (IOException e) {
            ui.println("Server crashed" + e);
        }
    }
}
