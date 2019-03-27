package com.thyng.model.entity;

import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Transient;

import org.springframework.security.core.CredentialsContainer;
import org.springframework.security.core.userdetails.UserDetails;

import com.thyng.model.enumeration.Authority;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper=false, of={"id","email"})
public class User extends AuditableEntity implements UserDetails, CredentialsContainer{
	private static final long serialVersionUID = 8339215430882886024L;

	@Id 
	@GeneratedValue
	private Long id;

	@Embedded
	private Name name;
	
	@Column(nullable=false, unique=true)
	private String email;
	
	private String phone;
	
	@ManyToOne(optional=false)
	private Tenant tenant;
	
	@ElementCollection(fetch=FetchType.EAGER)
	private Set<String> tags;

	@ElementCollection(fetch=FetchType.EAGER)
	private Map<String, String> properties;
	
	@Builder.Default()
	private boolean enabled = true;
	
	private boolean accountExpired;
	
	private boolean accountLocked;
	
	private boolean credencialsExpired;
	
	private String password;
	
	@Override
	public void eraseCredentials() {
		password = null;
	}

	@Override
	public String getUsername() {
		return email;
	}

	@Override
	public boolean isAccountNonExpired() {
		return !accountExpired;
	}

	@Override
	public boolean isAccountNonLocked() {
		return !accountLocked;
	}

	@Override
	public boolean isCredentialsNonExpired() {
		return !credencialsExpired;
	}

	@Override
	public boolean isEnabled() {
		return enabled;
	}
	
	
	
	@Transient
	private final Set<Authority> authorities = new HashSet<>();
	
	@Override
	public Collection<Authority> getAuthorities() {
		if(authorities.isEmpty()){
			authorities.addAll(Arrays.asList(Authority.values()));
		}
		authorities.remove(Authority.TENANT_CREATE);
		authorities.remove(Authority.TENANT_UPDATE);
		authorities.remove(Authority.TENANT_DELETE);
		return authorities;
	}
	
	public boolean hasAuthority(String authority){
		return null != authority && getAuthorities().stream()
				.filter(auth -> null != auth && auth.name().trim().equalsIgnoreCase(authority.trim()))
				.findFirst().isPresent();
	}
	
}
