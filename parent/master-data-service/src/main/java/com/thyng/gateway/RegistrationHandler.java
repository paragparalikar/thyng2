package com.thyng.gateway;


import org.springframework.stereotype.Component;

import com.thyng.model.dto.GatewayRegistrationDTO;
import com.thyng.service.GatewayService;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class RegistrationHandler extends AbstractSocketChannelHandler<GatewayRegistrationDTO> {

	private final GatewayService gatewayService;
	
	@Override
	protected void handle(GatewayRegistrationDTO input) {
		System.out.println(input);
		gatewayService.register(input.getGatewayId(), input.getHost(), input.getPort());
	}
	
}
