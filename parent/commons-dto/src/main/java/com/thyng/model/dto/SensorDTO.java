package com.thyng.model.dto;

import lombok.Data;

@Data
public class SensorDTO {

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
