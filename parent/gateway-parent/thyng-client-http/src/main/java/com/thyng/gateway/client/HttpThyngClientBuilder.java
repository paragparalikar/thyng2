package com.thyng.gateway.client;

import com.thyng.gateway.provider.property.PropertyProvider;

public class HttpThyngClientBuilder implements ThyngClientBuilder {

	@Override
	public ThyngClient newInstance(PropertyProvider properties) {
		return new HttpThyngClient(properties);
	}

}
