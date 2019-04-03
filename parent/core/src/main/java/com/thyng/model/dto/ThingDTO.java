package com.thyng.model.dto;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(of={"id","name"})
public class ThingDTO{

	private Long id;

	private String name;
	
	private String description;

	private Boolean alive;

}