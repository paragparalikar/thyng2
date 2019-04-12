package com.thyng.model.entity;

import java.util.Date;
import java.util.Map;
import java.util.Set;

import javax.persistence.Cacheable;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

import org.hibernate.envers.Audited;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@Entity
@Builder
@Audited
@Cacheable
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper=false, of={"id", "name"})
public class Tenant extends AuditableEntity{

	@Id
	@GeneratedValue
	private Long id;
	
	@NotBlank
	@Size(min=3, max=255)
	private String name;
	
	@Size(max=255)
	private String description;
	
	@ElementCollection(fetch=FetchType.EAGER)
	private Set<@NotBlank String> tags;

	@ElementCollection(fetch=FetchType.EAGER)
	private Map<@NotBlank String,@NotBlank  String> properties;
	
	@Temporal(TemporalType.TIMESTAMP)
	private Date start;
	
	@Temporal(TemporalType.TIMESTAMP)
	private Date expiry;
	
	private boolean locked;
	
}
