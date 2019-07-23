package com.thyng.configuration.persistence;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import com.thyng.domain.metrics.InMemoryMetricsRepository;
import com.thyng.domain.metrics.MetricsRepository;

@Configuration
@EnableTransactionManagement
@EnableJpaRepositories("com.thyng")
public class PersistenceConfiguration {
	
	@Bean
	@Profile("dev")
	public MetricsRepository metricsRepository() {
		return new InMemoryMetricsRepository();
	}
	
}
