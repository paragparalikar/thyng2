package com.thyng.gateway.provider.details;

import org.eclipse.californium.core.CoapClient;
import org.eclipse.californium.core.CoapResponse;
import org.eclipse.californium.core.coap.MediaTypeRegistry;

import com.esotericsoftware.kryo.io.Input;
import com.thyng.gateway.model.Constant;
import com.thyng.gateway.provider.persistence.PersistenceProvider;
import com.thyng.gateway.provider.property.PropertyProvider;
import com.thyng.gateway.service.server.coap.CoapServerService;
import com.thyng.model.Serializer;
import com.thyng.model.dto.GatewayDetailsDTO;
import com.thyng.model.dto.GatewayRegistrationDTO;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
public class DetailsProvider{
	public static final String KEY_CONFIGURATION_URL = "thyng.server.url.registration";
	
	private final PropertyProvider properties;
	private final PersistenceProvider persistenceProvider;
		
	public GatewayDetailsDTO get() throws Exception{
		try{
			return persistenceProvider.load();
		}catch(Exception e){
			log.error("Could not load details from persistence provider, fetching from server");
			return persistenceProvider.save(fetch());
		}
	}
	
	private GatewayDetailsDTO fetch() throws Exception{
		log.info("Registering and Fetching gateway details");
		
		final CoapClient coapClient = new CoapClient("coap", 
				properties.get(Constant.KEY_URL_SERVER, "www.thyng.io"),
				properties.getInteger(Constant.KEY_URL_SERVER_PORT, 5684),
				properties.get(KEY_CONFIGURATION_URL, "gateways"));
		final CoapResponse coapResponse = coapClient.post(buildRegistrationRequestBody(), 
				MediaTypeRegistry.APPLICATION_OCTET_STREAM);
		if(null != coapResponse && coapResponse.isSuccess()){
			return Serializer.kryo().readObject(new Input(coapResponse.getPayload()), GatewayDetailsDTO.class);
		}
		throw new Exception("Failed to fetch gateway details from server "
		+ (null != coapResponse ? coapResponse.getCode() + ", "+coapResponse.getResponseText() : ""));
	}
	
	private byte[] buildRegistrationRequestBody(){
		final GatewayRegistrationDTO dto = new GatewayRegistrationDTO();
		dto.setGatewayId(properties.getLong(Constant.KEY_GATEWAY_ID, null));
		dto.setPort(properties.getInteger(CoapServerService.KEY_COAP_PORT, 5683));
		return Serializer.write(dto);
	}
		
}