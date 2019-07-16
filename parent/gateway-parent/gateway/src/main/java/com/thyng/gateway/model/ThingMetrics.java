package com.thyng.gateway.model;

import java.util.Map;

import lombok.Builder;
import lombok.Value;

@Value
@Builder
public class ThingMetrics {
	public static final String RECEIVED = "thing-metrics-received";
	
	private final Long timestamp;
	private final Map<Long,Double> values;
	
}
