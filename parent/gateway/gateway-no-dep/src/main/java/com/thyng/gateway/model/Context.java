package com.thyng.gateway.model;

import java.util.concurrent.ScheduledExecutorService;

import com.thyng.gateway.provider.client.ThyngClient;
import com.thyng.gateway.provider.event.EventBus;
import com.thyng.gateway.provider.persistence.PersistenceProvider;
import com.thyng.gateway.provider.property.PropertyProvider;
import com.thyng.model.dto.GatewayConfigurationDTO;

import lombok.Builder;
import lombok.Value;

@Value
@Builder
public class Context {

	private final EventBus eventBus;
	private final ThyngClient client;
	private final ScheduledExecutorService executor;
	private final PropertyProvider properties;
	private final GatewayConfigurationDTO details;
	private final PersistenceProvider persistenceProvider;
	
}
