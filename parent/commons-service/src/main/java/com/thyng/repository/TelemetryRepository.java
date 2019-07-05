package com.thyng.repository;

import java.util.Map;

import com.thyng.model.Telemetry;

public interface TelemetryRepository {
	
	void save(final Telemetry telemetry);

	Map<Long, Double> read(Long sensorId);

	Map<Long, Double> read(Long sensorId, Long start);

	Map<Long, Double> read(Long sensorId, Long start, Long end);

}
