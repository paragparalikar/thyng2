package com.thyng.gateway.service.health;

import com.thyng.gateway.model.Context;
import com.thyng.gateway.service.Service;
import com.thyng.gateway.service.ServiceBuilder;

public class HeartbeatServiceBuilder implements ServiceBuilder {

	@Override
	public Service newInstance(Context context) {
		return new HeartbeatService(context);
	}

}
