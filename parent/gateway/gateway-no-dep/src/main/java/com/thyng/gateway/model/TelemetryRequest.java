package com.thyng.gateway.model;

import java.util.function.Consumer;

import com.thyng.model.dto.TelemetryDTO;

import lombok.Builder;
import lombok.Value;

@Value
@Builder
public class TelemetryRequest {

	private Long thingId;
	
	private Long sensorId;
	
	private TelemetryDTO telemetry;
	
	private Runnable success;
	
	private Consumer<Exception> failure;
	
	private Runnable after;

}
