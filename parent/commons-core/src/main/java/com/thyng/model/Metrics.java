package com.thyng.model;

import java.util.HashMap;
import java.util.Map;

import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
@RequiredArgsConstructor
public class Metrics {

	private final byte[] data;
	private final Long metricsSchemaId;
	private final Map<Long, Map<Long, Double>> sensorIdValues = new HashMap<>();
	
}
