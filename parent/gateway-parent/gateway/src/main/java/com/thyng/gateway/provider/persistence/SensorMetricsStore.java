package com.thyng.gateway.provider.persistence;

import java.util.Map;

public interface SensorMetricsStore {

	Map<Long, Double> read(Long transactionId);

	SensorMetricsStore rollback(Long transactionId);

	SensorMetricsStore commit(Long transactionId);

	SensorMetricsStore save(Long timestamp, Double value);

}