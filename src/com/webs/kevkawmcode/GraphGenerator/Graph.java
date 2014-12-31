package com.webs.kevkawmcode.GraphGenerator;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.event.AdjustmentEvent;
import java.awt.event.AdjustmentListener;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.swing.JCheckBox;
import javax.swing.JPanel;
import javax.swing.JScrollBar;

import com.webs.kevkawmcode.Parser.Equation;

public class Graph extends JPanel implements AdjustmentListener {
	private static final long serialVersionUID = 1L;
	Dimension interval;
	public int zoom;
	Point pos;
	Frame frame;

	List<BufferedImage> equationImgs;

	public Graph(Dimension interval, int zoom, Frame frame) {
		this.interval = interval;
		this.zoom = zoom;
		this.pos = new Point(0, 0);
		this.frame = frame;
		equationImgs = new ArrayList<BufferedImage>();
	}

	public void addEquation(Equation eq, Color color) {
		BufferedImage img = new BufferedImage(800, 800, BufferedImage.TRANSLUCENT);
		Graphics g = img.getGraphics();
		frame.contentPane.add(frame.progressBar);
		double prevY = 0;
		for (int x = 0; x < 800; x++) {
			double xP = (((double) x - 400) / 20) * interval.width;
			frame.progressBar.setValue(x / 8);
			frame.progressBar.paint(frame.progressBar.getGraphics());
			for (int y = 0; y < 800; y++) {
				double yP = (((double) (800 - y) - 400) / 20) * interval.height;
				HashMap<String, String> values = new HashMap<String, String>();
				values.put("x", xP + "");
				values.put("y", yP + "");
				double r = Math.sqrt((xP * xP) + (yP * yP));
				values.put("r", r + "");
				values.put("t", FindAngle(r, xP, yP) + "");
				double diff = Math.abs(eq.getLeft(values) - eq.getRight(values));
				g.setColor(color);
				if (y > 1) {
					if (diff <= ((double) interval.height) / 20) {
						if (prevY != 0) {
							g.drawLine(x - 1, (int) prevY, x, y);
						}
						prevY = y;
					}
				}
			}
		}
		equationImgs.add(img);
		frame.contentPane.remove(frame.progressBar);
	}
	
	public double FindAngle(double r, double x, double y) {
		double cos = x / r;
		return y < 0 ? (2 * Math.PI) - Math.acos(cos) : Math.acos(cos);
	}

	public void removeEquation(int index) {
		equationImgs.remove(index);
		frame.paintComponents(frame.getGraphics());
	}

	@Override
	public void paintComponent(Graphics g) {
		g.clearRect(0, 0, getWidth(), getHeight());
		g.setColor(Color.white);
		g.fillRect(0, 0, 717, 717);
		for (int i = 0; i < equationImgs.size(); i++) {
			BufferedImage img = equationImgs.get(i);
			if (((JCheckBox) frame.equationPanel.getComponent(i)).isSelected()) {
				g.drawImage(img, -pos.x + 17, -pos.y - 17, zoom, zoom, null);
			}
			// g.drawImage(img, 0, 0, 717, 717, null);
		}
	}

	@Override
	public void adjustmentValueChanged(AdjustmentEvent e) {
		if (((JScrollBar) e.getAdjustable()).getOrientation() == 0) {
			// Horizontal
			pos.x = e.getValue() * 2;
		} else {
			// Vertical
			pos.y = e.getValue() * 2;
		}
		repaint();
	}

}
