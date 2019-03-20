package com.thyng.model.dto;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import com.thyng.model.enumeration.DataType;

import lombok.Data;

@Data
public class MetricDTO {

	private Long id;
	
	@NotBlank
	@Size(min=3, max=255)
	private String name;
	
	@NotBlank
	@Size(max=255)
	private String abbreviation;
	
	private String description;
	
	@NotBlank
	@Size(max=255)
	private String unit;
	
	@NotNull
	private DataType dataType;
}
