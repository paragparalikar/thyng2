package com.thyng.repository;

import java.nio.ByteBuffer;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.TreeMap;
import java.util.stream.Collectors;

import com.thyng.model.Telemetry;

import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class InMemoryTelemetryRepository implements TelemetryRepository {
	
	private final Map<Long, Map<Long, Double>> cache = new HashMap<>();
	
	@Override
	public Map<Long, Double> read(@NonNull final Long sensorId){
		return cache.computeIfAbsent(sensorId, id -> new TreeMap<>());
	}
	
	@Override
	public void save(Telemetry telemetry) {
		log.info("Persisting telemetry with transactionId : "+telemetry.getTransactionId());
		for(int index = 0; index < telemetry.getSensorIds().length; index++) {
			final Long sensorId = telemetry.getSensorIds()[index];
			final Map<Long, Double> values = read(sensorId);
			final ByteBuffer byteBuffer = ByteBuffer.wrap(telemetry.getData()[index]);
			while(byteBuffer.hasRemaining()) {
				values.put(byteBuffer.getLong(), byteBuffer.getDouble());
			}
		}
	}
	
	@Override
	public Map<Long, Double> read(@NonNull final Long sensorId, @NonNull final Long start){
		return read(sensorId).entrySet().stream()
			.filter(entry -> entry.getKey() >= start)
			.collect(Collectors.toMap(Entry::getKey, Entry::getValue));
	}

	@Override
	public Map<Long, Double> read(@NonNull final Long sensorId, @NonNull final Long start, @NonNull final Long end){
		return read(sensorId).entrySet().stream()
			.filter(entry -> entry.getKey() >= start && entry.getKey() <= end)
			.collect(Collectors.toMap(Entry::getKey, Entry::getValue));
	}

}
