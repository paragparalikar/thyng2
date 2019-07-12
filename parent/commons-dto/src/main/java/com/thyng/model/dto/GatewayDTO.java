package com.thyng.model.dto;

import com.thyng.model.enumeration.Protocol;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper=true, exclude={"description","active", 
		"inactivityPeriod", "host", "port", "protocol", "ssl"})
public class GatewayDTO extends GatewayThinDTO{
	private static final long serialVersionUID = -4884894890668527785L;

	private String description;

	private Boolean active;

	private Integer inactivityPeriod;
	
	private Protocol protocol = Protocol.HTTP;
	
	private Boolean ssl = Boolean.FALSE;
	
	private String host;
	
	private Integer port;

}
