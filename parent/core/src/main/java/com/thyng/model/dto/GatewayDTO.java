package com.thyng.model.dto;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper=true, exclude={"description","active", "inactivityPeriod", "host", "port"})
public class GatewayDTO extends GatewayThinDTO{

	private String description;

	private Boolean active;

	private Integer inactivityPeriod;
	
	private String host;
	
	private Integer port;

}
