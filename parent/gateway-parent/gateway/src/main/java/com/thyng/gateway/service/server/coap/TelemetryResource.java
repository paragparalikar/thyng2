package com.thyng.gateway.service.server.coap;

import java.io.UnsupportedEncodingException;
import java.nio.ByteBuffer;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import org.eclipse.californium.core.CoapResource;
import org.eclipse.californium.core.coap.CoAP.ResponseCode;
import org.eclipse.californium.core.server.resources.CoapExchange;

import com.thyng.gateway.model.Constant;
import com.thyng.gateway.model.Context;
import com.thyng.gateway.model.Message;
import com.thyng.util.StringUtil;

import lombok.SneakyThrows;

public class TelemetryResource extends CoapResource{
	private static final String URL = "telemetry";

	private final Context context;
	
	public TelemetryResource(Context context) {
		super(URL);
		this.context = context;
	}
	
	@Override
	@SneakyThrows
	public void handlePOST(CoapExchange exchange) {
		final Long timestamp = System.currentTimeMillis();
		final Message message = Message.builder()
				.timestamp(timestamp)
				.values(resolve(exchange.getRequestPayload()))
				.build();
		context.getEventBus().publishAsync(Message.RECEIVED, message);
		final ByteBuffer buffer = ByteBuffer.allocate(Long.BYTES);
		buffer.asLongBuffer().put(0, timestamp);
		exchange.respond(ResponseCode.CREATED, buffer.array());
	}
	
	private Map<Long, String> resolve(final byte[] payload) throws UnsupportedEncodingException{
		final Map<Long, String> values = new HashMap<>();
		Arrays.asList(new String(payload,Constant.CHARSET).split(Constant.LINEFEED))
			.forEach(line -> {
				if(StringUtil.hasText(line)) {
					final String[] tokens = line.split(",");
					if(2 <= tokens.length) {
						values.put(Long.parseLong(tokens[0]), tokens[1]);
					}
				}
			});
		return values;
	}
	
}
