package com.thyng.gateway.provider.serialization;

import java.lang.reflect.Type;

import com.google.gson.Gson;

public class GsonSerializationProvider implements SerializationProvider<String> {

	private final Gson gson = new Gson();

	@Override
	public String serialize(Object object) {
		return gson.toJson(object);
	}

	@Override
	public <V> V deserialize(String content, Class<V> clazz) {
		return gson.fromJson(content, clazz);
	}

	@Override
	public <V> V deserialize(String content, Type type) {
		return gson.fromJson(content, type);
	}
	

}
