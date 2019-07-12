package com.thyng.model.dto;

import java.io.Serializable;

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
	
	private Boolean active;
	
	private Integer inactivityPeriod;
	
	private String normalizer;
	
	private Integer batchSize;
	
}
