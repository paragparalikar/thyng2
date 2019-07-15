package com.thyng.gateway.service;

import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

import com.thyng.gateway.model.Context;
import com.thyng.gateway.provider.persistence.TelemetryStore;
import com.thyng.model.Telemetry;
import com.thyng.model.TelemetryRequest;
import com.thyng.model.TelemetryResponse;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
public class TelemetryDispatchService implements Service, Runnable {

	private final Context context;
	private ScheduledFuture<?> future;
	
	@Override
	public void start() throws Exception {
		final Long delay = context.getProperties().getLong("thyng.gateway.telemetry.dispatch.delay" , 1000l);
		log.info("Starting telemetry dispatch service width interval " + delay);
		future = context.getExecutor().scheduleWithFixedDelay(this, 0, delay, TimeUnit.MILLISECONDS);
	}

	@Override
	public void stop() throws Exception {
		if(null != future && !future.isDone()) {
			log.info("Stopping telemetry dispatch service");
			future.cancel(false);
		}
	}
	
	@Override
	public void run() {
		final Long transactionId = System.currentTimeMillis();
		try {
			boolean telemetryAvailable = false;
			final Long[] sensorIds = context.getSensorIds().toArray(new Long[0]);
			final long[] primitiveSensorIds = new long[sensorIds.length];
			final byte[][] data = new byte[sensorIds.length][];
			for(int index = 0; index < sensorIds.length; index++) {
				final Long sensorId = sensorIds[index];
				primitiveSensorIds[index] = sensorId;
				final TelemetryStore telemetryStore = context.getPersistenceProvider().getTelemetryStore(sensorId);
				data[index] = telemetryStore.read(transactionId);
				telemetryAvailable = telemetryAvailable || 0 < data[index].length;
			}
			if(telemetryAvailable) {
				final Long gatewayId = context.getDetails().getId();
				final Telemetry rawTelemetry = new Telemetry(gatewayId, transactionId, primitiveSensorIds, data);
				final TelemetryResponse response = context.getClient().execute(new TelemetryRequest(rawTelemetry));
			}
		}catch(Exception exception) {
			log.error("Failed to send telemetry", exception);
			context.getSensorIds().forEach(sensorId -> {
				final TelemetryStore telemetryStore = context.getPersistenceProvider().getTelemetryStore(sensorId);
				telemetryStore.rollback(transactionId);
			});
		}
	}

}
