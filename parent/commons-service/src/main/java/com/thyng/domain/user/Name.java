package com.thyng.domain.user;

import javax.persistence.Embeddable;

import org.hibernate.envers.Audited;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@Audited
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
