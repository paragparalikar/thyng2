package com.thyng.gateway.provider.client;

import com.thyng.gateway.provider.property.PropertyProvider;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class ThyngClientBuilder {

	private volatile ThyngClient client;
	private final PropertyProvider properties;
	
	public ThyngClient getClient() {
		build();
		return client;
	}
	
	private void build(){
		if(null == client){
			client = new CoapThyngClient(properties);
		}
	}

}
