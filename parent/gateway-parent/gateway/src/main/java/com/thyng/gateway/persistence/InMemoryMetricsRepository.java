package com.thyng.gateway.persistence;

import java.util.HashMap;
import java.util.Map;

import com.thyng.gateway.model.ThingMetrics;

public class InMemoryMetricsRepository implements MetricsRepository {

	private final Map<Long, Map<Long, Double>> store = new HashMap<>();
	private final Map<Long, Map<Long, Map<Long, Double>>> transactions = new HashMap<>();
	
	@Override
	public synchronized void save(ThingMetrics thingMetrics) {
		thingMetrics.getValues().forEach((sensorId, value) -> {
			final Map<Long, Double> values = store.computeIfAbsent(sensorId, id -> new HashMap<>());
			values.put(thingMetrics.getTimestamp(), value);
		});
	}

	@Override
	public synchronized Map<Long, Map<Long, Double>> read(Long transactionId) {
		final Map<Long, Map<Long, Double>> transaction = new HashMap<>(store);
		transactions.put(transactionId, transaction);
		store.clear();
		return transaction;
	}

	@Override
	public synchronized void rollback(Long transactionId) {
		store.putAll(transactions.remove(transactionId));
	}

	@Override
	public synchronized void commit(Long transactionId) {
		transactions.remove(transactionId);
	}

	@Override
	public synchronized void rollbackAll() {
		transactions.forEach((transactionId, metrics) -> store.putAll(metrics));
		transactions.clear();
	}

	@Override
	public synchronized int getPendingTransactionCount() {
		return transactions.size();
	}

}
