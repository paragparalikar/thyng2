package com.thyng.gateway.provider.persistence;

import java.io.IOException;

import com.thyng.gateway.model.Message;
import com.thyng.model.dto.TelemetryDTO;

public interface PersistenceProvider {
	String PERSISTED = "topic-message-persisted";

	Message save(Message message) throws Exception;

	TelemetryDTO getUnsentTelemetry(Long thingId, Long sensorId) throws IOException;
	
}
