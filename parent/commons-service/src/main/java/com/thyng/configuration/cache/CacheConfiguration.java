package com.thyng.configuration.cache;

import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.hazelcast.config.Config;

import lombok.RequiredArgsConstructor;

//@EnableCaching
@Configuration
@RequiredArgsConstructor
public class CacheConfiguration {

		
	//@Bean
	public Config config() {
		final Config config = new Config();
		
		return config;
	}
	

}
