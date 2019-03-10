package com.thyng.model.dto;

import javax.validation.constraints.NotNull;

import lombok.Data;

@Data
public class AddressDTO {

	@NotNull
	private String country;

	@NotNull
	private String state;

	@NotNull
	private String city;

	private String street;

	private String line2;

	private String line1;

}
