package ru.opa.pack.views.components;

import ru.opa.pack.references.References;

import javax.swing.*;
import java.awt.*;
import java.text.DateFormat;
import java.util.Date;

public class Log extends JPanel {
    private static final long serialVersionUID = -7968405633747657642L;
    private JTextArea text;
    private DateFormat dateFormat = References.DATE_FORMAT;


    public Log() {
        text = new JTextArea();
        text.setAlignmentX(LEFT_ALIGNMENT);
        text.setAlignmentY(TOP_ALIGNMENT);
        text.setBackground(Color.BLACK);
        text.setForeground(Color.WHITE);
        text.setFont(References.LOG_FONT);
        text.setLineWrap(true);
        text.setWrapStyleWord(true);

        JScrollPane scrollPane = new JScrollPane(text);
        scrollPane.setPreferredSize(new Dimension(500, 200));
        this.add(scrollPane);
        this.setVisible(true);
    }


    public void info(String message) {
        text.append("[" + dateFormat.format(new Date()) + "] " + message + " \n");
    }


    @Override
    public Dimension getPreferredSize() {
        return new Dimension(500, 200);
    }
}
