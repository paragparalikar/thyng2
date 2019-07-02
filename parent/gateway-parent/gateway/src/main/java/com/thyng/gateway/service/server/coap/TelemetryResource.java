package com.thyng.gateway.service.server.coap;

import java.nio.ByteBuffer;

import org.eclipse.californium.core.CoapResource;
import org.eclipse.californium.core.coap.CoAP.ResponseCode;
import org.eclipse.californium.core.server.resources.CoapExchange;

import com.thyng.gateway.Constant;
import com.thyng.gateway.model.Context;
import com.thyng.gateway.model.TelemetryMessage;
import com.thyng.gateway.telemetry.TelemetryMessageHandler;

import lombok.SneakyThrows;

public class TelemetryResource extends CoapResource{
	private static final String URL = "telemetry";

	private final TelemetryMessageHandler telemetryMessageHandler;
	
	
	public TelemetryResource(final Context context) {
		super(URL);
		telemetryMessageHandler = new TelemetryMessageHandler(context);
	}
	
	@Override
	@SneakyThrows
	public void handlePOST(CoapExchange exchange) {
		final String payload = new String(exchange.getRequestPayload(), Constant.CHARSET);
		final TelemetryMessage message = telemetryMessageHandler.handle(payload);
		final ByteBuffer buffer = ByteBuffer.allocate(Long.BYTES);
		buffer.asLongBuffer().put(0, message.getTimestamp());
		exchange.respond(ResponseCode.CREATED, buffer.array());
	}
}
