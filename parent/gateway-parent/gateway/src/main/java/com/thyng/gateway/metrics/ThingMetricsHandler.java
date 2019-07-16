package com.thyng.gateway.metrics;

import com.thyng.gateway.model.Context;
import com.thyng.gateway.model.ThingMetrics;

import lombok.SneakyThrows;

public class ThingMetricsHandler {

	private final Context context;
	private final ThingMetricsResolver metricsPayloadResolver;
	private final ThingMetricsNormalizer metricsMessageNormalizer;
	private final ThingMetricsPersister metricsMessagePersister;

	public ThingMetricsHandler(final Context context) {
		this.context = context;
		metricsPayloadResolver = new ThingMetricsResolver();
		metricsMessageNormalizer = new ThingMetricsNormalizer(context);
		metricsMessagePersister = new ThingMetricsPersister(context);
	}
	
	@SneakyThrows
	public ThingMetrics handle(final byte[] data) {
		final ThingMetrics message = metricsPayloadResolver.resolve(data);
		context.getEventBus().publish(ThingMetrics.RECEIVED, message);
		metricsMessageNormalizer.normalize(message);
		metricsMessagePersister.persist(message);
		return message;
	}

}
