package com.thyng.gateway.model;

import java.util.Map;

import lombok.Builder;
import lombok.Value;

@Value
@Builder
public class Message {
	public static final String RECEIVED = "topic-message-received";
	public static final String NORMALIZED = "topic-message-normalized";
	public static final String PERSISTED = "topic-message-persisted";
	
	private final Long timestamp;
	private final Map<Long,String> values;
	
}
