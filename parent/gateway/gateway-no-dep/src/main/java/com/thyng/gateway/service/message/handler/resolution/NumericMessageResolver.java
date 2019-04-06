package com.thyng.gateway.service.message.handler.resolution;

import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.util.Map;

import com.thyng.gateway.service.message.Message;

import lombok.SneakyThrows;

public class NumericMessageResolver implements MessageResolver {

	private static final int THRESHOLD = 2*Byte.BYTES + 2*(Long.BYTES + Byte.BYTES);
	
	@Override
	public void resolve(Message message) throws InvalidMessageException {
		final byte[] payload = message.getPayload();
		final DataInputStream in = new DataInputStream(new ByteArrayInputStream(payload));
		if(payload.length <= Double.BYTES){
			final Double value = resolve(payload.length, in);
			message.getValues().put(message.getSensor().getId(), value);
		}else if(payload.length >= THRESHOLD){
			resolve(in, message.getValues());
		}else{
			throw new InvalidMessageException();
		}
	}
	
	@SneakyThrows
	private Double resolve(int bytes, DataInputStream in) throws InvalidMessageException{
		switch(bytes){
		case Byte.BYTES: return (double) in.readByte();
		case Short.BYTES: return (double) in.readShort();
		case Integer.BYTES: return (double) in.readInt();
		case Double.BYTES: return in.readDouble();
		default: throw new InvalidMessageException();
		}
	}
	
	@SneakyThrows
	private void resolve(DataInputStream in, Map<Long, Object> values) throws InvalidMessageException{
		final int bytes = in.readByte();
		final int count = in.readByte();
		for(int index = 0; index < count; index++){
			values.put(in.readLong(), resolve(bytes, in));
		}
	}
	
	

}
