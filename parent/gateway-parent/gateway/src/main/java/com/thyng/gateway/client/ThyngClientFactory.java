package com.thyng.gateway.client;

import com.thyng.gateway.EventBus;
import com.thyng.gateway.provider.property.PropertyProvider;
import com.thyng.gateway.provider.serialization.SerializationProvider;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class ThyngClientFactory {

	private final EventBus eventBus;
	private volatile ThyngClient client;
	private final PropertyProvider properties;
	private final SerializationProvider<String> serializationProvider;
	
	public ThyngClient getClient() {
		if(null == client){
			client = build();
		}
		return client;
	}
	
	private ThyngClient build(){
		return new EventPublisherThyngClient(eventBus, 
				new HttpThyngClient(properties, serializationProvider));
	}
}
