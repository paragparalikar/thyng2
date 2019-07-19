package com.thyng.service;

import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import com.thyng.model.GatewayMetrics;
import com.thyng.model.Metrics;
import com.thyng.repository.MetricsRepository;
import com.thyng.service.kafka.KafkaMetricsProducer;
import com.thyng.service.kafka.KafkaTopicProvider;

import lombok.RequiredArgsConstructor;

@Service 
@Lazy(true)
@RequiredArgsConstructor
public class MetricsService {

	private final KafkaTopicProvider kafkaTopicProvider;
	private final MetricsRepository metricsRepository;
	private final KafkaMetricsProducer kafkaMetricsProducer;
	
	public void save(Metrics metrics) {
		final String topic = kafkaTopicProvider.getMetricsTopic(metrics.getSensorId());
		kafkaMetricsProducer.send(topic, metrics);
		metricsRepository.save(metrics);
	}
	
	public void save(GatewayMetrics gatewayMetrics) {
		metricsRepository.save(gatewayMetrics);
	}

}
