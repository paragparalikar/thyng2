package com.thyng.data.injestion.coap;

import java.nio.ByteBuffer;

import org.eclipse.californium.core.CoapResource;
import org.eclipse.californium.core.coap.CoAP.ResponseCode;
import org.eclipse.californium.core.server.resources.CoapExchange;
import org.springframework.stereotype.Component;

import com.thyng.gateway.GatewayService;

@Component
public class HeartbeatResource extends CoapResource{

	private final GatewayService gatewayService;
	
	public HeartbeatResource(GatewayService gatewayService) {
		super("heartbeats");
		this.gatewayService = gatewayService;
	}
	
	@Override
	public void handlePOST(CoapExchange exchange) {
		final Long gatewayId = ByteBuffer.wrap(exchange.getRequestPayload()).getLong();
		System.out.println("Got heartbeat from gateway with id "+gatewayId);
		exchange.respond(ResponseCode.CREATED);
	}

}
