package com.thyng.user;

import java.util.Set;

import com.thyng.aspect.security.Authority;

import lombok.Data;

@Data
public class UserDTO {

	private Long id;

	private Name name;

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
