package com.thyng.aspect.persistence;

import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.domain.AuditorAware;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import com.thyng.ThyngDeviceRegistryApplication;
import com.thyng.aspect.audit.AuditorAwareBean;

@Configuration
@EnableCaching
@EnableTransactionManagement
@EnableJpaAuditing(auditorAwareRef="auditorAwareBean")
@EnableJpaRepositories(basePackageClasses = ThyngDeviceRegistryApplication.class)
public class PersistenceConfiguration {

	@Bean("auditorAwareBean")
	public AuditorAware<Long> auditorAware(){
		return new AuditorAwareBean();
	}
	
}
