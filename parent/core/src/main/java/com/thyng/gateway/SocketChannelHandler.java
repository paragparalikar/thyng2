package com.thyng.gateway;

import java.io.IOException;

import com.esotericsoftware.kryo.io.Input;
import com.esotericsoftware.kryo.io.Output;

public interface SocketChannelHandler{

	void handle(Input in, Output out) throws IOException;

}
