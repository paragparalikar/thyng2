package com.thyng.gateway;

import java.io.IOException;

import org.springframework.context.annotation.Scope;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import com.thyng.model.dto.GatewayDetailsDTO;
import com.thyng.model.entity.Gateway;
import com.thyng.model.entity.GatewayRegistration;
import com.thyng.model.enumeration.GatewayOps;
import com.thyng.model.mapper.GatewayMapper;

import lombok.RequiredArgsConstructor;

@Component 
@Scope("prototype")
@RequiredArgsConstructor
public class ConfigurationHandler {

	private final GatewayMapper gatewayMapper;
	
	@EventListener
	public void onGatewayRegistered(GatewayRegistration gatewayRegistration) throws IOException{
		final Gateway gateway = gatewayRegistration.getGateway();
		final GatewayDetailsDTO dto = gatewayMapper.dto(gateway);
		BinaryClient.builder()
			.host(gateway.getRegistration().getHost())
			.port(gateway.getRegistration().getPort())
			.gatewayOps(GatewayOps.CONFIGURATION)
			.payload(dto)
			.build().send();
	}
	
}
