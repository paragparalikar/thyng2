package com.thyng.model.dto;

import java.util.Map;
import java.util.Set;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper=true)
public class ThingDetailsDTO extends ThingDTO{

	private Set<SensorDTO> sensors;
	
	private Set<ActuatorDTO> actuators;

	private Map<String,String> properties;
	
}
