package com.thyng.gateway.service.server.coap;

import org.eclipse.californium.core.CoapResource;
import org.eclipse.californium.core.coap.CoAP.ResponseCode;
import org.eclipse.californium.core.server.resources.CoapExchange;

import com.thyng.gateway.Context;
import com.thyng.gateway.service.message.MessageHandlerChain;
import com.thyng.gateway.service.message.handler.resolution.InvalidMessageException;
import com.thyng.model.dto.SensorDTO;
import com.thyng.model.dto.ThingDetailsDTO;

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
		boolean found = false;
		final Long thingId = Long.parseLong(exchange.getQueryParameter("thingId"));
		final Long sensorId = Long.parseLong(exchange.getQueryParameter("sensorId"));
		
		for(ThingDetailsDTO thing : context.getDetails().getThings()){
			if(thingId.equals(thing.getId())){
				for(SensorDTO sensor : thing.getSensors()){
					if(sensorId.equals(sensor.getId())){
						found = true;
						onMessage(exchange, thing, sensor);
						break;
					}
				}
				break;
			}
		}
		if(!found){
			exchange.respond(ResponseCode.NOT_FOUND);
		}
	}
	
	private void onMessage(CoapExchange exchange, 
			ThingDetailsDTO thing, SensorDTO sensor) throws Exception{
		final CoapMessage message = new CoapMessage(exchange);
		message.setThing(thing);
		message.setSensor(sensor);
		message.setTimestamp(System.currentTimeMillis());
		final MessageHandlerChain chain = new MessageHandlerChain(context);
		try{
			chain.next(message);
			exchange.respond(ResponseCode.CREATED);
		}catch(InvalidMessageException ime){
			exchange.respond(ResponseCode.BAD_REQUEST);
		}catch(Exception e){
			exchange.respond(ResponseCode.INTERNAL_SERVER_ERROR);
		}
	}
	
}
