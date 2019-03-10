package com.thyng.model.dto;

import java.util.Map;
import java.util.Set;

import lombok.Data;

@Data
public class ThingDTO {

	private Long id;

	private Long templateId;

	private String name;

	private String description;

	private Boolean alive;

	private Boolean biDirectional;

	private Double altitude;

	private Double latitude;
	
	private Double longitude;
	
	private Long lastEvent;

	private Long lastBeat;
	
	private Set<String> tags;

	private Map<String, String> properties;

}
