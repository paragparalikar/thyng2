package com.thyng.gateway.service;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

import com.thyng.gateway.model.Context;
import com.thyng.gateway.provider.persistence.SensorMetricsStore;
import com.thyng.model.GatewayMetrics;
import com.thyng.model.GatewayMetricsRequest;
import com.thyng.model.GatewayMetricsResponse;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
public class MetricsDispatchService implements Service, Runnable {

	private final Context context;
	private ScheduledFuture<?> future;
	
	@Override
	public void start() throws Exception {
		final Long delay = context.getProperties().getLong("thyng.gateway.metrics.dispatch.delay" , 1000l);
		log.info("Starting metrics dispatch service width interval " + delay);
		future = context.getExecutor().scheduleWithFixedDelay(this, 0, delay, TimeUnit.MILLISECONDS);
	}

	@Override
	public void stop() throws Exception {
		if(null != future && !future.isDone()) {
			log.info("Stopping metrics dispatch service");
			future.cancel(false);
		}
	}
	
	@Override
	public void run() {
		final Long transactionId = System.currentTimeMillis();
		try {
			boolean metricsAvailable = false;
			final Map<Long, Map<Long, Double>> sensorMetrics = new HashMap<>();
			for(Long sensorId : context.getSensorIds()) {
				final SensorMetricsStore sensorMetricsStore = context.getPersistenceProvider().getSensorMetricsStore(sensorId);
				final Map<Long, Double> metrics = sensorMetricsStore.read(transactionId);
				metricsAvailable = metricsAvailable || !metrics.isEmpty();
				sensorMetrics.put(sensorId, metrics);
			}
			if(metricsAvailable) {
				final Long gatewayId = context.getDetails().getId();
				final GatewayMetrics gatewayMetrics = new GatewayMetrics(gatewayId, transactionId, sensorMetrics);
				final GatewayMetricsResponse response = context.getClient().execute(new GatewayMetricsRequest(gatewayMetrics));
			}
		}catch(Exception exception) {
			log.error("Failed to send metrics", exception);
			context.getSensorIds().forEach(sensorId -> {
				final SensorMetricsStore sensorMetricsStore = context.getPersistenceProvider().getSensorMetricsStore(sensorId);
				sensorMetricsStore.rollback(transactionId);
			});
		}
	}

}
