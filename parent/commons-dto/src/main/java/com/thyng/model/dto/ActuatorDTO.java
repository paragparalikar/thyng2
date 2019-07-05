package com.thyng.model.dto;

import com.thyng.model.enumeration.Protocol;

import lombok.Data;

@Data
public class ActuatorDTO{

	private Long id;

	private String name;
	
	private String abbreviation;

	private String description;

	private String unit;

	private Protocol protocol;	// depending upon protocol, some of below values will be used 
	
	private Boolean ssl;
	
	private String topic;		// mqtt topic
	
	private String url;		// coap, http

}
