package com.thyng;

import java.nio.ByteBuffer;

import com.thyng.netty.Client;
import com.thyng.netty.OioClient;

public class TestClient {
	public static void main(String[] args) throws InterruptedException {
		final Client client = new OioClient(8080, "localhost");
		final ByteBuffer byteBuffer = ByteBuffer.allocate(16);
		byteBuffer.putLong(197);
		byteBuffer.putDouble(33);
		while(true) {
			System.out.println("Sending metrics");
			client.execute(byteBuffer.array());
			Thread.sleep(100);
		}
	}
}
