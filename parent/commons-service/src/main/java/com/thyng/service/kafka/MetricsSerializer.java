package com.thyng.service.kafka;

import org.apache.kafka.common.serialization.Serializer;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.thyng.model.Metrics;

import lombok.SneakyThrows;

public class MetricsSerializer implements Serializer<Metrics> {

	private final ObjectMapper objectMapper = new ObjectMapper();
	
	@Override
	@SneakyThrows
	public byte[] serialize(String topic, Metrics metrics) {
		return objectMapper.writeValueAsBytes(metrics);
	}

}
