package com.thyng.gateway.provider.persistence;

import com.thyng.gateway.model.Message;
import com.thyng.model.dto.GatewayDetailsDTO;

public interface PersistenceProvider {

	Message save(Message message) throws Exception;

	PersistentTelemetry getUnsentTelemetry(Long thingId, Long sensorId) throws Exception;

	void markSent(PersistentTelemetry telemetry) throws Exception;

	GatewayDetailsDTO save(GatewayDetailsDTO dto) throws Exception;

	GatewayDetailsDTO load() throws Exception;
	
}
