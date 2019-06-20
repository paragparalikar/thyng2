package com.thyng.gateway.provider.client;

import com.thyng.gateway.model.TelemetryRequest;
import com.thyng.model.dto.GatewayConfigurationDTO;

public interface ThyngClient {

	GatewayConfigurationDTO registerAndFetchDetails() throws Exception;

	void heartbeat() throws Exception;

	void sendTelemetry(TelemetryRequest telemetryRequest) throws Exception;

	void sendThingStatus(Long thingId, Boolean active) throws Exception;

	void sendSensorStatus(Long sensorId, Boolean active) throws Exception;

}
