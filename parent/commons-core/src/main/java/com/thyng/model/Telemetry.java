package com.thyng.model;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

import lombok.Builder;
import lombok.NonNull;
import lombok.SneakyThrows;
import lombok.Value;

@Value
public class Telemetry {

	@NonNull private final String uuid; 
	@NonNull private final Long sensorId;
	@NonNull private final Map<Long, Double> values;
	
	@Builder
	@SneakyThrows
	public Telemetry(final String uuid, final Long sensorId, final InputStream inputStream) {
		super();
		this.uuid = uuid;
		this.sensorId = sensorId;
		this.values = readValues(inputStream);
	}
	
	@Builder
	public Telemetry(final String uuid, final Long sensorId, final Map<Long, Double> values) {
		super();
		this.uuid = uuid;
		this.sensorId = sensorId;
		this.values = values;
	}
	
	private Map<Long, Double> readValues(final InputStream inputStream) throws IOException{
		String line = null;
		final Map<Long, Double> values = new HashMap<>();
		try(final BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream))){
			while(null != (line = bufferedReader.readLine())) {
				final String[] tokens = line.split(",");
				values.put(Long.parseLong(tokens[0]), Double.parseDouble(tokens[1]));
			}
		}
		return values;
	}
	
	@Override
	public String toString() {
		return values.entrySet().stream()
				.map(e -> e.getKey()+","+e.getValue()).collect(Collectors.joining("\n"));
	}
}
