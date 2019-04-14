package com.thyng.gateway;

import org.eclipse.californium.core.CoapResource;
import org.eclipse.californium.core.coap.CoAP.ResponseCode;
import org.eclipse.californium.core.coap.MediaTypeRegistry;
import org.eclipse.californium.core.server.resources.CoapExchange;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.esotericsoftware.kryo.io.Input;
import com.thyng.model.Serializer;
import com.thyng.model.dto.GatewayExtendedDetailsDTO;
import com.thyng.model.dto.GatewayRegistrationDTO;
import com.thyng.model.entity.Gateway;
import com.thyng.model.mapper.GatewayMapper;
import com.thyng.service.GatewayService;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class GatewayResource extends CoapResource {
	
	@Autowired
	private GatewayMapper gatewayMapper;
	
	@Autowired
	private GatewayService gatewayService;
	
	public GatewayResource() {
		super("gateways");
	}
	
	@Override
	public void handlePOST(CoapExchange exchange) {
		try{
			final String host = exchange.getSourceAddress().getHostAddress();
			final GatewayRegistrationDTO gatewayRegistrationDTO = Serializer.kryo()
					.readObject(new Input(exchange.getRequestPayload()), GatewayRegistrationDTO.class);
			final Gateway gateway = gatewayService.register(gatewayRegistrationDTO.getGatewayId(), 
					host, gatewayRegistrationDTO.getPort());
			final GatewayExtendedDetailsDTO gatewayDetailsDTO = gatewayMapper.toExtendedDTO(gateway);
			exchange.respond(ResponseCode.CREATED, Serializer.write(gatewayDetailsDTO), 
					MediaTypeRegistry.APPLICATION_OCTET_STREAM);
		}catch(Exception e){
			log.error("Failed to register gateway", e);
			exchange.respond(ResponseCode.INTERNAL_SERVER_ERROR);
		}
	}
	
	@Override
	public void handleGET(CoapExchange exchange) {
		exchange.respond(ResponseCode.CONTENT, "Hello");
	}
		
}
