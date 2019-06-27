package com.thyng.gateway.client;

import com.thyng.gateway.EventBus;
import com.thyng.gateway.provider.property.PropertyProvider;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class ThyngClientBuilder {

	private final EventBus eventBus;
	private volatile ThyngClient client;
	private final PropertyProvider properties;
	
	public ThyngClient getClient() {
		build();
		return client;
	}
	
	private void build(){
		if(null == client){
			client = new EventPublisherThyngClient(eventBus, 
					new CoapThyngClient(properties));
		}
	}

}
