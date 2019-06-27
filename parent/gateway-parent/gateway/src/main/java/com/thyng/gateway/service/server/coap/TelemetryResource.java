package com.thyng.gateway.service.server.coap;

import java.nio.ByteBuffer;

import org.eclipse.californium.core.CoapResource;
import org.eclipse.californium.core.coap.CoAP.ResponseCode;
import org.eclipse.californium.core.server.resources.CoapExchange;

import com.thyng.gateway.model.Context;
import com.thyng.gateway.model.TelemetryMessage;

import lombok.SneakyThrows;

public class TelemetryResource extends CoapResource{
	private static final String URL = "telemetry";

	private final Context context;
	private final TelemetryPayloadResolver telemetryPayloadResolver;
	private final TelemetryMessageNormalizer telemetryMessageNormalizer;
	private final TelemetryMessageDispatcher telemetryMessageDispatcher;
	private final TelemetryMessagePersister telemetryMessagePersister;
	
	
	public TelemetryResource(final Context context) {
		super(URL);
		this.context = context;
		telemetryPayloadResolver = new TelemetryPayloadResolver();
		telemetryMessageNormalizer = new TelemetryMessageNormalizer(context);
		telemetryMessageDispatcher = new TelemetryMessageDispatcher(context);
		telemetryMessagePersister = new TelemetryMessagePersister(context);
	}
	
	@Override
	@SneakyThrows
	public void handlePOST(CoapExchange exchange) {
		final Long timestamp = System.currentTimeMillis();
		final TelemetryMessage message = TelemetryMessage.builder()
				.timestamp(timestamp)
				.values(telemetryPayloadResolver.resolve(exchange.getRequestPayload()))
				.build();
		context.getEventBus().publish(TelemetryMessage.RECEIVED, message);
		telemetryMessageNormalizer.normalize(message);
		telemetryMessagePersister.persist(message);
		telemetryMessageDispatcher.dispatch(message);
		
		final ByteBuffer buffer = ByteBuffer.allocate(Long.BYTES);
		buffer.asLongBuffer().put(0, timestamp);
		exchange.respond(ResponseCode.CREATED, buffer.array());
	}
}
