package com.thyng.model.enumeration;

import org.springframework.security.core.GrantedAuthority;

/**
 * In the form of {ENTITY}_{OPERATION}
 * ENTITY: {TENANT, USER, THING, RULE...}
 * OPERATION: {READ, WRITE, EXECUTE}
 */
public enum Authority implements GrantedAuthority{

	TENANT_LIST, TENANT_VIEW, TENANT_UPDATE, TENANT_CREATE, TENANT_DELETE,
	USER_LIST, USER_VIEW, USER_UPDATE, USER_CREATE, USER_DELETE,
	THING_LIST, THING_VIEW, THING_UPDATE, THING_CREATE, THING_DELETE;

	@Override
	public String getAuthority() {
		return name();
	}
	
}
