package com.thyng;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Profile;

@SpringBootApplication
public class WebAPIApplication extends SpringBootServletInitializer {
	
	@Bean
	@Profile("dev")
	public CommandLineRunner run(MockDataSetupService setupService){
		return args -> {
			setupService.setup();
		};
	}

	public static void main(String[] args) {
		SpringApplication.run(WebAPIApplication.class, args);
	}

	@Override
	protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
		return application.sources(WebAPIApplication.class);
	}

}
