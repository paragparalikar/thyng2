package com.thyng.model.dto;

import java.util.Map;
import java.util.Set;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper=true, of="gatewayId")
public class ThingDetailsDTO extends ThingDTO{

	private Long gatewayId;
	
	private String gatewayName;

	private Set<String> tags;
	
	private Set<SensorDTO> sensors;
	
	private Set<ActuatorDTO> actuators;

	private Map<String,String> properties;
	
}
