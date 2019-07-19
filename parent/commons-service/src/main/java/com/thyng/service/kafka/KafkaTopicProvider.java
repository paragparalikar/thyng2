package com.thyng.service.kafka;

import java.util.HashMap;
import java.util.Map;

import com.thyng.entity.Sensor;
import com.thyng.service.SensorService;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class KafkaTopicProvider {

	private final SensorService sensorService;
	private final Map<Long, String> sensorIdToTopicMap = new HashMap<>();
	
	public String getMetricsTopic(Long sensorId) {
		return sensorIdToTopicMap.computeIfAbsent(sensorId, id -> {
			final Sensor sensor = sensorService.findById(sensorId);
			return sensor.getThing().getTenant().getId()+"-metrics";
		});
	}

}
