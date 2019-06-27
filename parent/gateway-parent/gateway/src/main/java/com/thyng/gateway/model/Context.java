package com.thyng.gateway.model;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ScheduledExecutorService;

import com.thyng.gateway.EventBus;
import com.thyng.gateway.client.ThyngClient;
import com.thyng.gateway.provider.persistence.PersistenceProvider;
import com.thyng.gateway.provider.property.PropertyProvider;
import com.thyng.model.dto.GatewayConfigurationDTO;
import com.thyng.model.dto.SensorDTO;
import com.thyng.model.dto.ThingDetailsDTO;

import lombok.Builder;
import lombok.Value;

@Value
public class Context {

	private final EventBus eventBus;
	private final ThyngClient client;
	private final ScheduledExecutorService executor;
	private final PropertyProvider properties;
	private final GatewayConfigurationDTO details;
	private final PersistenceProvider persistenceProvider;
	private final Map<Long, ThingDetailsDTO> sensorIdToThingDTO = new HashMap<>();
	private final Map<Long, SensorDTO> sensorIdToSensorDTO = new HashMap<>();

	@Builder
	public Context(	EventBus eventBus, 
					ThyngClient client, 
					ScheduledExecutorService executor,
					PropertyProvider properties, 
					GatewayConfigurationDTO details, 
					PersistenceProvider persistenceProvider) {
		super();
		this.eventBus = eventBus;
		this.client = client;
		this.executor = executor;
		this.properties = properties;
		this.details = details;
		this.persistenceProvider = persistenceProvider;
		init();
	}

	private void init() {
		for(ThingDetailsDTO thing : details.getThings()){
			for(SensorDTO sensor : thing.getSensors()){
				sensorIdToThingDTO.put(sensor.getId(), thing);
				sensorIdToSensorDTO.put(sensor.getId(), sensor);
			}
		}
	}
	
	public ThingDetailsDTO getThing(Long sensorId){
		return sensorIdToThingDTO.get(sensorId);
	}
	
	public SensorDTO getSensor(Long sensorId){
		return sensorIdToSensorDTO.get(sensorId);
	}
}
