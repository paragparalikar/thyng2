package com.thyng.gateway.service.message.handler.resolution;

import java.io.ByteArrayInputStream;
import java.io.DataInput;
import java.io.DataInputStream;

import com.thyng.gateway.service.message.Message;

import lombok.SneakyThrows;

public class StringMessageResolver implements MessageResolver {

	@Override
	@SneakyThrows
	public void resolve(Message message) throws InvalidMessageException {
		final DataInput in = new DataInputStream(new ByteArrayInputStream(message.getPayload()));
		final int count = in.readByte();
		for(int index = 0; index < count; index++){
			message.getValues().put(in.readLong(), in.readUTF());
		}
	}

}
