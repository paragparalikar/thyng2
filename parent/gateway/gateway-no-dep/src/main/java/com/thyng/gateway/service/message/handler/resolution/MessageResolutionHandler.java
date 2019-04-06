package com.thyng.gateway.service.message.handler.resolution;

import com.thyng.gateway.service.message.Message;
import com.thyng.gateway.service.message.MessageHandlerChain;
import com.thyng.gateway.service.message.handler.MessageHandler;
import com.thyng.model.enumeration.DataType;

public class MessageResolutionHandler implements MessageHandler{

	private final MessageResolverFactory messageResolverFactory = 
			new MessageResolverFactory();
	
	@Override
	public void handle(Message message, MessageHandlerChain chain) throws Exception {
		final DataType dataType = message.getSensor().getDataType();
		final MessageResolver messageResolver = messageResolverFactory.get(dataType);
		messageResolver.resolve(message);
		chain.next(message);
	}

}
