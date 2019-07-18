package com.thyng.model.dto;

import java.io.Serializable;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(of={"id","name"})
public class ThingDTO implements Serializable{
	private static final long serialVersionUID = -234777795990134486L;

	private Long id;

	private String name;
	
	private String description;
	
	private Long gatewayId;
	
	private String gatewayName;
	
}
