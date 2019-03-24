package com.thyng.model.dto;

import javax.validation.constraints.NotBlank;
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
	
}
