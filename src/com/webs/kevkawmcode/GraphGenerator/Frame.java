package com.webs.kevkawmcode.GraphGenerator;

import java.awt.Color;
import java.awt.EventQueue;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import javax.swing.border.EmptyBorder;

import com.webs.kevkawmcode.Exception.InvalidOperationArgumentException;
import com.webs.kevkawmcode.Parser.Equation;
import com.webs.kevkawmcode.Parser.EquationParser;

public class Frame extends JFrame {

	private JPanel contentPane;
	private JTextField equationField;
	private final JPanel equationPanel;

	public final List<String> equationStrings = new ArrayList<String>();
	public final List<Equation> equations = new ArrayList<Equation>();

	public static void main(String[] args) {
		String input = JOptionPane.showInputDialog("Equation");
		List<String> equationList = EquationParser.parse(input);
		double out = 0;
		try {
			out = Equation.solve(equationList);
		} catch (InvalidOperationArgumentException e1) {
			e1.printStackTrace();
		}
		System.out.println(out);
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

		JPanel panel = new JPanel();
		panel.setBackground(Color.WHITE);
		panel.setBounds(157, 11, 717, 606);
		contentPane.add(panel);

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
				final String name = equationField.getText();
				addEquation(name);
			}
		});
		contentPane.add(btnAdd);

		equationField = new JTextField();
		equationField.setToolTipText("Enter equation here");
		equationField.setBounds(128, 629, 240, 20);
		contentPane.add(equationField);
		equationField.setColumns(10);
	}

	public void addEquation(final String name) {
		if (name.contains("=")) {
			equationStrings.add(name);
			equations.add(new Equation(EquationParser.parse(name)));
			final JCheckBox checkBox = new JCheckBox(name);
			checkBox.setBackground(Color.LIGHT_GRAY);
			checkBox.setBounds(6, equationPanel.getComponentCount() * 26 + 7, 125, 23);
			checkBox.setSelected(true);
			equationPanel.add(checkBox);
			paintComponents(getGraphics());
		} else {
			JOptionPane.showMessageDialog(null, "The equation you entered does not contain an =", "Error", 0, null);
		}
	}

}
