package com.thyng.gateway;

import java.util.HashMap;
import java.util.Map;

import org.springframework.stereotype.Component;

import com.thyng.entity.Gateway;
import com.thyng.netty.Client;
import com.thyng.netty.OioClient;
import com.thyng.service.GatewayService;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class GatewayClientFactory {

	private final GatewayService gatewayService;
	private final Map<Long, Client> gatewayClientCache = new HashMap<>();
	
	public Client get(final Long gatewayId) {
		return gatewayClientCache.computeIfAbsent(gatewayId, id -> {
			final Gateway gateway = gatewayService.findById(gatewayId);
			return new OioClient(gateway.getPort(), gateway.getHost());
		});
	}

}
