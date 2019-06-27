package com.thyng.gateway.service.server.coap;

import org.eclipse.californium.core.CoapServer;

import com.thyng.gateway.model.Context;
import com.thyng.gateway.service.Service;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class CoapServerService extends CoapServer implements Service{
	public static final String KEY_COAP_PORT = "thyng.gateway.coap.server.port";
	
	private final Context context;
	
	public CoapServerService(Context context) {
		super(context.getProperties().getInteger(KEY_COAP_PORT, 5683));
		setExecutor(context.getExecutor(), true);
		add(new TelemetryResource(context));
		this.context = context;
	}
	
}