package com.thyng.gateway;

import java.util.concurrent.ScheduledExecutorService;

import com.google.gson.Gson;
import com.thyng.model.dto.GatewayDetailsDTO;

import lombok.Builder;
import lombok.Value;

@Value
@Builder
public class SimpleContext implements Context{

	private final Gson gson;
	private final EventBus eventBus;
	private final ScheduledExecutorService executor;
	private final PropertyProvider properties;
	private final GatewayDetailsDTO details;
	
}
