package com.thyng.netty;

public interface Client {

	<T> T execute(Object request);

}
