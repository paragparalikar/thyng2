package com.thyng.gateway.metrics;

import com.thyng.gateway.model.Context;
import com.thyng.gateway.model.ThingMetrics;
import com.thyng.gateway.provider.persistence.SensorMetricsStore;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class ThingMetricsPersister {

	private final Context context;
	
	public void persist(ThingMetrics message) {
		message.getValues().forEach((sensorId, value) -> {
			final SensorMetricsStore sensorMetricsStore = context.getPersistenceProvider()
					.getSensorMetricsStore(sensorId);
			sensorMetricsStore.save(message.getTimestamp(), value);
		});
	}

}
