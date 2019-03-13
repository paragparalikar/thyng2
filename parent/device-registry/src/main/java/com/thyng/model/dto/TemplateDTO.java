package com.thyng.model.dto;

import java.util.Map;
import java.util.Set;

import lombok.Data;

@Data
public class TemplateDTO {

	private Long id;

	private String name;
	
	private Integer inactivityPeriod;

	private String description;
	
	private Set<MetricDTO> metrics;
	
	private Set<String> tags;

	private Map<String, String> properties;

}
