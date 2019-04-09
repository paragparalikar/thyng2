package com.thyng.gateway;

import org.eclipse.californium.core.CoapServer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class GatewayConfiguration {

	@Value("${server.gateway.port}")
	private int port;
	
	@Bean(initMethod="start", destroyMethod="stop")
	public CoapServer coapServer(GatewayResource gatewayResource, 
			HeartbeatResource heartbeatResource){
		final CoapServer server = new CoapServer(port);
		server.add(gatewayResource, heartbeatResource);
		return server;
	}
	
}
