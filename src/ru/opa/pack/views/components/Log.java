package ru.opa.pack.views.components;

import ru.opa.pack.references.References;

import javax.swing.*;
import java.awt.*;
import java.text.DateFormat;
import java.util.Date;

public class Log extends JPanel {
    private static final long serialVersionUID = -7968405633747657642L;
    private JTextArea text;
    private JScrollPane scroll;
    private DateFormat dateFormat = References.DATE_FORMAT;


    public Log() {
        text = new JTextArea();
        text.setAlignmentX(LEFT_ALIGNMENT);
        text.setAlignmentY(TOP_ALIGNMENT);
        text.setPreferredSize(getPreferredSize());
        text.setBackground(Color.BLACK);
        text.setForeground(Color.WHITE);
        text.setFont(References.LOG_FONT);

        scroll = new JScrollPane(text);
        scroll.setBounds(10, 60, 780, 500);
        scroll.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);

        this.add(text);
        this.add(scroll);
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
