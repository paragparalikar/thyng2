package com.thyng;

import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

import com.thyng.service.MockDataSetupService;

@Profile("dev")
@Configuration
public class DevConfiguration {

	@Bean
	public CommandLineRunner runner(final MockDataSetupService mockDataSetupService) {
		return args -> mockDataSetupService.setup();
	}
	
}
