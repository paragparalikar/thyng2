package com.thyng;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public interface Serializer {

	byte[] serialize(Object object);
	
	<T> T deserialize(InputStream inputStream);

	void serialize(Object object, OutputStream outputStream) throws IOException;
	
}
