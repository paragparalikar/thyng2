package com.thyng.gateway.model;

import java.util.Map;

import lombok.Builder;
import lombok.Value;

@Value
@Builder
public class TelemetryMessage {
	public static final String RECEIVED = "telemetry-message-received";
	
	private final Long timestamp;
	private final Map<Long,String> values;
	
}
