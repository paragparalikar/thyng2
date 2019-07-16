package com.thyng.model;

import java.util.Map;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class GatewayMetrics {

	private Long gatewayId;
	private Long transactionId;
	private Map<Long, Map<Long,Double>> sensorMetrics;
	
}
