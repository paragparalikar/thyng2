package com.thyng.gateway;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.thyng.gateway.service.BootstrapService;

import lombok.extern.slf4j.Slf4j;

/**
 * Heart beats 
 * Data transformation 
 * Data persistence 
 * Data Aggregation 
 * Data upload
 * Thing status change notifications
 * 
 * Configuration service info/commands 
 * System service(+Watchdog) info/commands
 * Log upload service info
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
