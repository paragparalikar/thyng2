package com.thyng.metrics.endpoint;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.thyng.model.Metrics;

public class HttpMetricsEndpointTest {

	public static void main(String[] args) throws MalformedURLException, IOException {
		final ObjectMapper objectMapper = new ObjectMapper();
		final Map<String, String> meta = new HashMap<>();
		meta.put("a", "b");
		meta.put("c", "d");
		double value = 1;
		final Metrics metrics = Metrics.builder()
				.meta(meta) // try with null as well
				.sensorId(197l)
				.timestamp(System.currentTimeMillis())
				.value(value)
				.build();
		while(true) {
			final HttpURLConnection connection = (HttpURLConnection) new URL("http://localhost:8080/metrics").openConnection();
			final String encoded = Base64.getEncoder().encodeToString(("0.0@gmail.com:thyng").getBytes(StandardCharsets.UTF_8));
			connection.setRequestMethod("POST");
			connection.setRequestProperty("Authorization", "Basic "+encoded);
			connection.setRequestProperty("Content-Type", "application/json;charset=UTF-8");
			final String body = objectMapper.writeValueAsString(metrics);
			connection.setDoOutput(true);
			connection.setDoInput(true);
			objectMapper.writeValue(connection.getOutputStream(), metrics);
			if(200 == connection.getResponseCode()) {
				System.out.println(body);
				value++;
			}else {
				System.out.println(connection.getResponseCode()+", "+connection.getResponseMessage());
			}
		}
	}

}
