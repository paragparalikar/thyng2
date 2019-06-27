package com.thyng.model;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

import com.thyng.util.StringUtil;

import lombok.Builder;
import lombok.NonNull;
import lombok.SneakyThrows;
import lombok.Value;

@Value
public class Telemetry {

	@NonNull private final String uuid; 
	@NonNull private final Long sensorId;
	@NonNull private final Map<String, String> values;
	
	@Builder
	public Telemetry(final String uuid, final Long sensorId, final String payload) {
		super();
		this.uuid = uuid;
		this.sensorId = sensorId;
		this.values = readValues(payload);
	}
	
	@Builder
	public Telemetry(final String uuid, final Long sensorId, final Map<String, String> values) {
		super();
		this.uuid = uuid;
		this.sensorId = sensorId;
		this.values = values;
	}
	
	private Map<String, String> readValues(final String payload){
		final Map<String, String> values = new HashMap<>();
		if(StringUtil.hasText(payload)) {
			Arrays.asList(payload.split("\n")).forEach(line -> {
				final String[] tokens = line.split(",");
				values.put(tokens[0], tokens[1]);
			});
		}
		return values;
	}
	
	@SneakyThrows
	public byte[] toByteArray() {
		final String payload = values.entrySet().stream()
				.map(e -> e.getKey()+","+e.getValue()).collect(Collectors.joining("\n"));
		return payload.getBytes("UTF-8");
	}




	
}
