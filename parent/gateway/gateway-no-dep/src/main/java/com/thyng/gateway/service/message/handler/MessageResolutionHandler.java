package com.thyng.gateway.service.message.handler;

import java.io.ByteArrayInputStream;
import java.io.DataInput;
import java.io.DataInputStream;

import com.thyng.gateway.model.Message;
import com.thyng.gateway.service.message.MessageHandlerChain;

public class MessageResolutionHandler implements MessageHandler {

	@Override
	public void handle(Message message, MessageHandlerChain chain) throws Exception {
		final DataInputStream in = new DataInputStream(new ByteArrayInputStream(message.getPayload()));
		while (0 < in.available()) {
			final Long sensorId = in.readLong();
			final Object value = readValue(in);
			message.getValues().put(sensorId, value);
		}
		chain.getContext().getEventBus().publish(Message.RECEIVED, message);
		chain.next(message);
	}

	private Object readValue(DataInput in) throws Exception {
		final Byte dataType = in.readByte();
		switch (dataType) {
		case 0:
			return in.readUTF();
		case 1:
			return in.readBoolean();
		case 2:
			return in.readByte();
		case 3:
			return in.readShort();
		case 4:
			return in.readChar();
		case 5:
			return in.readInt();
		case 6:
			return in.readFloat();
		case 7:
			return in.readLong();
		case 8:
			return in.readDouble();
		}
		throw new Exception("Invalid data type");
	}

}
