package com.thyng.gateway.service;

import com.thyng.gateway.model.Context;

public interface ServiceBuilder {

	Service newInstance(Context context);
	
}
