package com.thyng.gateway.provider.persistence;

public interface TelemetryStore {

	byte[] read(Long transactionId);

	TelemetryStore rollback(Long transactionId);

	TelemetryStore commit(Long transactionId);

	TelemetryStore save(Long timestamp, Double value);

}