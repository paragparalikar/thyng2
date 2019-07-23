package com.thyng.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.AbstractMap.SimpleImmutableEntry;
import java.util.Arrays;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class StringUtil {

	public static String line(){
		return "\n_______________________________________________________________________________________";
	}
	
	public static boolean hasText(String text){
		return null != text && 0 < text.trim().length();
	}
	
	public static String convert(InputStream inputStream, String charset) throws IOException {
		try (BufferedReader br = new BufferedReader(new InputStreamReader(inputStream, charset))) {
			return br.lines().collect(Collectors.joining(System.lineSeparator()));
		}
	}
	
	public static Map<String, List<String>> splitQuery(String query) {
	    if (!hasText(query)) {
	        return Collections.emptyMap();
	    }
	    return Arrays.stream(query.split("&"))
	            .map(StringUtil::splitQueryParameter)
	            .collect(Collectors.groupingBy(SimpleImmutableEntry::getKey, LinkedHashMap::new, 
	            		Collectors.mapping(Map.Entry::getValue, Collectors.toList())));
	}

	public static SimpleImmutableEntry<String, String> splitQueryParameter(String it) {
	    final int idx = it.indexOf("=");
	    final String key = idx > 0 ? it.substring(0, idx) : it;
	    final String value = idx > 0 && it.length() > idx + 1 ? it.substring(idx + 1) : null;
	    return new SimpleImmutableEntry<>(key, value);
	}
	
}
