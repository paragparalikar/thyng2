package com.thyng.gateway.service.message;

import com.thyng.gateway.model.Context;
import com.thyng.gateway.model.Message;
import com.thyng.gateway.service.message.handler.MessageHandler;
import com.thyng.gateway.service.message.handler.MessageNormalizationHandler;
import com.thyng.gateway.service.message.handler.MessagePersistenceHandler;
import com.thyng.gateway.service.message.handler.MessageResolutionHandler;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class MessageHandlerChain {
	private static final MessageHandler[] HANDLERS = {
			new MessageResolutionHandler(),
			new MessageNormalizationHandler(),
			new MessagePersistenceHandler()
		};

	private int index;
	private final Context context;
	
	public void next(Message message) throws Exception{
		if(index < HANDLERS.length){
			final MessageHandler handler = HANDLERS[index++];
			handler.handle(message, this);
		}
	}

	public Context getContext() {
		return context;
	}
	
}
