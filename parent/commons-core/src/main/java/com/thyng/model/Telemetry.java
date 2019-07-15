package com.thyng.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class Telemetry {

	private long gatewayId;
	private long transactionId;
	private long[] sensorIds;
	private byte[][] data;
	
}
