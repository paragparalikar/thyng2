package com.thyng.gateway.client;

import com.thyng.gateway.provider.property.PropertyProvider;

public interface ThyngClientBuilder {

	ThyngClient newInstance(PropertyProvider properties);
	
}
