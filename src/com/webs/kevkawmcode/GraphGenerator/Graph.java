package com.webs.kevkawmcode.GraphGenerator;

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Point;

import javax.swing.JPanel;

public class Graph extends JPanel{

	Dimension interval;
	double zoom;
	
	public Graph(Dimension interval, double zoom){
		this.interval = interval;
		this.zoom = zoom;
	}
	
	@Override
	public void paintComponent(Graphics g){
		
	}
	
	private Point convert(Point p){
		Point ret = new Point(p.x,p.y);
		ret.y = getHeight() - ret.y;
		return p;
	}
	
}
