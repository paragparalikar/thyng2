package com.thyng.model.entity;

import javax.persistence.Embeddable;

import lombok.Data;

@Data
@Embeddable
public class Name {

	private String prefix;
	
	private String givenName;
	
	private String middleName;
	
	private String familyName;
	
	private String suffix;
	
}
