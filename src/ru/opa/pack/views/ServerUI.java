package ru.opa.pack.views;

import ru.opa.pack.views.components.Log;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class ServerUI extends JFrame {
    private static final long serialVersionUID = 6182239340196405650L;
    private Log log;

    public ServerUI() {
        log = new Log();
        //this.setPreferredSize(new Dimension(500, 250));

        this.setLayout(new FlowLayout());
        this.add(log);
        this.setTitle("Server");
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.pack();
        this.setVisible(true);
    }

    public void println(String message) {
        log.info(message);
    }
}
