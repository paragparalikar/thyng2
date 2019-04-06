package com.thyng.gateway;

public interface PropertyProvider {

	String get(String key, String defaultValue);
	
	Long getLong(String key, Long defaultValue);
	
	Integer getInteger(String key, Integer defaultValue);
	
	Boolean getBoolean(String key, Boolean defaultValue);
	
}