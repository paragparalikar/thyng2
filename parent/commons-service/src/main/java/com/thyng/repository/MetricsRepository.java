package com.thyng.repository;

import java.util.Map;

import com.thyng.model.GatewayMetrics;

public interface MetricsRepository {
	
	void save(final GatewayMetrics gatewayMetrics);

	Map<Long, Double> read(Long sensorId);

	Map<Long, Double> read(Long sensorId, Long start);

	Map<Long, Double> read(Long sensorId, Long start, Long end);

}
