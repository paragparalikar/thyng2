package com.thyng.gateway.persistence;

import java.util.Map;

import com.thyng.gateway.model.ThingMetrics;

public interface MetricsRepository {

	void save(ThingMetrics thingMetrics);
	
	Map<Long, Map<Long, Double>> read(Long transactionId);
	
	void rollback(Long transactionId);
	
	void rollbackAll();
	
	int getPendingTransactionCount();
	
	void commit(Long transactionId);
	
}
