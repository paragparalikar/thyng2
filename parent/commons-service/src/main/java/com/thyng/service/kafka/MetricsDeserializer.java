package com.thyng.service.kafka;

import org.apache.kafka.common.serialization.Deserializer;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.thyng.model.Metrics;

import lombok.SneakyThrows;

public class MetricsDeserializer implements Deserializer<Metrics> {

	private final ObjectMapper objectMapper = new ObjectMapper();
	
	@Override
	@SneakyThrows
	public Metrics deserialize(String topic, byte[] data) {
		return objectMapper.readValue(data, Metrics.class);
	}

}
