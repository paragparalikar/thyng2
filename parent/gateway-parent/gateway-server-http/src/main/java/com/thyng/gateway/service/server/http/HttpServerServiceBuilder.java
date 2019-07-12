package com.thyng.gateway.service.server.http;

import com.thyng.gateway.model.Context;
import com.thyng.gateway.service.Service;
import com.thyng.gateway.service.ServiceBuilder;

public class HttpServerServiceBuilder implements ServiceBuilder {

	@Override
	public Service newInstance(Context context) {
		return new HttpServerService(context);
	}

}
