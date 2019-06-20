package com.thyng.aspect.web.coap;

import org.eclipse.californium.core.CoapServer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.thyng.gateway.GatewayResource;
import com.thyng.gateway.HeartbeatResource;

@Configuration
public class GatewayConfiguration {

	@Value("${server.gateway.port}")
	private int port;
	
	@Bean(initMethod="start", destroyMethod="stop")
	public CoapServer coapServer(GatewayResource gatewayResource, 
			HeartbeatResource heartbeatResource, TelemetryResource telemetryResource){
		final CoapServer server = new CoapServer(port);
		server.add(gatewayResource, heartbeatResource, telemetryResource);
		return server;
	}
	
}
