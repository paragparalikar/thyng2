package com.thyng.gateway.telemetry;

import com.thyng.gateway.model.Context;
import com.thyng.gateway.model.TelemetryMessage;

import lombok.SneakyThrows;

public class TelemetryMessageHandler {

	private final Context context;
	private final TelemetryMessageResolver telemetryPayloadResolver;
	private final TelemetryMessageNormalizer telemetryMessageNormalizer;
	private final TelemetryMessageDispatcher telemetryMessageDispatcher;
	private final TelemetryMessagePersister telemetryMessagePersister;

	public TelemetryMessageHandler(final Context context) {
		this.context = context;
		telemetryPayloadResolver = new TelemetryMessageResolver();
		telemetryMessageNormalizer = new TelemetryMessageNormalizer(context);
		telemetryMessageDispatcher = new TelemetryMessageDispatcher(context);
		telemetryMessagePersister = new TelemetryMessagePersister(context);
	}
	
	@SneakyThrows
	public TelemetryMessage handle(final String payload) {
		final TelemetryMessage message = telemetryPayloadResolver.resolve(payload);
		context.getEventBus().publish(TelemetryMessage.RECEIVED, message);
		telemetryMessageNormalizer.normalize(message);
		telemetryMessagePersister.persist(message);
		telemetryMessageDispatcher.dispatch(message);
		return message;
	}

}
