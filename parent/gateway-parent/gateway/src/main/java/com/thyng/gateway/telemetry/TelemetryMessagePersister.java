package com.thyng.gateway.telemetry;

import com.thyng.gateway.model.Context;
import com.thyng.gateway.model.TelemetryMessage;
import com.thyng.gateway.provider.persistence.TelemetryStore;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class TelemetryMessagePersister {

	private final Context context;
	
	public void persist(TelemetryMessage message) {
		message.getValues().forEach((sensorId, value) -> {
			final TelemetryStore telemetryStore = context.getPersistenceProvider()
					.getTelemetryStore(sensorId);
			telemetryStore.save(message.getTimestamp(), value);
		});
	}

}
