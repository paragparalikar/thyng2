package com.thyng.model.dto;

import java.io.Serializable;
import java.util.Set;

import com.thyng.model.enumeration.Authority;

import lombok.Data;

@Data
public class UserDTO implements Serializable {
	private static final long serialVersionUID = 4876218028798611614L;

	private Long id;

	private String prefix;
	
	private String givenName;
	
	private String middleName;
	
	private String familyName;
	
	private String suffix;

	private String email;

	private String phone;
	
	private boolean enabled = true;
	
	private boolean accountExpired;
	
	private boolean accountLocked;
	
	private boolean credencialsExpired;
	
	private Set<Authority> authorities;
	
	public boolean hasAuthority(Authority authority){
		return null != authorities && authorities.contains(authority);
	}

}
