package com.thyng.gateway.persistence;

import java.util.Map;

import com.thyng.gateway.model.ThingMetrics;

public class SmartMetricsRepository implements MetricsRepository {
	private static final int MAX_TRANSACTION_COUNT = 10;
	private static final int MIN_TRANSACTION_COUNT = 0;
	
	private final FileMetricsRepository fileMetricsRepository = new FileMetricsRepository();
	private final InMemoryMetricsRepository inMemoryMetricsRepository = new InMemoryMetricsRepository();
	private MetricsRepository delegate = inMemoryMetricsRepository;

	@Override
	public void save(ThingMetrics thingMetrics) {
		// TODO Auto-generated method stub

	}

	@Override
	public Map<Long, Map<Long, Double>> read(Long transactionId) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void rollback(Long transactionId) {
		// TODO Auto-generated method stub

	}

	@Override
	public void commit(Long transactionId) {
		// TODO Auto-generated method stub

	}

	@Override
	public void rollbackAll() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public int getPendingTransactionCount() {
		// TODO Auto-generated method stub
		return 0;
	}

}
