package com.thyng.repository;

import java.util.Map;

import com.thyng.model.GatewayMetrics;
import com.thyng.model.Metrics;

public interface MetricsRepository {
	
	void save(Metrics metrics);
	
	void save(GatewayMetrics gatewayMetrics);

	Map<Long, Double> read(Long sensorId);

	Map<Long, Double> read(Long sensorId, Long start);

	Map<Long, Double> read(Long sensorId, Long start, Long end);

}
