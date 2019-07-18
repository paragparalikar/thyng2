package com.thyng.service;

import org.springframework.stereotype.Service;

import com.thyng.model.GatewayMetrics;
import com.thyng.model.Metrics;
import com.thyng.repository.MetricsRepository;

import lombok.RequiredArgsConstructor;

@Service 
@RequiredArgsConstructor
public class MetricsService {

	private final MetricsRepository metricsRepository;
	
	public void save(Metrics metrics) {
		metricsRepository.save(metrics);
	}
	
	public void save(GatewayMetrics gatewayMetrics) {
		metricsRepository.save(gatewayMetrics);
	}

}
