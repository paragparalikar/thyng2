package com.thyng.gateway.service.server.coap;

import com.thyng.gateway.model.Context;
import com.thyng.gateway.service.Service;
import com.thyng.gateway.service.ServiceBuilder;

public class CoapServerServiceBuilder implements ServiceBuilder {

	@Override
	public Service newInstance(Context context) {
		return new CoapServerService(context);
	}

}
