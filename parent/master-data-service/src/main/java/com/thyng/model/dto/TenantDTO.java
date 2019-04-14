package com.thyng.model.dto;

import java.util.Date;
import java.util.Map;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import lombok.Data;

@Data
public class TenantDTO {

	private Long id;
	
	@NotBlank
	@Size(min=3, max=255)
	private String name;
	
	@Size(max=255)
	private String description;
	
	@NotNull
	private Date start;
	
	@NotNull
	private Date expiry;
	
	private boolean locked;

	private Map<String, String> properties;
	
}
