package com.thyng.domain.gateway;

import com.thyng.domain.actuator.Protocol;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper=true, exclude={"description", "host", "port", "protocol", "ssl"})
public class GatewayDTO extends GatewayThinDTO{
	private static final long serialVersionUID = -4884894890668527785L;

	private String description;
	
	private Protocol protocol = Protocol.HTTP;
	
	private Boolean ssl = Boolean.FALSE;
	
	private String host;
	
	private Integer port;

}
