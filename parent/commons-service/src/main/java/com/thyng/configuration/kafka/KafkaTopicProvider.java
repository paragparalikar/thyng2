package com.thyng.configuration.kafka;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;

import com.thyng.domain.sensor.SensorService;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class KafkaTopicProvider {

	private final SensorService sensorService;
	private final Pattern metricsPattern = Pattern.compile("\\d+-metrics");
	private final Map<Long, Long> sensorIdToTenantIdMap = new HashMap<>();
	
	private Long getTenantIdBySensorId(Long sensorId) {
		return sensorIdToTenantIdMap.computeIfAbsent(sensorId, id -> {
			return sensorService.findById(sensorId).getThing().getTenant().getId();
		});
	}
	
	public String getMetricsTopic(Long sensorId) {
		return getTenantIdBySensorId(sensorId) + "-metrics";
	}
	
	public Pattern getAllTenantsMetricsPattern() {
		return metricsPattern;
	}
	
	public String getStatusTopic(Long sensorId) {
		return getTenantIdBySensorId(sensorId) + "-statuses";
	}

}
