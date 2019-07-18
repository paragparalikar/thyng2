package com.thyng.metrics.resolver;

import java.nio.ByteBuffer;
import java.util.HashMap;
import java.util.Map;

import com.thyng.entity.MetricsSchema;
import com.thyng.model.Metrics;

public class DefaultMetricsResolver implements MetricsResolver {

	@Override
	public void resolve(Metrics metrics, MetricsSchema schema) {
		final ByteBuffer buffer = ByteBuffer.wrap(metrics.getData());
		
		if(buffer.getInt() != buffer.remaining()) { 
			throw new RuntimeException("Message size does not match with actual data size");
		}
		
		if(0 != buffer.getInt()) {
			throw new RuntimeException("Message schema is not native");
		}
		
		while(buffer.hasRemaining()) {
			final Long sensorId = buffer.getLong();
			final Map<Long, Double> values = metrics.getSensorIdValues().computeIfAbsent(sensorId, id -> new HashMap<>());
			values.put(buffer.getLong(), buffer.getDouble());
		}
	}

}
