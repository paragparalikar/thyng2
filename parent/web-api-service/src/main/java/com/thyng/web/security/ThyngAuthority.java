package com.thyng.web.security;

import org.springframework.security.core.GrantedAuthority;

import com.thyng.model.enumeration.Authority;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class ThyngAuthority implements GrantedAuthority {
	private static final long serialVersionUID = 5241953488239747651L;

	@NonNull
	private final Authority authority;

	@Override
	public String getAuthority() {
		return getAuthorityEnum().name();
	}
	
	public Authority getAuthorityEnum() {
		return authority;
	}

}
