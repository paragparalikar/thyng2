package com.thyng.netty;

import java.net.Socket;
import java.nio.ByteBuffer;

import com.thyng.KryoSerializer;
import com.thyng.Serializer;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class OioClient implements Client{
	private static final Serializer SERIALIZER = KryoSerializer.INSTANCE;

	private final int port;
	private final String host;
	
	@Override
	public <T> T execute(@NonNull final Object request){
		try(final Socket socket = new Socket(host, port)){
			final byte[] data = SERIALIZER.serialize(request);
			final ByteBuffer byteBuffer = ByteBuffer.allocate(Integer.BYTES + data.length);
			byteBuffer.putInt(data.length);
			byteBuffer.put(data);
			socket.getOutputStream().write(byteBuffer.array());
			return SERIALIZER.deserialize(socket.getInputStream());
		}catch(Throwable throwable) {
			throw new RuntimeException(throwable);
		}
	}

}
