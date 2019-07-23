package com.thyng.web.security;

import java.util.Collection;
import java.util.Collections;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.security.core.CredentialsContainer;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import com.thyng.domain.tenant.Tenant;
import com.thyng.domain.user.Authority;
import com.thyng.domain.user.Name;
import com.thyng.domain.user.User;

import lombok.Builder;
import lombok.NonNull;

public class ThyngUserDetails extends User implements UserDetails, CredentialsContainer {
	private static final long serialVersionUID = 1L;
	
	private final Set<ThyngAuthority> thyngAuthorities;
	
	public ThyngUserDetails(final User user) {
		this(	user.getId(), 
				user.getName(), 
				user.getEmail(), 
				user.getPhone(), 
				user.getTenant(), 
				user.getProperties(), 
				user.isEnabled(),
				user.isAccountExpired(), 
				user.isAccountLocked(), 
				user.isCredencialsExpired(), 
				user.getPassword(), 
				user.getThyngAuthorities());
	}

	@Builder(builderMethodName="thyngUserDetailsBuilder")
	public ThyngUserDetails(
			@NonNull final Long id, 
			@NonNull final Name name, 
			@NonNull final String email, 
			final String phone, 
			@NonNull final Tenant tenant,
			final Map<String, String> properties, 
			final boolean enabled, 
			final boolean accountExpired, 
			final boolean accountLocked,
			final boolean credencialsExpired, 
			final String password, 
			@NonNull final Set<Authority> authorities) {
		super(id, name, email, phone, tenant, properties, enabled, accountExpired, accountLocked, credencialsExpired,
				password, authorities);
		thyngAuthorities = Collections.unmodifiableSet(
				authorities.stream()
				.map(ThyngAuthority::new)
				.collect(Collectors.toSet()));
	}

	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		return thyngAuthorities;
	}

}
