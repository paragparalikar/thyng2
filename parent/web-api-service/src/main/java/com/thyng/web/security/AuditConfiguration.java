package com.thyng.web.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.domain.AuditorAware;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@Configuration
@EnableJpaAuditing(auditorAwareRef="auditorAwareBean")
public class AuditConfiguration {

	@Bean("auditorAwareBean")
	public AuditorAware<Long> auditorAware(){
		return new AuditorAwareBean();
	}

}
