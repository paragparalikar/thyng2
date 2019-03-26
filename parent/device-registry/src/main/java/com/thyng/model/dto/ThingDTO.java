package com.thyng.model.dto;

import lombok.Data;

@Data
public class ThingDTO {

	private Long id;

	private String key;

	private String name;

	private String description;

	private Boolean alive;

	private Boolean biDirectional;

}
