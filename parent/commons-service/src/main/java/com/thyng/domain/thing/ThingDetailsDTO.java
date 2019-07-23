package com.thyng.domain.thing;

import java.util.Map;
import java.util.Set;

import com.thyng.domain.actuator.ActuatorDTO;
import com.thyng.domain.sensor.SensorDTO;

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
