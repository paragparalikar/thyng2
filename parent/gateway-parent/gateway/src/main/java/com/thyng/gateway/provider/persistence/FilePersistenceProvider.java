package com.thyng.gateway.provider.persistence;

import java.util.HashMap;
import java.util.Map;

import com.thyng.gateway.model.Constant;
import com.thyng.gateway.provider.property.PropertyProvider;

import lombok.NonNull;


public class FilePersistenceProvider implements PersistenceProvider {
	
	private final PropertyProvider properties;
	private final Map<Long, TelemetryStore> telemetryStoreChache = new HashMap<>();
	private final FileConfigurationStore configurationStore;
	
	public FilePersistenceProvider(@NonNull final PropertyProvider properties) {
		this.properties = properties;
		configurationStore = new FileConfigurationStore(properties);
	}
	
	@Override
	public TelemetryStore getTelemetryStore(final Long sensorId) {
		return telemetryStoreChache.computeIfAbsent(sensorId, id -> {
			return FileTelemetryStore.builder()
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
