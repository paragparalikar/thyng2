package com.thyng.aspect.web.coap;

import org.eclipse.californium.core.CoapResource;
import org.eclipse.californium.core.coap.CoAP.ResponseCode;
import org.eclipse.californium.core.server.resources.CoapExchange;
import org.springframework.stereotype.Component;

import com.thyng.model.dto.TelemetryDTO;

import lombok.SneakyThrows;

@Component
public class TelemetryResource extends CoapResource {
	
	public TelemetryResource() {
		super("telemetries");
	}

	@Override
	@SneakyThrows
	public void handlePOST(CoapExchange exchange) {
		final Long gatewayId = Long.parseLong(exchange.getQueryParameter("gatewayId"));
		final Long thingId = Long.parseLong(exchange.getQueryParameter("thingId"));
		final Long sensorId = Long.parseLong(exchange.getQueryParameter("sensorId"));
		final Byte dataType = Byte.parseByte(exchange.getQueryParameter("dataType"));
		final TelemetryDTO telemetry = new TelemetryDTO(thingId, sensorId, dataType, exchange.getRequestPayload());
		System.out.println("Received thingId:"+thingId+", sensorId:"+sensorId+", dataType:"+dataType);
		exchange.respond(ResponseCode.CREATED);
	}
	
}
