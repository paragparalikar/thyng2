package com.thyng.gateway.provider.persistence;

import java.util.HashMap;
import java.util.Map;

import com.thyng.gateway.Constant;


public class FilePersistenceProvider implements PersistenceProvider {
	
	private final Map<Long, SensorMetricsStore> sensorMetricsStoreChache = new HashMap<>();
	private final FileConfigurationStore configurationStore = new FileConfigurationStore();
		
	@Override
	public SensorMetricsStore getSensorMetricsStore(final Long sensorId) {
		return sensorMetricsStoreChache.computeIfAbsent(sensorId, id -> {
			return FileSensorMetricsStore.builder()
					.sensorId(id)
					.baseStoragePath(System.getProperty(Constant.KEY_STORAGE, null))
					.build();
		});
	}

	@Override
	public ConfigurationStore getConfigurationStore() {
		return configurationStore;
	}
	
}
