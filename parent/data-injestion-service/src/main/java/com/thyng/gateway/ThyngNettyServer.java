package com.thyng.gateway;

import java.util.concurrent.Executor;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.thyng.AbstractThyngNettyServer;
import com.thyng.entity.Gateway;
import com.thyng.entity.Sensor;
import com.thyng.gateway.model.CommitRequest;
import com.thyng.gateway.model.RollbackRequest;
import com.thyng.mapper.GatewayMapper;
import com.thyng.model.HeartbeatRequest;
import com.thyng.model.HeartbeatResponse;
import com.thyng.model.RegistrationRequest;
import com.thyng.model.RegistrationResponse;
import com.thyng.model.SensorStatusRequest;
import com.thyng.model.SensorStatusResponse;
import com.thyng.model.Telemetry;
import com.thyng.model.TelemetryRequest;
import com.thyng.model.TelemetryResponse;
import com.thyng.model.ThingStatusRequest;
import com.thyng.model.ThingStatusResponse;
import com.thyng.model.dto.GatewayConfigurationDTO;
import com.thyng.netty.Client;
import com.thyng.service.GatewayService;
import com.thyng.service.SensorService;
import com.thyng.service.TelemetryService;

import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class ThyngNettyServer extends AbstractThyngNettyServer {

	private final Executor executor;
	private final GatewayMapper gatewayMapper;
	private final SensorService sensorService;
	private final GatewayService gatewayService;
	private final TelemetryService telemetryService;
	private final GatewayClientFactory gatewayClientFactory;
	
	@Autowired
	public ThyngNettyServer(
			@Value("${thyng.server.port:9090}") int port, 
			@NonNull final Executor executor,
			@NonNull final SensorService sensorService,
			@NonNull final GatewayService gatewayService, 
			@NonNull final GatewayMapper gatewayMapper, 
			@NonNull final TelemetryService telemetryService) {
		super(port);
		this.executor = executor;
		this.gatewayMapper = gatewayMapper;
		this.sensorService = sensorService;
		this.gatewayService = gatewayService;
		this.telemetryService = telemetryService;
		this.gatewayClientFactory = new GatewayClientFactory(gatewayService);
	}

	@Override
	@PostConstruct
	public ThyngNettyServer start() {
		super.start();
		return this;
	}
	
	@Override
	@PreDestroy
	public ThyngNettyServer stop() {
		super.stop();
		return this;
	}

	@Override
	protected TelemetryResponse handle(TelemetryRequest request) {
		executor.execute(() -> {
			try {
				final Telemetry telemetry = request.getTelemtry();
				telemetryService.save(telemetry);
				final Sensor sensor = sensorService.findById(telemetry.getSensorId());
				final Client client = gatewayClientFactory.get(sensor.getThing().getGateway().getId());
				client.execute(new CommitRequest(telemetry.getUuid(), telemetry.getSensorId()));
			}catch(Exception exception) {
				log.error("Failed to persist telemetry", exception);
				final Telemetry telemetry = request.getTelemtry();
				final Sensor sensor = sensorService.findById(telemetry.getSensorId());
				final Client client = gatewayClientFactory.get(sensor.getThing().getGateway().getId());
				client.execute(new RollbackRequest(telemetry.getUuid(), telemetry.getSensorId()));
			}
		});
		return new TelemetryResponse();
	}

	@Override
	protected RegistrationResponse handle(RegistrationRequest request) {
		final Gateway gateway = gatewayService.register(request.getGatewayId(), request.getHost(), request.getPort());
		final GatewayConfigurationDTO dto = gatewayMapper.toExtendedDTO(gateway);
		return new RegistrationResponse(dto);
	}

	@Override
	protected HeartbeatResponse handle(HeartbeatRequest request) {
		System.out.println("Heartbeat : " + request.getGatewayId());
		return new HeartbeatResponse();
	}

	@Override
	protected ThingStatusResponse handle(ThingStatusRequest request) {
		// TODO Auto-generated method stub
		return new ThingStatusResponse();
	}

	@Override
	protected SensorStatusResponse handle(SensorStatusRequest request) {
		// TODO Auto-generated method stub
		return new SensorStatusResponse();
	}


}
