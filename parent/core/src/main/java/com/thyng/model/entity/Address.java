package com.thyng.model.entity;

import javax.persistence.Entity;
import javax.persistence.Id;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@Entity
@EqualsAndHashCode(callSuper=true)
public class Address extends AuditableEntity{
	
	@Id 
	private Long id;
	
	private String country;

	private String state;

	private String city;

	private String street;

	private String line2;

	private String line1;

}
