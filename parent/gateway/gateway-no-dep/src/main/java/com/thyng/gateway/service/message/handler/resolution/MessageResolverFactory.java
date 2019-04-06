package com.thyng.gateway.service.message.handler.resolution;

import java.util.HashMap;
import java.util.Map;

import com.thyng.model.enumeration.DataType;

public class MessageResolverFactory {

	private final Map<DataType, MessageResolver> mapping = new HashMap<>();
	
	public MessageResolverFactory() {
		mapping.put(DataType.NUMBER, new NumericMessageResolver());
		mapping.put(DataType.BOOLEAN, new BooleanMessageResolver());
		mapping.put(DataType.TEXT, new StringMessageResolver());
	}	
	
	public MessageResolver get(DataType dataType){
		return mapping.get(dataType);
	}

}
