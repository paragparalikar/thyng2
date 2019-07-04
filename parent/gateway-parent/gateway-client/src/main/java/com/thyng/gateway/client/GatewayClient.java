package com.thyng.gateway.client;

public interface GatewayClient {

	void commit(String uuid);

	void rollback(String uuid);

}