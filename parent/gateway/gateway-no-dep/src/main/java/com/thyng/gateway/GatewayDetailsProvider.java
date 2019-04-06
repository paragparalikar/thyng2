package com.thyng.gateway;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.Writer;
import java.net.HttpURLConnection;

import com.google.gson.Gson;
import com.google.gson.JsonIOException;
import com.thyng.gateway.service.server.coap.CoapServerService;
import com.thyng.gateway.util.HttpUtil;
import com.thyng.model.dto.GatewayDetailsDTO;
import com.thyng.model.dto.GatewayRegistrationDTO;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
public class GatewayDetailsProvider{
	public static final String KEY_CONFIGURATION_URL = "thyng.server.url.registration";
	
	private final Gson gson;
	private final PropertyProvider properties;
		
	public GatewayDetailsDTO load() throws Exception{
		final File file = getConfigurationFile();
		log.info("Loading gateway details from "+file.getAbsolutePath());
		final Reader reader = new FileReader(file);
		return gson.fromJson(reader, GatewayDetailsDTO.class);
	}
	
	public GatewayDetailsDTO fetch() throws Exception{
		final String url = properties.get(Constant.KEY_URL_SERVER, "http://www.thyng.io") 
				+ properties.get(KEY_CONFIGURATION_URL, "/gateways/registrations");
		log.info("Registering and Fetching gateway details from "+url);
		final HttpURLConnection connection = HttpUtil.build(url, 
				properties.get(Constant.KEY_USERNAME, null), 
				properties.get(Constant.KEY_PASSWORD, null));
		connection.setRequestMethod("POST");
		connection.setRequestProperty("Content-Type", 
				"application/json; charset=utf8");
		connection.setDoInput(true);
		connection.setDoOutput(true);
		connection.getOutputStream().write(buildRegistrationRequestBody());
		connection.getOutputStream().flush();
		if(200 == connection.getResponseCode()){
			final Reader reader = new InputStreamReader(connection.getInputStream());
			return gson.fromJson(reader, GatewayDetailsDTO.class);
		}else{
			throw new Exception("Failed to register gateway - " + 
					connection.getResponseCode() + " : " + 
					connection.getResponseMessage());
		}
	}
	
	public GatewayDetailsDTO save(GatewayDetailsDTO dto) throws JsonIOException, IOException{
		final File file = getConfigurationFile();
		log.info("Persisting gateway details to "+file.getAbsolutePath());
		try(Writer writer = new FileWriter(file, false)){
			gson.toJson(dto, writer);
			return dto;
		}
	}
	
	private byte[] buildRegistrationRequestBody(){
		final GatewayRegistrationDTO dto = new GatewayRegistrationDTO();
		dto.setGatewayId(properties.getLong(Constant.KEY_GATEWAY_ID, null));
		dto.setPort(properties.getInteger(CoapServerService.KEY_COAP_PORT, 9090));
		return gson.toJson(dto).getBytes();
	}
		
	private File getConfigurationFile() throws IOException{
		final String path = properties.get(Constant.KEY_STORAGE, null)
				+ properties.get(Constant.KEY_GATEWAY_ID, null) + ".json";
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
