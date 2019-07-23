package com.thyng.domain.metrics;

import java.util.Map;

public interface MetricsRepository {
	
	void save(Metrics metrics);
	
	Map<Long, Double> read(Long sensorId);

	Map<Long, Double> read(Long sensorId, Long start);

	Map<Long, Double> read(Long sensorId, Long start, Long end);

}
