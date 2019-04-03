package com.thyng.model.dto;

import lombok.Data;

@Data
public class GatewayRegistrationDTO{

	private Long gatewayId;
	
	private String host;
	
	private Integer port;

}
