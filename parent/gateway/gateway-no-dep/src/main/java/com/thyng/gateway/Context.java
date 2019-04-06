package com.thyng.gateway;

import java.util.concurrent.ScheduledExecutorService;

import com.google.gson.Gson;
import com.thyng.model.dto.GatewayDetailsDTO;

public interface Context {

	EventBus getEventBus();
	
	ScheduledExecutorService getExecutor();
	
	PropertyProvider getProperties();
	
	Gson getGson();
	
	GatewayDetailsDTO getDetails();
	
}
