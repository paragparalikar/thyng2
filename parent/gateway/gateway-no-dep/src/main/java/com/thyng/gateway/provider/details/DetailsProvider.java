package com.thyng.gateway.provider.details;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

import org.eclipse.californium.core.CoapClient;
import org.eclipse.californium.core.CoapHandler;
import org.eclipse.californium.core.CoapResponse;
import org.eclipse.californium.core.coap.MediaTypeRegistry;

import com.esotericsoftware.kryo.io.Input;
import com.esotericsoftware.kryo.io.Output;
import com.thyng.gateway.model.Constant;
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
		
	public GatewayDetailsDTO load() throws Exception{
		final File file = getConfigurationFile();
		log.info("Loading gateway details from "+file.getAbsolutePath());
		try(final Input input = new Input(new FileInputStream(file))){
			return Serializer.kryo().readObject(input, GatewayDetailsDTO.class);
		}
	}
	
	public GatewayDetailsDTO fetch() throws Exception{
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
	
	public GatewayDetailsDTO save(GatewayDetailsDTO dto) throws IOException{
		final File file = getConfigurationFile();
		log.info("Persisting gateway details to "+file.getAbsolutePath());
		try(final Output output = new Output(new FileOutputStream(file))){
			Serializer.kryo().writeObject(output, dto);
			return dto;
		}
	}
	
	private byte[] buildRegistrationRequestBody(){
		final GatewayRegistrationDTO dto = new GatewayRegistrationDTO();
		dto.setGatewayId(properties.getLong(Constant.KEY_GATEWAY_ID, null));
		dto.setPort(properties.getInteger(CoapServerService.KEY_COAP_PORT, 5683));
		return Serializer.write(dto);
	}
		
	private File getConfigurationFile() throws IOException{
		final String path = properties.get(Constant.KEY_STORAGE, null)
				+ properties.get(Constant.KEY_GATEWAY_ID, null) + ".binary";
		final File file = new File(path);
		if(!file.exists()){
			log.info("Could not find gateway details file "
					+path+"\ncreating new file...");
			file.getParentFile().mkdirs();
			file.createNewFile();
		}
		return file;
	}
	
}


class GatewayDetailsHandler implements CoapHandler{

	@Override
	public void onLoad(CoapResponse response) {
		
	}

	@Override
	public void onError() {
		
	}
	
}