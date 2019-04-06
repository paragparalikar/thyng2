package com.thyng.gateway.service.message.handler.resolution;

import java.io.ByteArrayInputStream;
import java.io.DataInput;
import java.io.DataInputStream;

import com.thyng.gateway.service.message.Message;

import lombok.SneakyThrows;

public class BooleanMessageResolver implements MessageResolver {

	private static final int THRESHOLD = Byte.BYTES + 2*(Long.BYTES + Byte.BYTES);
	
	@Override
	@SneakyThrows
	public void resolve(Message message) throws InvalidMessageException {
		final byte[] payload = message.getPayload();
		if(payload.length == 1){
			message.getValues().put(message.getSensor().getId(), 1 == payload[0]);
		}else if(payload.length >= THRESHOLD){
			final DataInput in = new DataInputStream(new ByteArrayInputStream(payload));
			final int count = in.readByte();
			for(int index = 0; index < count; index++){
				message.getValues().put(in.readLong(), 1 == in.readByte());
			}
		}else{
			throw new InvalidMessageException();
		}
	}

}
