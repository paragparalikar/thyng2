package com.thyng.gateway.client;

import java.util.HashMap;
import java.util.Map;

import org.springframework.stereotype.Component;

import com.thyng.entity.Gateway;
import com.thyng.mapper.GatewayMapper;
import com.thyng.model.dto.GatewayConfigurationDTO;
import com.thyng.service.GatewayService;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class GatewayClientFactory {

	private final GatewayMapper gatewayMapper;
	private final GatewayService gatewayService;
	private final Map<Long,GatewayClient> gatewayClientCache = new HashMap<>();
	
	public GatewayClient get(final Long gatewayId) {
		return gatewayClientCache.computeIfAbsent(gatewayId, id -> {
			final Gateway gateway = gatewayService.findByIdIncludeThings(gatewayId);
			final GatewayConfigurationDTO details = gatewayMapper.toExtendedDTO(gateway);
			switch(gateway.getProtocol()) {
				case HTTP: return new HttpGatewayClient(details);
				case COAP: return new CoapGatewayClient(details);
				case MQTT: return null;
				default: return null;
			}
		});
	}

}
