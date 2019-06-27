package com.thyng.gateway.provider.persistence;

public interface PersistenceProvider{

	TelemetryStore getTelemetryStore(Long sensorId);

	ConfigurationStore getConfigurationStore();
	
}
