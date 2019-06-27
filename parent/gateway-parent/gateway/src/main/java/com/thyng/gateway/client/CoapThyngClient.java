package com.thyng.gateway.client;

import java.nio.ByteBuffer;

import org.eclipse.californium.core.CoapClient;
import org.eclipse.californium.core.CoapResponse;
import org.eclipse.californium.core.coap.CoAP.Type;
import org.eclipse.californium.core.coap.MediaTypeRegistry;
import org.eclipse.californium.core.coap.Request;

import com.esotericsoftware.kryo.io.Input;
import com.thyng.gateway.model.Constant;
import com.thyng.gateway.provider.property.PropertyProvider;
import com.thyng.gateway.service.server.coap.CoapServerService;
import com.thyng.model.Serializer;
import com.thyng.model.Telemetry;
import com.thyng.model.dto.GatewayConfigurationDTO;
import com.thyng.model.dto.GatewayRegistrationDTO;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
public class CoapThyngClient implements ThyngClient {
	public static final String KEY_HEARTBEAT_URL = "thyng.server.url.heartbeat";
	public static final String KEY_CONFIGURATION_URL = "thyng.server.url.registration";
	public static final String KEY_TELEMETRY_URL = "thyng.server.url.telemetry";

	private final PropertyProvider properties;
	
	@Override
	public GatewayConfigurationDTO registerAndFetchDetails() throws Exception{
		log.info("Registering and Fetching gateway details");
		final CoapClient coapClient = new CoapClient("coap", 
				properties.get(Constant.KEY_URL_SERVER, "www.thyng.io"),
				properties.getInteger(Constant.KEY_URL_SERVER_PORT, 5684),
				properties.get(KEY_CONFIGURATION_URL, "gateways"));
		final CoapResponse coapResponse = coapClient.post(buildRegistrationRequestBody(), 
				MediaTypeRegistry.APPLICATION_OCTET_STREAM);
		if(null != coapResponse && coapResponse.isSuccess()){
			return Serializer.kryo().readObject(new Input(coapResponse.getPayload()), GatewayConfigurationDTO.class);
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
	
	@Override
	public void heartbeat() throws Exception{
		log.debug("Sending heartbeat");
		final Long id = properties.getLong(Constant.KEY_GATEWAY_ID, null);
		final byte[] value = ByteBuffer.allocate(Long.BYTES).putLong(id).array();
		final CoapResponse response = new CoapClient("coap", 
				properties.get(Constant.KEY_URL_SERVER, "www.thyng.io"),
				properties.getInteger(Constant.KEY_URL_SERVER_PORT, 5684),
				properties.get(KEY_HEARTBEAT_URL, "heartbeats"))
		.post(value, MediaTypeRegistry.APPLICATION_OCTET_STREAM);
		log.debug("Heartbeat response success : " + (null != response && response.isSuccess()));
	}
	
	@Override
	public void send(Telemetry telemetry) throws Exception{
		final CoapClient client = new CoapClient("coap", 
				properties.get(Constant.KEY_URL_SERVER, "www.thyng.io"),
				properties.getInteger(Constant.KEY_URL_SERVER_PORT, 5684),
				properties.get(KEY_TELEMETRY_URL, "telemetries"));
		final Request request = Request.newPost();
		request.setType(Type.CON);
		request.setPayload(telemetry.toByteArray());
		request.getOptions().setContentFormat(MediaTypeRegistry.APPLICATION_OCTET_STREAM);
		request.getOptions().setUriQuery("sensorId="+telemetry.getSensorId()
				+"&uuid="+telemetry.getUuid());
		final CoapResponse response = client.advanced(request);
		if(!response.isSuccess()) {
			throw new Exception("Failure respones from server, "
					+ "code: "+response.getCode()+", "
					+ "text: "+response.getResponseText());
		}
	}
	
	@Override
	public void sendThingStatus(Long thingId, Boolean active) throws Exception{
		final CoapClient client = client("thingStatus");
		final Request request = get("thingId="+thingId+"&active="+active);
		final CoapResponse response = client.advanced(request);
		if(null != response && response.isSuccess()){
			log.info("Successfully reported status");
		}else{
			log.error("Failed to report status");
		}
	}
	
	@Override
	public void sendSensorStatus(Long sensorId, Boolean active) throws Exception{
		final CoapClient client = client("sensorStatus");
		final Request request = get("sensorId="+sensorId+"&active="+active);
		final CoapResponse response = client.advanced(request);
		if(null != response && response.isSuccess()){
			log.info("Successfully reported status");
		}else{
			log.error("Failed to report status");
		}
	}
	
	private CoapClient client(String path){
		return new CoapClient("coap", 
				properties.get(Constant.KEY_URL_SERVER, "www.thyng.io"),
				properties.getInteger(Constant.KEY_URL_SERVER_PORT, 5684),
				path);
	}
	
	private Request post(byte[] payload, String query){
		final Request request = Request.newPost();
		request.setType(Type.CON);
		request.setPayload(payload);
		request.getOptions().setContentFormat(MediaTypeRegistry.APPLICATION_OCTET_STREAM);
		request.getOptions().setUriQuery(query);
		return request;
	}
	
	private Request get(String query){
		final Request request = Request.newGet();
		request.setType(Type.CON);
		request.getOptions().setUriQuery(query);
		return request;
	}

}
