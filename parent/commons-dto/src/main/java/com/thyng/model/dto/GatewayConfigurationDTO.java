package com.thyng.model.dto;

import java.util.Set;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper=true, exclude={"things","mqttClientConfig"})
public class GatewayConfigurationDTO extends GatewayDetailsDTO{
	private static final long serialVersionUID = -2160675713630448055L;

	private Set<ThingDetailsDTO> things;
	
	private MqttClientConfigDTO mqttClientConfig;
	
}
