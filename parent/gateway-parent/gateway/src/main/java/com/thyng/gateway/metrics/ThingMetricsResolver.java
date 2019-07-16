package com.thyng.gateway.metrics;

import java.nio.ByteBuffer;
import java.util.HashMap;
import java.util.Map;

import com.thyng.gateway.model.ThingMetrics;

public class ThingMetricsResolver {

	public ThingMetrics resolve(final byte[] data) {
		final ByteBuffer byteBuffer = ByteBuffer.wrap(data);
		final Map<Long, Double> values = new HashMap<>();
		while(byteBuffer.hasRemaining()) {
			values.put(byteBuffer.getLong(), byteBuffer.getDouble());
		}
		return ThingMetrics.builder()
				.timestamp(System.currentTimeMillis())
				.values(values)
				.build();
	}

}
