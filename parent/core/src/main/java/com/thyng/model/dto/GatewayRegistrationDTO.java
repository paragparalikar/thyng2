package com.thyng.model.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class GatewayRegistrationDTO{

	private Long gatewayId;
	
	private Integer port;

}
