package com.thyng.model.enumeration;

import org.springframework.security.core.GrantedAuthority;

/**
 * In the form of {ENTITY}_{OPERATION}
 * ENTITY: {TENANT, USER, THING, RULE...}
 * OPERATION: {READ, WRITE, EXECUTE}
 */
public enum Authority implements GrantedAuthority{

	TENANT_READ, TENANT_WRITE,
	USER_READ, USER_WRITE,
	THING_READ, THING_WRITE;

	@Override
	public String getAuthority() {
		return name();
	}
	
}
