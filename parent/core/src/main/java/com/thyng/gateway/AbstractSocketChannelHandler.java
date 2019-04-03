package com.thyng.gateway;

import java.io.IOException;

import com.esotericsoftware.kryo.io.Input;
import com.esotericsoftware.kryo.io.Output;
import com.thyng.model.Serializer;

public abstract class AbstractSocketChannelHandler<T> implements SocketChannelHandler {

	protected abstract void handle(T input);

	@Override
	@SuppressWarnings("unchecked")
	public void handle(Input in, Output out) throws IOException {
		final T input = (T) Serializer.kryo().readClassAndObject(in);
		handle(input);
	}

}
