package com.thyng.domain.user;

import java.util.Map;
import java.util.Set;

import javax.persistence.Cacheable;
import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.validation.Valid;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import org.hibernate.envers.Audited;

import com.thyng.configuration.persistence.AuditableEntity;
import com.thyng.domain.tenant.Tenant;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Data
@Entity
@Builder
@Audited
@Cacheable
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper=false, of={"id","email"})
public class User extends AuditableEntity{

	@Id 
	@GeneratedValue
	private Long id;

	@NotNull
	@Embedded
	private Name name;
	
	@Email
	@NotBlank
	@Column(nullable=false, unique=true)
	private String email;
	
	private String phone;
	
	@Valid
	@ManyToOne(optional=false)
	@JoinColumn(updatable=false, nullable=false)
	private Tenant tenant;

	@ElementCollection(fetch=FetchType.EAGER)
	private Map<String, String> properties;
	
	@Builder.Default()
	private boolean enabled = true;
	
	private boolean accountExpired;
	
	private boolean accountLocked;
	
	private boolean credencialsExpired;
	
	private String password;
	
	public void eraseCredentials() {
		password = null;
	}

	public String getUsername() {
		return email;
	}

	public boolean isAccountNonExpired() {
		return !accountExpired;
	}

	public boolean isAccountNonLocked() {
		return !accountLocked;
	}

	public boolean isCredentialsNonExpired() {
		return !credencialsExpired;
	}

	public boolean isEnabled() {
		return enabled;
	}
	
	@Getter(value = AccessLevel.NONE)
	@ElementCollection(fetch = FetchType.EAGER)
	private Set<Authority> authorities;
	
	public Set<Authority> getThyngAuthorities(){
		return authorities;
	}
	
	public boolean hasAuthority(final String value) {
		return null == value ? false : authorities.stream()
				.anyMatch(authority -> null != authority && 
				value.trim().equalsIgnoreCase(authority.name()));
	}
		
}
