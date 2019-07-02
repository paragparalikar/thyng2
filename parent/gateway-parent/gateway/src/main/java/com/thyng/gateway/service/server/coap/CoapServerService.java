package com.thyng.gateway.service.server.coap;

import org.eclipse.californium.core.CoapServer;

import com.thyng.gateway.model.Context;
import com.thyng.gateway.service.Service;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class CoapServerService extends CoapServer implements Service{
	public static final String KEY_COAP_PORT = "thyng.gateway.coap.server.port";
	
	public CoapServerService(Context context) {
		super(context.getProperties().getInteger(KEY_COAP_PORT, 5683));
		setExecutor(context.getExecutor(), true);
		add(new TelemetryResource(context));
	}
	
	@Override
	public synchronized void start() {
		log.info("Starting COAP server at port "+getEndpoints().get(0).getAddress().getPort());
		super.start();
		log.info("Successfully started COAP server");
	}
	
	@Override
	public synchronized void stop() {
		log.info("Stopping COAP server");
		super.stop();
		log.info("Successfully stopped COAP server");
	}
	
}