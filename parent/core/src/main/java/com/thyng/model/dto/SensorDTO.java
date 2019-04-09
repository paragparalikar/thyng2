package com.thyng.model.dto;

import com.thyng.model.enumeration.DataType;

import lombok.Data;

@Data
public class SensorDTO {

	private Long id;
	
	private String name;
	
	private String abbreviation;
	
	private String description;
	
	private String unit;
	
	private DataType dataType;
	
	private Integer inactivityPeriod;
	
	private Integer aggregationPeriod;
		
	private String normalizer;
	
	private Integer batchSize;
	
}
