package com.thyng.gateway;

import java.util.Collections;
import java.util.Map;
import java.util.stream.Collectors;

import kong.unirest.HttpResponse;
import kong.unirest.Unirest;

public class Client {

	public static void main(String[] args) throws Exception {
		while(true){
			sendHttp(Collections.singletonMap(197l,1d));
			Thread.sleep(1000);
		}
	}
	
	private static void sendHttp(Map<Long, Double> values) {
		final String payload = values.entrySet().stream()
		.map(e -> e.getKey()+","+e.getValue()).collect(Collectors.joining("\n"));
		System.out.print(payload);
		final HttpResponse response = Unirest.post("http://localhost:8080/telemetries")
			.body(payload)
			.asEmpty();
		
		if(response.isSuccess()) {
			System.out.println("\tSuccess");
		}else {
			System.out.println("\tFailure, HTTP code: "+response.getStatus()+", text: "+response.getStatusText());
		}
	}

}
