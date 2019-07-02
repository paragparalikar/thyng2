package com.thyng.gateway.provider.serialization;

import java.lang.reflect.Type;

public interface SerializationProvider<T> {

	T serialize(Object object);
	
	<V> V deserialize(T content, Class<V> clazz);
	
	<V> V deserialize(T content, Type type);
}
