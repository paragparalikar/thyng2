package com.thyng.gateway.client;

import java.util.HashMap;
import java.util.Map;

import org.springframework.stereotype.Component;

import com.thyng.entity.Gateway;
import com.thyng.entity.Sensor;
import com.thyng.mapper.GatewayMapper;
import com.thyng.model.dto.GatewayConfigurationDTO;
import com.thyng.service.GatewayService;
import com.thyng.service.SensorService;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class GatewayClientFactory {

	private final GatewayMapper gatewayMapper;
	private final SensorService sensorService;
	private final GatewayService gatewayService;
	private final Map<Long,GatewayClient> gatewayClientCache = new HashMap<>();
	
	public GatewayClient get(final Long sensorId) {
		return gatewayClientCache.computeIfAbsent(sensorId, id -> {
			final Sensor sensor = sensorService.findById(id);
			final Long gatewayId = sensor.getThing().getGateway().getId();
			final Gateway gateway = gatewayService.findByIdIncludeThings(gatewayId);
			final GatewayConfigurationDTO details = gatewayMapper.toExtendedDTO(gateway);
			return new HttpGatewayClient(details);
		});
	}

}
