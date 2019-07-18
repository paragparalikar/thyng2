package com.thyng.model.dto;

import com.thyng.model.enumeration.MetricsType;

import lombok.Data;

@Data
public class MetricsSchemaDTO {

	private Long id;

	private String name;
	
	private String description;

	private MetricsType type;

	private String sensorIdQualifier;
	
	private String timestampQualifier;
	
	private String valueQualifier;

}
