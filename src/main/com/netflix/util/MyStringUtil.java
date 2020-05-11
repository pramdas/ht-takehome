package com.netflix.util;

public class MyStringUtil {
	public static String cleanStr(String str) {
		String newstr = str.replace("\n", "");
		return newstr;
	}
}
