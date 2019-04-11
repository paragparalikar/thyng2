package com.thyng.gateway.provider.persistence;

import java.io.IOException;

import com.thyng.model.TypedMap;
import com.thyng.model.dto.TelemetryDTO;

public class PersistentTelemetry extends TelemetryDTO{
	
	private final TypedMap context = new TypedMap();

	public PersistentTelemetry(Long thingId, Long sensorId) {
		super(thingId, sensorId);
	}

	public PersistentTelemetry(Long thingId, Long sensorId, byte dataType, byte[] data) throws IOException {
		super(thingId, sensorId, dataType, data);
	}
	
	public TypedMap getContext() {
		return context;
	}

}
