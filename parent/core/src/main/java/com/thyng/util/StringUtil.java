package com.thyng.util;

public class StringUtil {

	public static String line(){
		return "\n_______________________________________________________________________________________";
	}
	
	public static boolean hasText(String text){
		return null != text && 0 < text.trim().length();
	}
	
}
