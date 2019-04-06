package com.thyng.gateway.service.server.coap;

import org.eclipse.californium.core.server.resources.CoapExchange;

import com.thyng.gateway.service.message.Message;

public class CoapMessage extends Message{

	private final CoapExchange exchange;
	
	public CoapMessage(CoapExchange exchange) {
		this.exchange = exchange;
		super.setPayload(exchange.getRequestPayload());
	}
	
	public CoapExchange getExchange() {
		return exchange;
	}
	
}