package com.webs.kevkawmcode.GraphGenerator;

import java.awt.Color;
import java.awt.Graphics;

import javax.swing.JPanel;

public class Display extends JPanel implements Runnable {
	private static final long serialVersionUID = 1L;

	Frame frame;
	
	Thread thread = new Thread(this);
	
	public Display(Frame frame){
		this.frame = frame;
		
		thread.start();
	}
	
	@Override
	public void run() {
		while(true){
			repaint();
		}
	}

	@Override
	public void paintComponent(Graphics g){
		g.clearRect(0, 0, frame.getWidth(), frame.getHeight());
		g.setColor(Color.RED);
		for(int i = 0; i < 25; i++){
			g.drawLine((frame.getWidth() / 24) * i, 0, (frame.getWidth() / 24) * i, frame.getHeight());
		}
		/*for(int i = 0; i < 18; i++){
			g.drawLine(0,(frame.getHeight() / 18) * (i + 1) - 15, frame.getWidth(), (frame.getHeight() / 18) * (i + 1) - 15);
		}*/
		g.setColor(Color.BLACK);
		g.fillRect(800 / 2 - 3, 0, 6, 630);
		g.fillRect(0, 600 / 2 - 3, 800, 6);
	}
	
}
