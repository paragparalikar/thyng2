package com.thyng.aspect.persistence;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

@Profile("dev")
@Configuration
public class DevConfiguration{
	
	@Autowired
	private SetupService setupService;
	
	@Bean
	public CommandLineRunner run(){
		return args -> {
			setupService.setup();
		};
	}

	
		
}
