package com.thyng.gateway.provider.persistence;

import java.util.Map;

import com.thyng.model.Telemetry;

public interface TelemetryStore {

	int getCount();
	
	Long getSensorId();

	TelemetryStore save(Map<String, String> values);

	Telemetry read();

	TelemetryStore rollback(String uuid);

	TelemetryStore commit(String uuid);

	TelemetryStore save(Long timestamp, String value);

}