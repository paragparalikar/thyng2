package com.thyng.model.dto;

import java.io.Serializable;

import com.thyng.model.enumeration.Protocol;

import lombok.Data;

@Data
public class ActuatorDTO implements Serializable{
	private static final long serialVersionUID = -2884676290657804833L;

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
