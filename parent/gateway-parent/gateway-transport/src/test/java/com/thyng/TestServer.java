package com.thyng;

import com.thyng.gateway.AbstractGatewayNettyServer;
import com.thyng.gateway.model.CommitRequest;
import com.thyng.gateway.model.CommitResponse;
import com.thyng.gateway.model.RollbackRequest;
import com.thyng.gateway.model.RollbackResponse;

public class TestServer {

	public static void main(String[] args) throws InterruptedException {
		final AbstractGatewayNettyServer server = new AbstractGatewayNettyServer(8080) {

			@Override
			protected CommitResponse handle(CommitRequest request) {
				// TODO Auto-generated method stub
				return null;
			}

			@Override
			protected RollbackResponse handle(RollbackRequest request) {
				// TODO Auto-generated method stub
				return null;
			}
			
		};
		
		server.start();
		server.channelFuture().sync().channel().closeFuture().sync();
		server.stop();
	}

}
