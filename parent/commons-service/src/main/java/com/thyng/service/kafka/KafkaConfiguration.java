package com.thyng.service.kafka;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;

import com.thyng.service.SensorService;

@Configuration
public class KafkaConfiguration {

	@Lazy(true)
	@Bean(initMethod = "start", destroyMethod = "stop")
	public KafkaMetricsProducer kafkaMetricsProducer(
			@Value("${kafka.client-id}") String clientId,
			@Value("${kafka.bootstrap-servers}") String bootstrapServers) {
		return KafkaMetricsProducer.builder()
				.clientId(clientId)
				.bootstrapServers(bootstrapServers)
				.build();
	}
	
	@Bean
	public KafkaTopicProvider kafkaTopicProvider(SensorService sensorService){
		return new KafkaTopicProvider(sensorService);
	}

}
