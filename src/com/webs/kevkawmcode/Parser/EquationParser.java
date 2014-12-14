package com.webs.kevkawmcode.Parser;

import java.util.ArrayList;
import java.util.List;

public class EquationParser {

	public static List<String> parse(String eq) {
		List<String> ret_ = new ArrayList<String>();
		String str = "";
		for (int i = 0; i < eq.length(); i++) {
			Character xar = eq.charAt(i);
			if (ops().contains(xar.toString())) {
				if (!str.equals(""))
					ret_.add(str);
				str = "";
				ret_.add(xar.toString());
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
		return ret;
	}

	public static List<String> ops() {
		String[] array = { "=", "+", "-", "*", "/", "(", ")", "[", "]", "{", "}", "^" , "log", "sin", "cos", "tg", "tan", "!", "sen"};
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