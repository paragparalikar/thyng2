package com.thyng.gateway.service.message.handler.resolution;

import com.thyng.gateway.service.message.Message;

public interface MessageResolver {

	void resolve(Message message) throws InvalidMessageException;
	
}
