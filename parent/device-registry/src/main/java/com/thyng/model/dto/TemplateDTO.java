package com.thyng.model.dto;

import java.util.Map;
import java.util.Set;

import javax.validation.Valid;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import lombok.Data;

@Data
public class TemplateDTO {

	private Long id;

	@NotBlank
	@Size(min=3, max=255)
	private String name;
	
	@NotNull
	@Min(60)
	private Integer inactivityPeriod;

	@Size(max=255)
	private String description;
	
	@Valid
	private Set<MetricDTO> metrics;
	
	@Size(max=10)
	private Set<String> tags;

	private Map<String, String> properties;

}
