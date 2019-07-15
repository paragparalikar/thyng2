package com.thyng;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

@SpringBootApplication
public class DataInjestionApplication {

	public static void main(String[] args) {
		SpringApplication.run(DataInjestionApplication.class, args);
	}

	@Bean
	public ThreadPoolTaskExecutor executor() {
		return new ThreadPoolTaskExecutor();
	}
	
}
