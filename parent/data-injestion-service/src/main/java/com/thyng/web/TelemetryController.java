package com.thyng.web;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.validation.constraints.NotNull;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.thyng.entity.Sensor;
import com.thyng.gateway.client.GatewayClient;
import com.thyng.gateway.client.GatewayClientFactory;
import com.thyng.model.Telemetry;
import com.thyng.service.SensorService;
import com.thyng.service.TelemetryService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping("/api/v1/telemetries")
@RequiredArgsConstructor
public class TelemetryController {

	private final SensorService sensorService;
	private final TelemetryService telemetryService;
	private final GatewayClientFactory gatewayClientFactory;
	
	@PostMapping
	public void save(
			@RequestParam @NotNull final Long sensorId, 
			@RequestParam @NotNull final String uuid, 
			final HttpServletRequest request) throws IOException {
		final Telemetry telemetry = new Telemetry(uuid, sensorId, request.getInputStream());
		if(log.isTraceEnabled()) {
			log.trace("Received telemetry from sensor id "+telemetry.getSensorId()
			+ ", uuid "+telemetry.getUuid());
		}
		
		final Sensor sensor = sensorService.findById(telemetry.getSensorId());
		final GatewayClient gatewayClient = gatewayClientFactory
				.get(sensor.getThing().getGateway().getId());
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
