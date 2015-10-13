package ru.opa.pack.views;

import ru.opa.pack.views.components.Log;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class ServerUI extends JFrame implements ActionListener {
	private static final long serialVersionUID = 6182239340196405650L;
	private Log log;
	private JButton button;

	public ServerUI() {
		log = new Log();
		this.setPreferredSize(new Dimension(500, 200));
		
		button = new JButton("Exit");
		button.setAlignmentX(RIGHT_ALIGNMENT);
		button.setAlignmentY(BOTTOM_ALIGNMENT);
		button.addActionListener(this);

		this.setLayout(new FlowLayout());
		this.add(log);
		this.add(button);

		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		//this.setResizable(false);
		this.pack();
		this.setVisible(true);
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		this.dispose();
		Thread.currentThread().interrupt();
		System.exit(-1);
	}

	public void println(String message){
		log.info(message);
	}
}
