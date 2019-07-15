package com.thyng.gateway.telemetry;

import com.thyng.gateway.model.Context;
import com.thyng.gateway.model.TelemetryMessage;
import com.thyng.gateway.provider.persistence.TelemetryStore;
import com.thyng.model.Telemetry;
import com.thyng.model.TelemetryRequest;
import com.thyng.model.TelemetryResponse;
import com.thyng.model.dto.SensorDTO;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
public class TelemetryMessageDispatcher {

	private final Context context;
	
	public void dispatch(TelemetryMessage message) {
		message.getValues().keySet().forEach(sensorId -> {
			final SensorDTO sensor = context.getSensor(sensorId);
			final TelemetryStore telemetryStore = context
					.getPersistenceProvider()
					.getTelemetryStore(sensorId);
			synchronized(telemetryStore) {
				if(sensor.getBatchSize() <= telemetryStore.getCount()) {
					doDispatch(telemetryStore);
				}
			}
		});
	}
	
	private void doDispatch(final TelemetryStore telemetryStore) {
		final Telemetry telemetry = telemetryStore.read();
		try {
			final TelemetryRequest request = new TelemetryRequest(telemetry);
			final TelemetryResponse response = context.getClient().execute(request);
		}catch(Throwable throwable) {
			log.error("Failed to dispatch telemetry to server\n" 
					+ throwable.getMessage(), throwable);
			telemetryStore.rollback(telemetry.getUuid());
		}
	}

}
