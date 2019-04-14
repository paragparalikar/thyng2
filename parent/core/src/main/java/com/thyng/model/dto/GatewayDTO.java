package com.thyng.model.dto;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(of={"id","name"})
public class GatewayDTO{

	private Long id;

	private String name;

	private String description;

	private Boolean active;

	private Integer inactivityPeriod;
	
	private String host;
	
	private Integer port;

}
