package com.thyng.gateway;


import com.thyng.gateway.service.BootstrapService;

import lombok.extern.slf4j.Slf4j;

/**
 * 		Heart beats 
 * 		Data normalization 
 * 		Data persistence 
 * 		Data Aggregation 
 * 		Data upload
 * Thing status change notifications
 * 
 * Queries
 * Ping
 * Logs
 * System Properties
 * Environmental Variables
 * Thyng properties
 * MX bean values
 * 
 * Commands
 * Set log levels
 * Set System properties
 * Set Thyng property
 * Soft Restart
 * Hard Restart
 */
@Slf4j
public class Main {
	
	public static void main(String[] args) throws Exception {
		
		try{
			new BootstrapService().start();
		}catch(Exception e){
			log.error("Failed to bootstrap gateway", e);
		}
		
	}

}
