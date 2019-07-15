package com.thyng.model;

import com.thyng.model.dto.GatewayConfigurationDTO;

import lombok.Value;

@Value
public class RegistrationResponse {

	private final GatewayConfigurationDTO gatewayConfigurationDTO;

}
