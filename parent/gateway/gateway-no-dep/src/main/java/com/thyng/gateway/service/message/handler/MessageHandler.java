package com.thyng.gateway.service.message.handler;

import com.thyng.gateway.service.message.Message;
import com.thyng.gateway.service.message.MessageHandlerChain;

public interface MessageHandler {

	void handle(Message message, MessageHandlerChain chain) throws Exception;
	
}
