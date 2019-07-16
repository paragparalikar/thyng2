package com.thyng.repository;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import com.thyng.RootMarker;

@Configuration
@EnableTransactionManagement
@EnableJpaRepositories(basePackageClasses = RootMarker.class)
public class PersistenceConfiguration {
	
	@Bean
	@Profile("dev")
	public MetricsRepository metricsRepository() {
		return new InMemoryMetricsRepository();
	}
	
}
