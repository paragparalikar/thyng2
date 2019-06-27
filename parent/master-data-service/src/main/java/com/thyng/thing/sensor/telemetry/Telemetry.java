package com.thyng.thing.sensor.telemetry;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import lombok.AccessLevel;
import lombok.Data;
import lombok.Getter;

@Data
public class Telemetry{

	private final Long thingId;
	private final Long sensorId; 
	
	@Getter(value = AccessLevel.NONE)
	private final HashMap<Long, Object> values = new HashMap<>();
	private final Map<Long, Object> unmodifiableValues = Collections.unmodifiableMap(values);  
	
	public Map<Long, Object> getValues() {
		return unmodifiableValues;
	}
	
}
