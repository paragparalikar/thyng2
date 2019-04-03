package com.thyng.model.entity;

import javax.persistence.Embeddable;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@Embeddable
@NoArgsConstructor
@AllArgsConstructor
public class Name {

	private String prefix;
	
	private String givenName;
	
	private String middleName;
	
	private String familyName;
	
	private String suffix;
	
}
