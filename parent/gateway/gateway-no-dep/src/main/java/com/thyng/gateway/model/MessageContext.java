package com.thyng.gateway.model;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import com.thyng.model.dto.GatewayConfigurationDTO;
import com.thyng.model.dto.SensorDTO;
import com.thyng.model.dto.ThingDetailsDTO;

public class MessageContext {

	private final Map<Long, ThingDetailsDTO> sensorIdToThingDTO = new HashMap<>();
	private final Map<Long, SensorDTO> sensorIdToSensorDTO = new HashMap<>();

	public MessageContext(GatewayConfigurationDTO details) {
		Objects.requireNonNull(details);
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
