package com.webs.kevkawmcode.Parser;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class Equation {

	List<String> equation;

	public Equation(List<String> equation) {
		this.equation = equation;
	}

	public boolean isEqual(HashMap<String, String> values) {
		List<String> localEquation = new ArrayList<String>();
		for (String s : equation) {
			if (!s.equals(" ")) {
				localEquation.add(s);
			}
		}

		// Replace all variables with values
		for (String var : values.keySet()) {
			for (int i = 0; i < localEquation.size(); i++) {
				String index = localEquation.get(i);
				if (index.equals(var)) {
					localEquation.set(i, values.get(var));
				}
			}
		}

		// Solve both sides
		int equalsPos = findEqual(localEquation);
		if (equalsPos == -1)
			return false;
		List<String> leftEquation = localEquation.subList(0, equalsPos);
		List<String> rightEquation = localEquation.subList(equalsPos + 1, localEquation.size());
		double leftValue = solve(leftEquation);
		double rightValue = solve(rightEquation);

		// Return if values are the same
		return (leftValue == rightValue);
	}

	public static double solve(List<String> equation) {
		String[][] ops = { { "!", "log", "sin", "cos", "tg" }, { "^" }, { "*", "/" }, { "+", "-" } };
		List<String> localEquation = new ArrayList<String>();
		for (String s : equation) {
			if (!s.equals(" ")) {
				localEquation.add(s);
			}
		}

		// Finds parentheses and solves
		if (localEquation.contains("(")) {
			int open = findOpen(localEquation);
			int close = findCloseFor(localEquation, open);
			localEquation.set(open, solve(localEquation.subList(open + 1, close)) + "");
			for (int i = open; i < close; i++) {
				localEquation.remove(open + 1);
			}
		}

		for (String[] sA : ops) {
			int i = findFirst(sA, localEquation);
			while (i != -1) {
				double a = Double.parseDouble(localEquation.get(i - 1));
				double b = Double.parseDouble(localEquation.get(i + 1));
				double t = 0;
				String op = localEquation.get(i);
				switch (op) {
				case "^":
					t = Math.pow(a, b);
					break;
				case "*":
					t = a * b;
					break;
				case "/":
					t = a / b;
					break;
				case "+":
					t = a + b;
					break;
				case "-":
					t = a - b;
					break;
				}
				localEquation.set(i - 1, t + "");
				localEquation.remove(i);
				localEquation.remove(i);
				i = findFirst(sA, localEquation);
			}
		}
		return Double.parseDouble(localEquation.get(0));
	}

	public static int findFirst(String[] sArray, List<String> list) {
		List<String> sList = new ArrayList<String>();
		for (String s : sArray) {
			sList.add(s);
		}
		for (int i = 0; i < list.size(); i++) {
			String s = list.get(i);
			if (sList.contains(s))
				return i;
		}
		return -1;
	}

	public static int findOpen(List<String> equation) {
		for (int i = 0; i < equation.size(); i++) {
			String s = equation.get(i);
			if (s.equals("("))
				return i;
		}
		return -1;
	}

	public static int findCloseFor(List<String> equation, int openIndex) {
		int t = 0;
		for (int i = openIndex + 1; i < equation.size(); i++) {
			String s = equation.get(i);
			if (s.equals("("))
				t++;
			if (s.equals(")")) {
				if (t == 0) {
					return i;
				} else
					t--;
			}
		}
		return -1;
	}

	public static int findEqual(List<String> equation) {
		for (int i = 0; i < equation.size(); i++) {
			String s = equation.get(i);
			if (s.equals("="))
				return i;
		}
		return -1;
	}

	public static long factorial(int num) {
		if (num == 0 || num == 1) return 1;
		long ret = 2;
		for (int i = 3; i <= num; i++) {
			ret *= i;
		}
		return ret;
	}
	
	public static double log(int num) {
		double exp = 1;
		// Test if the result is whole
		while (Math.pow(10, exp) < num) {
			exp++;;
		}
		if (Math.pow(10, exp) == num) return exp;
		// Decimal result
		exp = 0.001;
		while (Math.pow(10, exp) < num) {
			exp += 0.001;
		}
		String whole = Double.toString(exp).substring(0, Double.toString(exp).indexOf('.'));
		String dec = Double.toString(exp).substring(Double.toString(exp).indexOf('.') + 1).substring(0, 3);
		if (dec.substring(0, 2).equals("00")) {
			dec = "00" + Integer.toString(num < 10 ? Integer.parseInt(dec) - 1 : Integer.parseInt(dec));
		} else if (dec.subSequence(0, 1).equals("0")) {
			dec = "0" + Integer.toString(num < 10 ? Integer.parseInt(dec) - 1 : Integer.parseInt(dec));
		} else {
			dec = Integer.toString(num < 10 ? Integer.parseInt(dec) - 1 : Integer.parseInt(dec));
		}
		return Double.parseDouble(whole + "." + dec);
	}
}
