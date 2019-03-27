package com.thyng.model.dto;

import java.util.Map;
import java.util.Set;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

import lombok.Data;

@Data
public class ThingDetailsDTO {

	private Long id;
	
	private String key;

	@NotBlank
	@Size(min=3, max=255)
	private String name;

	@Size(max=255)
	private String description;

	private Boolean alive;

	@Size(max=10)
	private Set<String> tags;
	
	private Set<MetricDTO> metrics;

	private Map<String, String> properties;

}
