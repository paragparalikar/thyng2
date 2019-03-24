package com.thyng.model.dto;

import java.util.Set;

import com.thyng.model.entity.Name;
import com.thyng.model.enumeration.Authority;

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

}
