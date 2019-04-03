package com.thyng.gateway;

import java.io.IOException;
import java.net.Socket;

import com.esotericsoftware.kryo.io.Output;
import com.thyng.model.Serializer;
import com.thyng.model.enumeration.GatewayOps;

import lombok.Builder;

@Builder
public class BinaryClient {

	private int port;
	private String host;
	private GatewayOps gatewayOps;
	private Object payload;

	public void send() throws IOException{
		final Socket socket = new Socket(host, port);
		final Output output = new Output(socket.getOutputStream());
		Serializer.kryo().writeObject(output, gatewayOps.ordinal());
		Serializer.kryo().writeClassAndObject(output, payload);
		output.flush();
		socket.close();
	}
	
}
