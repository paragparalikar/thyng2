package com.thyng.gateway.client;

import com.thyng.model.dto.GatewayConfigurationDTO;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class CoapGatewayClient implements GatewayClient {

	private final GatewayConfigurationDTO details;
	
	@Override
	public void commit(String uuid) {
		details.getHost();
	}

	@Override
	public void rollback(String uuid) {

	}

}
