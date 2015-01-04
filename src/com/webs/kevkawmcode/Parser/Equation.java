package com.webs.kevkawmcode.Parser;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.webs.kevkawmcode.Exception.InvalidOperationArgumentException;

public class Equation {

	List<String> equation;

	public Equation(List<String> equation) {
		this.equation = equation;
	}

	public double getLeft(HashMap<String, String> values) {
		List<String> localEquation = new ArrayList<String>();
		for (String s : equation) {
			if (!s.equals(" ")) {
				localEquation.add(s);
			}
		}

		replaceVars(localEquation, values);

		int equalsPos = findEqual(localEquation);
		List<String> leftEquation = localEquation.subList(0, equalsPos);
		try {
			return solve(leftEquation);
		} catch (InvalidOperationArgumentException e) {
			return 0;
		}
	}

	public double getRight(HashMap<String, String> values) {
		List<String> localEquation = new ArrayList<String>();
		for (String s : equation) {
			if (!s.equals(" ")) {
				localEquation.add(s);
			}
		}

		replaceVars(localEquation, values);

		int equalsPos = findEqual(localEquation);
		List<String> rightEquation = localEquation.subList(equalsPos + 1, localEquation.size());
		try {
			return solve(rightEquation);
		} catch (InvalidOperationArgumentException e) {
			return 0;
		}
	}

	public void replaceVars(List<String> equation, HashMap<String, String> values) {
		for (String var : values.keySet()) {
			if (equation.contains(var)) {
				for (int i = 0; i < equation.size(); i++) {
					String index = equation.get(i);
					boolean negative = false;
					if (i > 0 && equation.get(i - 1) != null && equation.get(i - 1).equals("-")) {
						negative = true;
					}
					if (index != null && index.equals(var)) {
						if (negative) {
							if (values.get(var).startsWith("-")) {
								equation.set(i, values.get(var).substring(1));
							} else {
								equation.set(i, "-" + values.get(var));
							}
						} else {
							equation.set(i, values.get(var));
						}
						if (negative)
							equation.remove(i - 1);
					}
				}
			}
		}
	}

	public boolean isEqual(HashMap<String, String> values) {
		List<String> localEquation = new ArrayList<String>();
		for (String s : equation) {
			if (!s.equals(" ")) {
				localEquation.add(s);
			}
		}

		replaceVars(localEquation, values);

		// Solve both sides
		int equalsPos = findEqual(localEquation);
		if (equalsPos == -1)
			return false;
		List<String> leftEquation = localEquation.subList(0, equalsPos);
		List<String> rightEquation = localEquation.subList(equalsPos + 1, localEquation.size());
		double rightValue = 0;
		double leftValue = 0;
		try {
			leftValue = solve(leftEquation);
			rightValue = solve(rightEquation);
		} catch (InvalidOperationArgumentException e) {
			return false;
		}

		// Return if values are the same
		return (leftValue == rightValue);
	}

	public static double solve(List<String> equation) throws InvalidOperationArgumentException {
		String[][] ops = { { "log", "ln", "sin", "cos", "tg", "tan", "sen" }, { "!" }, { "^" }, { "*", "/" }, { "+", "-" }, { "abs", "mod" } };
		List<String> localEquation = new ArrayList<String>();
		for (String s : equation) {
			if (!s.equals(" ")) {
				localEquation.add(s);
			}
		}

		// Finds parentheses and solves
		while (localEquation.contains("(")) {
			int open = findOpen(localEquation);
			int close = findCloseFor(localEquation, open);
			localEquation.set(open, solve(localEquation.subList(open + 1, close)) + "");
			for (int i = open; i < close; i++) {
				localEquation.remove(open + 1);
			}
		}

		for (int j = 0; j < ops.length; j++) {
			String[] sA = ops[j];
			int i = findFirst(sA, localEquation);
			while (i != -1) {
				double a = j > 0 ? i != 0 ? Double.parseDouble(localEquation.get(i - 1)) : 0 : 0;
				double b = j != 1 ? Double.parseDouble(localEquation.get(i + 1)) : 0;
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
				case "!":
					t = factorial(a);
					if (t < 0) {
						throw (new InvalidOperationArgumentException());
					}
					break;
				case "log":
					t = Math.log10(b);
					if (t < 0) {
						throw (new InvalidOperationArgumentException());
					}
					break;
				case "ln":
					t = Math.log(b);
					if (t < 0) {
						throw (new InvalidOperationArgumentException());
					}
					break;
				case "sin":
					t = Math.sin(b);
					break;
				case "sen":
					t = Math.sin(b);
					break;
				case "cos":
					t = Math.cos(b);
					break;
				case "tan":
					t = Math.tan(b);
					break;
				case "tg":
					t = Math.tan(b);
					break;
				case "abs":
					t = Math.abs(b);
					break;
				case "mod":
					t = Math.abs(b);
					break;
				}
				localEquation.set(i, t + "");
				if (j != 1)
					localEquation.remove(i + 1);
				if (j > 0)
					if (i != 0) localEquation.remove(i - 1);
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

	public static long factorial(double num) {
		if (isInt(num)) {
			if (num == 0 || num == 1)
				return 1;
			long ret = 2;
			for (int i = 3; i <= num; i++) {
				ret *= i;
			}
			return ret;
		} else {
			return -1;
		}
	}

	public static boolean isInt(double num) {
		return (Math.round(num) == num);
	}

}