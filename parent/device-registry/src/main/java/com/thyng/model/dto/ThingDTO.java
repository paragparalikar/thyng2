package com.thyng.model.dto;

import java.util.Map;
import java.util.Set;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

import lombok.Data;

@Data
public class ThingDTO {

	private Long id;
	
	private String key;

	@NotBlank
	@Size(min=3, max=255)
	private String name;

	@Size(max=255)
	private String description;

	private Boolean alive;

	private Boolean biDirectional;

	private Double altitude;

	private Double latitude;
	
	private Double longitude;
	
	private Long lastEvent;

	private Long lastBeat;
	
	@Size(max=10)
	private Set<String> tags;
	
	private Set<MetricDTO> metrics;

	private Map<String, String> properties;

}
