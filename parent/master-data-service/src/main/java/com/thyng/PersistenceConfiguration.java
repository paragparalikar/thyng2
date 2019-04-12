package com.thyng;

import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.domain.AuditorAware;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import com.thyng.service.AuditorAwareBean;

@Configuration
@EnableCaching
@EnableJpaRepositories
@EnableTransactionManagement
@EnableJpaAuditing(auditorAwareRef="auditorAwareBean")
public class PersistenceConfiguration {

	@Bean("auditorAwareBean")
	public AuditorAware<Long> auditorAware(){
		return new AuditorAwareBean();
	}
	
}
