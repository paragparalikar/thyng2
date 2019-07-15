package com.thyng.gateway.telemetry;

import java.nio.ByteBuffer;
import java.util.HashMap;
import java.util.Map;

import com.thyng.gateway.model.TelemetryMessage;

public class TelemetryMessageResolver {

	public TelemetryMessage resolve(final byte[] data) {
		final ByteBuffer byteBuffer = ByteBuffer.wrap(data);
		final Map<Long, Double> values = new HashMap<>();
		while(byteBuffer.hasRemaining()) {
			values.put(byteBuffer.getLong(), byteBuffer.getDouble());
		}
		return TelemetryMessage.builder()
				.timestamp(System.currentTimeMillis())
				.values(values)
				.build();
	}

}
