package com.thyng.metrics.endpoint;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.thyng.configuration.kafka.KafkaMetricsProducer;
import com.thyng.configuration.kafka.KafkaTopicProvider;
import com.thyng.domain.metrics.Metrics;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
public class HttpMetricsEndpoint {

	private final KafkaTopicProvider kafkaTopicProvider;
	private final KafkaMetricsProducer kafkaMetricsProducer;
	
	@PostMapping("/metrics")
	public void create(@RequestBody Metrics metrics) {
		final String topic = kafkaTopicProvider.getMetricsTopic(metrics.getSensorId());
		kafkaMetricsProducer.send(topic, metrics);
	}

}
