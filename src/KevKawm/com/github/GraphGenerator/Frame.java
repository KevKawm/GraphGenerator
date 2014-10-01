package KevKawm.com.github.GraphGenerator;

import javax.swing.JFrame;

public class Frame extends JFrame{
	private static final long serialVersionUID = 1L;

	public static void main(String[] args) {
		new Frame();
	}

	public Frame(){
		new JFrame();
		
		Display display = new Display(this);
		this.add(display);

		this.setUndecorated(true);
		this.setExtendedState(MAXIMIZED_BOTH);
		this.setSize(800,630);
		this.setResizable(false);
		this.setVisible(true);
		this.setLocationRelativeTo(null);
		this.setTitle("Graph Generator");
		this.setDefaultCloseOperation(EXIT_ON_CLOSE);
	}
	
}
