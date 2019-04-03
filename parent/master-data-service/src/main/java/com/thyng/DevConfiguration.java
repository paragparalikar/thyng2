package com.thyng;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

import com.thyng.service.SetupService;

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