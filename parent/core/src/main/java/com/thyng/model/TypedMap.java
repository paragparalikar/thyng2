package com.thyng.model;

import java.util.HashMap;
import java.util.Map;

public class TypedMap extends HashMap<String, Object> {
	private static final long serialVersionUID = 2744704761818543550L;

	public TypedMap() {
	}

	public TypedMap(int initialCapacity) {
		super(initialCapacity);
	}

	public TypedMap(Map<? extends String, ? extends Object> m) {
		super(m);
	}

	public TypedMap(int initialCapacity, float loadFactor) {
		super(initialCapacity, loadFactor);
	}
	
	@SuppressWarnings("unchecked")
	public <T> T get(String key, Class<T> type){
		return (T)get(key);
	}
	
	public Double getDouble(String key){
		return get(key, Double.class);
	}
	
	public Long getLong(String key){
		return get(key, Long.class);
	}
	
	public Float getFloat(String key){
		return get(key, Float.class);
	}
	
	public Integer getInteger(String key){
		return get(key, Integer.class);
	}

	public Short getShort(String key){
		return get(key, Short.class);
	}
	
	public Character getCharacter(String key){
		return get(key, Character.class);
	}
	
	public Byte getByte(String key){
		return get(key, Byte.class);
	}
	
	public Boolean getBoolean(String key){
		return get(key, Boolean.class);
	}
	
	public String getString(String key){
		return get(key, String.class);
	}
}
