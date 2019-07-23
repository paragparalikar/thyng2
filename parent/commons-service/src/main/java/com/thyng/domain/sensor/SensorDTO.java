package com.thyng.domain.sensor;

import java.io.Serializable;
import java.util.Map;

import lombok.Data;

@Data
public class SensorDTO implements Serializable{
	private static final long serialVersionUID = -2583080147898354500L;

	private Long id;
	
	private Long thingId;
	
	private String name;
	
	private String abbreviation;
	
	private String description;
	
	private String unit;
	
	private Map<String,String> properties;
	
	private String normalizer;
	
	private Language normalizerLanguage;
	
	private Integer inactivityPeriod;
}
