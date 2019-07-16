package com.thyng.gateway.provider.persistence;

import java.util.HashMap;
import java.util.Map;

import com.thyng.gateway.Constant;
import com.thyng.gateway.provider.property.PropertyProvider;

import lombok.NonNull;


public class FilePersistenceProvider implements PersistenceProvider {
	
	private final PropertyProvider properties;
	private final Map<Long, SensorMetricsStore> sensorMetricsStoreChache = new HashMap<>();
	private final FileConfigurationStore configurationStore;
	
	public FilePersistenceProvider(@NonNull final PropertyProvider properties) {
		this.properties = properties;
		configurationStore = new FileConfigurationStore(properties);
	}
	
	@Override
	public SensorMetricsStore getSensorMetricsStore(final Long sensorId) {
		return sensorMetricsStoreChache.computeIfAbsent(sensorId, id -> {
			return FileSensorMetricsStore.builder()
					.sensorId(id)
					.baseStoragePath(properties.get(Constant.KEY_STORAGE, null))
					.build();
		});
	}

	@Override
	public ConfigurationStore getConfigurationStore() {
		return configurationStore;
	}
	
}
