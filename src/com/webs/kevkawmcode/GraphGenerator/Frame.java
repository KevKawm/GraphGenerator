package com.webs.kevkawmcode.GraphGenerator;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.AdjustmentListener;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollBar;
import javax.swing.JTextField;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import javax.swing.border.EmptyBorder;

import com.webs.kevkawmcode.Parser.Equation;
import com.webs.kevkawmcode.Parser.EquationParser;
import java.awt.Font;
import javax.swing.JProgressBar;

public class Frame extends JFrame {
	private static final long serialVersionUID = 1L;
	public JPanel contentPane;
	private JTextField equationField;
	public final JPanel equationPanel;
	public final Graph graph;
	public JProgressBar progressBar;

	public final List<String> equationStrings = new ArrayList<String>();
	public final List<Equation> equations = new ArrayList<Equation>();

	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					Frame frame = new Frame();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	public Frame() {
		setTitle("Graph Generator");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		try {
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		} catch (ClassNotFoundException | InstantiationException | IllegalAccessException | UnsupportedLookAndFeelException e) {
			e.printStackTrace();
		}
		setBounds(100, 100, 900, 700);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		setResizable(false);

		graph = new Graph(new Dimension(1, 1), 717, this);
		graph.setBackground(Color.BLACK);
		graph.setBounds(157, 11, 717, 606);
		contentPane.add(graph);
		graph.setLayout(null);

		final JScrollBar graphScrollX = new JScrollBar();
		graphScrollX.setOrientation(JScrollBar.HORIZONTAL);
		graphScrollX.setBounds(0, 589, 637, 17);
		graphScrollX.addAdjustmentListener((AdjustmentListener) graph);
		graphScrollX.setMaximum((graph.zoom - 717) / 2);
		graph.add(graphScrollX);

		final JScrollBar graphScrollY = new JScrollBar();
		graphScrollY.setBounds(0, 0, 17, 589);
		graphScrollY.addAdjustmentListener((AdjustmentListener) graph);
		graphScrollY.setMaximum((graph.zoom - 606) / 2);
		graph.add(graphScrollY);

		JButton zoomIn = new JButton("+");
		zoomIn.setBounds(637, 589, 40, 17);
		zoomIn.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				graph.zoom += 25;
				graphScrollX.setMaximum((graph.zoom - 717) / 2);
				graphScrollY.setMaximum((graph.zoom - 606) / 2);
				paintComponents(getGraphics());
				graph.repaint();
			}

		});
		graph.add(zoomIn);

		JButton zoomOut = new JButton("-");
		zoomOut.setBounds(677, 589, 40, 17);
		zoomOut.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				graph.zoom -= 25;
				graphScrollX.setMaximum((graph.zoom - 717) / 2);
				graphScrollY.setMaximum((graph.zoom - 606) / 2);
				paintComponents(getGraphics());
				graph.repaint();
			}

		});
		graph.add(zoomOut);

		equationPanel = new JPanel();
		equationPanel.setBackground(Color.LIGHT_GRAY);
		equationPanel.setBounds(10, 11, 137, 606);
		contentPane.add(equationPanel);
		equationPanel.setLayout(null);

		JButton btnAdd = new JButton("Add Equation");
		btnAdd.setBounds(10, 628, 108, 23);
		btnAdd.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				String name = equationField.getText();
				addEquation(name);
			}
		});
		contentPane.add(btnAdd);

		equationField = new JTextField();
		equationField.setToolTipText("Enter equation here");
		equationField.setBounds(128, 629, 240, 20);
		contentPane.add(equationField);
		equationField.setColumns(10);

		progressBar = new JProgressBar();
		progressBar.setBounds(378, 628, 496, 14);
	}

	public void addEquation(final String name) {
		equationStrings.add(name.replace("p", "\u03C0"));
		equations.add(new Equation(EquationParser.parse(name, false)));
		Color color = new Color((int) (Math.random() * 255), (int) (Math.random() * 255), (int) (Math.random() * 255));
		graph.addEquation(new Equation(EquationParser.parse(name, true)), color);
		final JCheckBox checkBox = new JCheckBox(name);
		checkBox.setFont(new Font("Tahoma", Font.BOLD, 11));
		checkBox.setBackground(color);
		checkBox.setBounds(6, equationPanel.getComponentCount() * 26 + 7, 125, 23);
		checkBox.setSelected(true);
		checkBox.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				graph.repaint();
			}

		});
		equationPanel.add(checkBox);
		paintComponents(getGraphics());
		equationPanel.paintComponents(equationPanel.getGraphics());
		graph.paintComponents(graph.getGraphics());
		equationField.setText("");
	}
}
