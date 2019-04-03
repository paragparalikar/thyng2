package com.thyng.model.dto;

import java.util.Map;
import java.util.Set;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper=true)
public class GatewayDetailsDTO extends GatewayDTO{

	private Set<String> tags;

	private Map<String,String> properties;
	
	private Set<ThingDetailsDTO> things;
	
	private MqttClientConfigDTO mqttClientConfig;
	
}
