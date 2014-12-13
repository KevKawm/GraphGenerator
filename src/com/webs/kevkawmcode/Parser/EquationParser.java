package com.webs.kevkawmcode.Parser;

import java.util.ArrayList;
import java.util.List;

public class EquationParser {

	public static List<String> parse(String eq) {
		List<Character> ops = new ArrayList<Character>();
		ops = ops();
		List<String> ret_ = new ArrayList<String>();
		String str = "";
		for (int i = 0; i < eq.length(); i++) {
			Character xar = eq.charAt(i);
			if (ops.contains(xar)) {
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
		return ret;
	}

	public static List<Character> ops() {
		Character[] array = { '=', '+', '-', '*', '/', '(', ')', '^' };
		List<Character> ret = new ArrayList<Character>();
		for (Character i : array) {
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
		String[] array = { "=", "+", "-", "*", "/", "(", ")", "^", ">", "<" };
		List<String> list = new ArrayList<String>();
		for (String i : array) {
			list.add(i);
		}
		return list.contains(str);
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