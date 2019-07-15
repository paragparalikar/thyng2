package com.thyng;

import com.thyng.model.HeartbeatRequest;
import com.thyng.model.HeartbeatResponse;
import com.thyng.netty.Client;
import com.thyng.netty.OioClient;

public class TestClient {
	public static void main(String[] args) {
		final Client client = new OioClient(9090, "localhost");
		final HeartbeatResponse response = client.execute(new HeartbeatRequest(164l));
	}
}
