package com.thyng;

import java.util.concurrent.Executor;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import com.thyng.mapper.MetricsSchemaMapper;
import com.thyng.mapper.SensorMapper;
import com.thyng.mapper.ThingMapper;
import com.thyng.metrics.MetricsInjestor;
import com.thyng.metrics.endpoint.BinaryEndpoint;
import com.thyng.service.MetricsSchemaService;
import com.thyng.service.MetricsService;
import com.thyng.service.SensorService;

@SpringBootApplication
public class DataInjestionApplication {

	public static void main(String[] args) {
		SpringApplication.run(DataInjestionApplication.class, args);
	}

	@Bean
	public ThreadPoolTaskExecutor executor() {
		return new ThreadPoolTaskExecutor();
	}
	
	@Bean
	public MetricsInjestor metricsInjestor(ThingMapper thingMapper,
			SensorMapper sensorMapper, SensorService sensorService,
			MetricsService metricsService,  MetricsSchemaService metricsSchemaService) {
		return MetricsInjestor.builder()
				.thingMapper(thingMapper)
				.sensorMapper(sensorMapper)
				.sensorService(sensorService)
				.metricsService(metricsService)
				.metricsSchemaService(metricsSchemaService)
				.build();
	}
	
	@Bean(initMethod = "start", destroyMethod = "stop")
	public BinaryEndpoint binaryEndpoint(
			@Value("${server.injestion.port.binary:9092}") Integer port,
			Executor executor,
			MetricsService metricsService,
			MetricsInjestor metricsInjestor,
			MetricsSchemaMapper metricsSchemaMapper,
			MetricsSchemaService metricsSchemaService) {
		return BinaryEndpoint.builder()
				.port(port)
				.executor(executor)
				.metricsConsumer(metricsInjestor::injest)
				.build();
	}
	
}
