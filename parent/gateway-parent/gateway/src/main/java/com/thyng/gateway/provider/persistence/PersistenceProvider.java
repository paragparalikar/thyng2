package com.thyng.gateway.provider.persistence;

public interface PersistenceProvider{

	SensorMetricsStore getSensorMetricsStore(Long sensorId);

	ConfigurationStore getConfigurationStore();
	
}
