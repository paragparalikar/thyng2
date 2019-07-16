package com.thyng.repository;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.TreeMap;
import java.util.stream.Collectors;

import com.thyng.model.GatewayMetrics;

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
	public void save(GatewayMetrics gatewayMetrics) {
		log.info("Persisting metrics with transactionId : "+gatewayMetrics.getTransactionId());
		gatewayMetrics.getSensorMetrics().forEach((sensorId, metrics) -> {
			read(sensorId).putAll(metrics);
		});
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
