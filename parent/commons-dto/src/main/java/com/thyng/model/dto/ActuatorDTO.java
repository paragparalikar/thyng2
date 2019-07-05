package com.thyng.model.dto;

import com.thyng.model.enumeration.DataType;
import com.thyng.model.enumeration.Protocol;

import lombok.Data;

@Data
public class ActuatorDTO{

	private Long id;

	private String name;
	
	private String abbreviation;

	private String description;

	private String unit;

	private DataType dataType;
	
	private Protocol protocol;	// depending upon protocol, some of below values will be used 
	
	private String topic;		// mqtt topic
	
	private String host;		// coap, http, https IP address
	
	private Integer port;		// coap, http, https port
	
	private String path;		// http, https context path

}
