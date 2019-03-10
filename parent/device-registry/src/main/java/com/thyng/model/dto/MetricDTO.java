package com.thyng.model.dto;

import com.thyng.model.enumeration.DataType;

import lombok.Data;

@Data
public class MetricDTO {

	private Long id;
	
	private String name;
	
	private String abbreviation;
	
	private String description;
	
	private String unit;
	
	private DataType dataType;
}
