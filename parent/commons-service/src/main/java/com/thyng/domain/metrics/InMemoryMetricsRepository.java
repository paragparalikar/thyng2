package com.thyng.domain.metrics;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.TreeMap;
import java.util.stream.Collectors;

import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class InMemoryMetricsRepository implements MetricsRepository {
	
	private final Map<Long, Map<Long, Double>> cache = new HashMap<>();
	
	@Override
	public Map<Long, Double> read(@NonNull final Long sensorId){
		return cache.computeIfAbsent(sensorId, id -> new TreeMap<>());
	}
	
	@Override
	public void save(Metrics metrics) {
		log.info("Persisting metrics for sensorId "+metrics.getSensorId());
		read(metrics.getSensorId()).put(metrics.getTimestamp(), metrics.getValue());
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
