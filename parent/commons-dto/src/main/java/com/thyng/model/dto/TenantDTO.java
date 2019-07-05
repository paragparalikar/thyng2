package com.thyng.model.dto;

import java.util.Date;
import java.util.Map;

import lombok.Data;

@Data
public class TenantDTO {

	private Long id;
	
	private String name;
	
	private String description;
	
	private Date start;
	
	private Date expiry;
	
	private boolean locked;

	private Map<String, String> properties;
	
}
