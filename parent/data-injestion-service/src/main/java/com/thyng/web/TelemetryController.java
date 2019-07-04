package com.thyng.web;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.thyng.gateway.client.GatewayClient;
import com.thyng.gateway.client.GatewayClientFactory;
import com.thyng.model.Telemetry;
import com.thyng.service.TelemetryService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping("\telemetries")
@RequiredArgsConstructor
public class TelemetryController {

	private final TelemetryService telemetryService;
	private final GatewayClientFactory gatewayClientFactory;
	
	@PostMapping
	public void save(@RequestBody final Telemetry telemetry) {
		if(log.isTraceEnabled()) {
			log.trace("Received telemetry from sensor id "+telemetry.getSensorId()
			+ ", uuid "+telemetry.getUuid());
		}
		
		final GatewayClient gatewayClient = gatewayClientFactory
				.get(telemetry.getSensorId());
		try {
			if(log.isDebugEnabled()) {
				log.debug("Attempting to persiste telemetry sensorId: "+telemetry.getSensorId()
				+ ", uuid: "+telemetry.getUuid());
			}
			telemetryService.save(telemetry);
			if(log.isDebugEnabled()) {
				log.debug("Successfully persisted telemetry sensorId: "+telemetry.getSensorId()
				+ ", uuid: "+telemetry.getUuid()+" now attempting to commit");
			}
			gatewayClient.commit(telemetry.getUuid());
			if(log.isDebugEnabled()) {
				log.debug("Successfully commited telemetry sensorId: "+telemetry.getSensorId()
				+ ", uuid: "+telemetry.getUuid());
			}
		}catch(Throwable throwable) {
			log.error("Failed to persist telemetry sensorId: " + telemetry.getSensorId() + 
					", uuid: " + telemetry.getUuid()+" now attempting to rollback", throwable);
			gatewayClient.rollback(telemetry.getUuid());
			log.error("Rolled back telemetry sensorId: "+telemetry.getSensorId()
					+", uuid: " + telemetry.getUuid());
		}
	}

}
