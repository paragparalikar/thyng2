package com.thyng;

import com.thyng.model.HeartbeatRequest;
import com.thyng.model.HeartbeatResponse;
import com.thyng.model.RegistrationRequest;
import com.thyng.model.RegistrationResponse;
import com.thyng.model.SensorStatusRequest;
import com.thyng.model.SensorStatusResponse;
import com.thyng.model.TelemetryRequest;
import com.thyng.model.TelemetryResponse;
import com.thyng.model.ThingStatusRequest;
import com.thyng.model.ThingStatusResponse;
import com.thyng.netty.NettyServer;

public abstract class AbstractThyngNettyServer extends NettyServer {

	public AbstractThyngNettyServer(int port) {
		super(port);
	}

	@Override
	public Object serve(Object request) {
		if(request instanceof TelemetryRequest) {
			return handle((TelemetryRequest) request);
		}else if(request instanceof RegistrationRequest) {
			return handle((RegistrationRequest) request);
		}else if(request instanceof HeartbeatRequest) {
			return handle((HeartbeatRequest) request);
		}else if(request instanceof ThingStatusRequest) {
			return handle((ThingStatusRequest) request);
		}else if(request instanceof SensorStatusRequest) {
			return handle((SensorStatusRequest) request);
		}
		throw new UnsupportedOperationException(request.getClass().getCanonicalName() + " is not supported");
	}

	protected abstract TelemetryResponse handle(TelemetryRequest request);
	
	protected abstract RegistrationResponse handle(RegistrationRequest request);
	
	protected abstract HeartbeatResponse handle(HeartbeatRequest request);
	
	protected abstract ThingStatusResponse handle(ThingStatusRequest request);
	
	protected abstract SensorStatusResponse handle(SensorStatusRequest request);
}
