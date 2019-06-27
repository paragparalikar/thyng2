package com.thyng.data.injestion.coap;

import org.eclipse.californium.core.CoapResource;
import org.eclipse.californium.core.coap.CoAP.ResponseCode;
import org.eclipse.californium.core.server.resources.CoapExchange;
import org.springframework.stereotype.Component;

import com.thyng.model.Telemetry;

import lombok.SneakyThrows;

@Component
public class TelemetryResource extends CoapResource {
	
	public TelemetryResource() {
		super("telemetries");
	}

	@Override
	@SneakyThrows
	public void handlePOST(CoapExchange exchange) {
		final String uuid = exchange.getQueryParameter("uuid");
		final Long sensorId = Long.parseLong(exchange.getQueryParameter("sensorId"));
		final Telemetry telemetry = Telemetry.builder()
				.sensorId(sensorId)
				.uuid(uuid)
				.payload(new String(exchange.getRequestPayload(),"UTF-8"))
				.build();
		System.out.println("Received sensorId:" + sensorId+", uuid:" + uuid);
		exchange.respond(ResponseCode.CREATED);
	}
	
}
