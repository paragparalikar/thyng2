package com.thyng.gateway.service.server.coap;

import org.eclipse.californium.core.CoapResource;
import org.eclipse.californium.core.coap.CoAP.ResponseCode;
import org.eclipse.californium.core.server.resources.CoapExchange;

import com.thyng.gateway.model.Context;
import com.thyng.gateway.model.Message;
import com.thyng.gateway.model.MessageContext;
import com.thyng.gateway.service.message.MessageHandlerChain;

import lombok.SneakyThrows;

public class TelemetryResource extends CoapResource{
	private static final String URL = "telemetry";

	private final Context context;
	private final MessageContext messageContext;
	
	public TelemetryResource(Context context) {
		super(URL);
		this.context = context;
		this.messageContext = new MessageContext(context.getDetails());
	}
	
	@Override
	@SneakyThrows
	public void handlePOST(CoapExchange exchange) {
		final Message message = new Message(System.currentTimeMillis(), 
				exchange.getRequestPayload(), messageContext);
		final MessageHandlerChain chain = new MessageHandlerChain(context);
		try{
			chain.next(message);
			exchange.respond(ResponseCode.CREATED);
		}catch(Exception e){
			exchange.respond(ResponseCode.INTERNAL_SERVER_ERROR);
		}
	}
	
}
