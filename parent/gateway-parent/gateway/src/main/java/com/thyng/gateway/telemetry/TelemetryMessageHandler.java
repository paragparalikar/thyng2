package com.thyng.gateway.telemetry;

import com.thyng.gateway.model.Context;
import com.thyng.gateway.model.TelemetryMessage;

import lombok.SneakyThrows;

public class TelemetryMessageHandler {

	private final Context context;
	private final TelemetryMessageResolver telemetryPayloadResolver;
	private final TelemetryMessageNormalizer telemetryMessageNormalizer;
	private final TelemetryMessagePersister telemetryMessagePersister;

	public TelemetryMessageHandler(final Context context) {
		this.context = context;
		telemetryPayloadResolver = new TelemetryMessageResolver();
		telemetryMessageNormalizer = new TelemetryMessageNormalizer(context);
		telemetryMessagePersister = new TelemetryMessagePersister(context);
	}
	
	@SneakyThrows
	public TelemetryMessage handle(final byte[] data) {
		final TelemetryMessage message = telemetryPayloadResolver.resolve(data);
		context.getEventBus().publish(TelemetryMessage.RECEIVED, message);
		telemetryMessageNormalizer.normalize(message);
		telemetryMessagePersister.persist(message);
		return message;
	}

}
