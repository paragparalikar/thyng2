package com.thyng.gateway.client;

import com.thyng.gateway.EventBus;
import com.thyng.model.Telemetry;
import com.thyng.model.dto.GatewayConfigurationDTO;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class EventPublisherThyngClient implements ThyngClient {

	private final EventBus eventBus;
	private final ThyngClient delegate;

	@Override
	public GatewayConfigurationDTO registerAndFetchDetails() throws Exception {
		final GatewayConfigurationDTO configuration = delegate.registerAndFetchDetails();
		eventBus.publish(ThyngClient.ACTIVITY, System.currentTimeMillis());
		return configuration;
	}

	@Override
	public void heartbeat() throws Exception {
		delegate.heartbeat();
		eventBus.publish(ThyngClient.ACTIVITY, System.currentTimeMillis());
	}

	@Override
	public void send(Telemetry telemetry) throws Exception {
		delegate.send(telemetry);
		eventBus.publish(ThyngClient.ACTIVITY, System.currentTimeMillis());
	}

	@Override
	public void sendThingStatus(Long thingId, Boolean active) throws Exception {
		delegate.sendThingStatus(thingId, active);
		eventBus.publish(ThyngClient.ACTIVITY, System.currentTimeMillis());
	}

	@Override
	public void sendSensorStatus(Long sensorId, Boolean active) throws Exception {
		delegate.sendSensorStatus(sensorId, active);
		eventBus.publish(ThyngClient.ACTIVITY, System.currentTimeMillis());
	}

}
