package com.thyng.model.dto;

import java.util.Set;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper=true)
public class GatewayExtendedDetailsDTO extends GatewayDetailsDTO{

	private Set<ThingDetailsDTO> things;
	
	private MqttClientConfigDTO mqttClientConfig;
}
