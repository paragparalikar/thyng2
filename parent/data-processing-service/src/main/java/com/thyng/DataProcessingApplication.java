package com.thyng;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import com.hazelcast.jet.Jet;
import com.hazelcast.jet.JetInstance;

@SpringBootApplication
public class DataProcessingApplication implements ServletContextListener{

	public static void main(String[] args) {
		SpringApplication.run(DataProcessingApplication.class, args);
	}

	@Bean
	public JetInstance jetInstance() {
		return Jet.newJetInstance();
	}

	@Override
	public void contextInitialized(ServletContextEvent sce) {
		
	}

	@Override
	public void contextDestroyed(ServletContextEvent sce) {
		Jet.shutdownAll();
	}

}
