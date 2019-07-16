package com.thyng.gateway;

import java.util.concurrent.Executor;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.thyng.AbstractThyngNettyServer;
import com.thyng.entity.Gateway;
import com.thyng.gateway.model.CommitRequest;
import com.thyng.gateway.model.RollbackRequest;
import com.thyng.mapper.GatewayMapper;
import com.thyng.model.HeartbeatRequest;
import com.thyng.model.HeartbeatResponse;
import com.thyng.model.GatewayMetrics;
import com.thyng.model.GatewayMetricsRequest;
import com.thyng.model.GatewayMetricsResponse;
import com.thyng.model.RegistrationRequest;
import com.thyng.model.RegistrationResponse;
import com.thyng.model.SensorStatusRequest;
import com.thyng.model.SensorStatusResponse;
import com.thyng.model.ThingStatusRequest;
import com.thyng.model.ThingStatusResponse;
import com.thyng.model.dto.GatewayConfigurationDTO;
import com.thyng.netty.Client;
import com.thyng.service.GatewayService;
import com.thyng.service.MetricsService;

import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class ThyngNettyServer extends AbstractThyngNettyServer {

	private final Executor executor;
	private final GatewayMapper gatewayMapper;
	private final GatewayService gatewayService;
	private final MetricsService metricsService;
	private final GatewayClientFactory gatewayClientFactory;
	
	@Autowired
	public ThyngNettyServer(
			@Value("${thyng.server.port:9090}") int port, 
			@NonNull final Executor executor,
			@NonNull final GatewayService gatewayService, 
			@NonNull final GatewayMapper gatewayMapper, 
			@NonNull final MetricsService metricsService) {
		super(port);
		this.executor = executor;
		this.gatewayMapper = gatewayMapper;
		this.gatewayService = gatewayService;
		this.metricsService = metricsService;
		this.gatewayClientFactory = new GatewayClientFactory(gatewayService);
	}

	@Override
	@PostConstruct
	public void start() {
		super.start();
	}
	
	@Override
	@PreDestroy
	public void stop() {
		super.stop();
	}

	@Override
	protected GatewayMetricsResponse handle(GatewayMetricsRequest request) {
		executor.execute(() -> {
			try {
				final GatewayMetrics gatewayMetrics = request.getGatewayMetrics();
				metricsService.save(gatewayMetrics);
				final Client client = gatewayClientFactory.get(gatewayMetrics.getGatewayId());
				client.execute(new CommitRequest(gatewayMetrics.getTransactionId()));
			}catch(Exception exception) {
				log.error("Failed to persist gatewayMetrics", exception);
				final GatewayMetrics gatewayMetrics = request.getGatewayMetrics();
				final Client client = gatewayClientFactory.get(gatewayMetrics.getGatewayId());
				client.execute(new RollbackRequest(gatewayMetrics.getTransactionId()));
			}
		});
		return new GatewayMetricsResponse();
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
