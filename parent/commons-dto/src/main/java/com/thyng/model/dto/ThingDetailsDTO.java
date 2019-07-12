package com.thyng.model.dto;

import java.util.Map;
import java.util.Set;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper=true)
public class ThingDetailsDTO extends ThingDTO{
	private static final long serialVersionUID = -3677623336031513193L;

	private Set<SensorDTO> sensors;
	
	private Set<ActuatorDTO> actuators;

	private Map<String,String> properties;
	
}
