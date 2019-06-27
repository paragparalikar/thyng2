package com.thyng.gateway.client;

import com.thyng.model.Telemetry;
import com.thyng.model.dto.GatewayConfigurationDTO;

public interface ThyngClient {
	String ACTIVITY = "thyng-client-activity";

	GatewayConfigurationDTO registerAndFetchDetails() throws Exception;

	void heartbeat() throws Exception;

	void send(Telemetry telemetryRequest) throws Exception;

	void sendThingStatus(Long thingId, Boolean active) throws Exception;

	void sendSensorStatus(Long sensorId, Boolean active) throws Exception;

}
