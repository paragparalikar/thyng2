package com.thyng.gateway;

import com.thyng.gateway.model.CommitRequest;
import com.thyng.gateway.model.CommitResponse;
import com.thyng.gateway.model.RollbackRequest;
import com.thyng.gateway.model.RollbackResponse;
import com.thyng.netty.NettyServer;

public abstract class AbstractGatewayNettyServer extends NettyServer {

	public AbstractGatewayNettyServer(int port) {
		super(port);
	}

	@Override
	public Object serve(Object request) {
		if(request instanceof CommitRequest){
			return handle((CommitRequest)request);
		}else if(request instanceof RollbackRequest) {
			return handle((RollbackRequest)request);
		}
		throw new UnsupportedOperationException(request.getClass().getCanonicalName() + " is not supported");
	}

	protected abstract CommitResponse handle(CommitRequest request);
	
	protected abstract RollbackResponse handle(RollbackRequest request);
	
}
