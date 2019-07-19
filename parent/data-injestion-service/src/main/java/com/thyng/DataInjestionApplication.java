package com.thyng;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import com.thyng.mapper.SensorMapper;
import com.thyng.mapper.ThingMapper;
import com.thyng.metrics.injestor.MetricsInjestor;
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
			MetricsService metricsService) {
		return MetricsInjestor.builder()
				.thingMapper(thingMapper)
				.sensorMapper(sensorMapper)
				.sensorService(sensorService)
				.metricsService(metricsService)
				.build();
	}
	
}
