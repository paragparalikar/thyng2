package com.thyng.gateway.service.message.handler;

import com.thyng.gateway.model.Message;
import com.thyng.gateway.service.message.MessageHandlerChain;

public class MessagePersistenceHandler implements MessageHandler{

	@Override
	public void handle(Message message, MessageHandlerChain chain) throws Exception {
		chain.getContext().getPersistenceProvider().save(message);
		chain.getContext().getEventBus().publish(Message.PERSISTED, message);
		chain.next(message);
	}

}
