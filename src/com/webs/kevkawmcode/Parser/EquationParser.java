package com.webs.kevkawmcode.Parser;

import java.util.ArrayList;
import java.util.List;

public class EquationParser {

	public static List<String> parse(String eq, boolean print) {
		// Removing spaces
		String eqBU = eq;
		eq = "";
		for (int i = 0; i < eqBU.length(); i++) {
			if (eqBU.charAt(i) != ' ')
				eq += eqBU.charAt(i);
		}
		// Remove some operations
			// At beginning
		while (true) {
			if (eq.startsWith("+") || eq.startsWith("*") || eq.startsWith("/") || eq.startsWith("!")) {
				eq = eq.substring(1);
			} else {
				break;
			}
		}
			// At end
		while (true) {
			if (eq.endsWith("+") || eq.endsWith("-") || eq.endsWith("*") || eq.endsWith("/")) {
				eq = eq.substring(0, eq.length() - 1);
			} else {
				break;
			}
		}
		// Adding "=0"
		if (!eq.contains("=")) eq += "=0";
		// Separating everything
		List<String> ret_ = new ArrayList<String>();
		String str = "";
		for (int i = 0; i < eq.length(); i++) {
			Character xar = eq.charAt(i);
			if (ops().contains(xar.toString())) {
				if (xar.toString().equals("-")) {
					if (!isNumber(Character.toString(eq.charAt(i + 1)))) {
						ret_.add(str);
						str = "-";
					} else if (i != 0) {
						if (!str.equals(""))
							ret_.add(str);
						ret_.add("+");
						str = "-";
					} else {
						str = "-";
					}
				} else {
					if (!str.equals(""))
						ret_.add(str);
					str = "";
					ret_.add(xar.toString());
				}
			} else {
				str += xar;
			}
		}
		ret_.add(str);
		// Separating variables from numbers
		List<String> ret = new ArrayList<String>();
		for (int i = 0; i < ret_.size(); i++) {
			String str2 = ret_.get(i);
			List<String> vars = new ArrayList<String>(), nums = new ArrayList<String>();
			boolean changed = false;
			if (hasVariable(str2) && hasNumber(str2)) {
				changed = true;
				for (int j = 0; j < str2.length(); j++) {
					if (isVariable(Character.toString(str2.charAt(j))))
						vars.add(Character.toString(str2.charAt(j)));
				}
				String str_ = "";
				for (int j = 0; j < str2.length(); j++) {
					if (isNumber(Character.toString(str2.charAt(j))))
						str_ += str2.charAt(j);
					if (isVariable(Character.toString(str2.charAt(j))) || j == str2.length() - 1) {
						if (!str_.isEmpty())
							nums.add(str_);
						str_ = "";
					}
				}
			}
			if (changed) {
				for (String j : nums) {
					ret.add(j);
					ret.add("*");
				}
				for (String j : vars) {
					ret.add(j);
					ret.add("*");
				}
				ret.remove(ret.size() - 1);
			} else {
				ret.add(str2);
			}
		}
		// Separating "-"s from stuff
		boolean changed = true;
		while (changed) {
			changed = false;
			for (int i = 0; i < ret.size(); i++) {
				if (ret.get(i).length() > 1 && ret.get(i).startsWith("-") && !isNumber(Character.toString(ret.get(i).charAt(1)))) {
					ret.set(i, ret.get(i).substring(1));
					ret.add(i, "-");
					ret.add(i, "+");
					changed = true;
				}
			}
		}
		// Replacing {} & [] into ()
		String[] array1 = { "[", "{" };
		List<String> list1 = new ArrayList<String>();
		for (String j : array1) {
			list1.add(j);
		}
		String[] array2 = { "]", "}" };
		List<String> list2 = new ArrayList<String>();
		for (String j : array2) {
			list2.add(j);
		}
		for (int i = 0; i < ret.size(); i++) {
			String repl = ret.get(i);
			if (list1.contains(repl) || list2.contains(repl)) {
				ret.remove(i);
				ret.add(i, list1.contains(repl) ? "(" : ")");
			}
		}
		// Replacing "p" into pi
		for (int i = 0; i < ret.size(); i++) {
			if (ret.get(i).equals("p")) {
				ret.remove(i);
				ret.add(i, Double.toString(Math.PI));
			}
		}
		// Remove emptys
		for (int i = 0; i < ret.size(); i++) {
			if (ret.get(i).equals("")) {
				ret.remove(i);
				i--;
			}
		}
		// Remove "+"s next to parentheses
		for (int i = 0; i < ret.size(); i++) {
			if (i != 0 && ret.get(i).equals(")") && ret.get(i - 1).equals("+")) {
				ret.remove(i - 1);
				i--;
			} else if (i != ret.size() - 1 && ret.get(i).equals("(") && ret.get(i + 1).equals("+")) {
				ret.remove(i + 1);
			}
		}
		// Do signal games
		changed = true;
		while (changed) {
			changed = false;
			// ++
			for (int i = 0; i < ret.size(); i++) {
				if (i != ret.size() - 1 && ret.get(i).equals("+") && ret.get(i + 1).equals("+")) {
					ret.remove(i);
					changed = true;
				}
			}
			// --
			for (int i = 0; i < ret.size(); i++) {
				if (i != ret.size() - 1 && ret.get(i).equals("-") && ret.get(i + 1).equals("-")) {
					ret.remove(i);
					ret.set(i, "+");
					changed = true;
				}
			}
			// +-
			for (int i = 0; i < ret.size(); i++) {
				if (i != ret.size() - 1 && ((ret.get(i).equals("+") && ret.get(i + 1).equals("-")) || (ret.get(i).equals("-") && ret.get(i + 1).equals("+")))) {
					ret.remove(i);
					ret.set(i, "-");
					changed = true;
				}
			}
		}
		// Removing some extra junk
		// At beginning
		while (true) {
			if ((ret.get(0).equals("-") && isNumber(ret.get(1))) || ret.get(0).equals("+")) {
				ret.remove(0);
			} else {
				break;
			}
		}
			// At end
		while (true) {
			if (ret.get(ret.size() - 1).equals("-") || ret.get(ret.size() - 1).equals("+")) {
				ret.remove(ret.size() - 1);
			} else {
				break;
			}
		}
		if (print) System.out.println(ret);
		return ret;
	}

	public static List<String> ops() {
		
		String[] array = { "=", "+", "-", "*", "/", "(", ")", "[", "]", "{", "}", "^", "" , "ln", "log", "sin", "cos", "tg", "tan", "!", "sen", "abs", "mod" };
		
		List<String> ret = new ArrayList<String>();
		for (String i : array) {
			ret.add(i);
		}
		return ret;
	}

	public static boolean isNumber(String str) {
		try {
			Double.parseDouble(str);
			return true;
		} catch (Exception e) {
			return false;
		}
	}

	public static boolean isOp(String str) {
		return ops().contains(str);
	}

	public static boolean isVariable(String str) {
		if (str.length() > 1)
			return false;
		return Character.isLetter(str.toCharArray()[0]);
	}

	public static boolean hasVariable(String str) {
		for (int i = 0; i < str.length(); i++) {
			if (isVariable(Character.toString(str.charAt(i))))
				return true;
		}
		return false;
	}

	public static boolean hasNumber(String str) {
		for (int i = 0; i < str.length(); i++) {
			if (isNumber(Character.toString(str.charAt(i))))
				return true;
		}
		return false;
	}

}