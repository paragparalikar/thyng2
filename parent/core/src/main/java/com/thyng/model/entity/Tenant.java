package com.thyng.model.entity;

import java.util.Date;
import java.util.Map;
import java.util.Set;

import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

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
@EqualsAndHashCode(callSuper=false, of={"id", "name"})
public class Tenant extends AuditableEntity{

	@Id
	@GeneratedValue
	private Long id;
	
	private String name;
	
	private String description;
	
	@ElementCollection(fetch=FetchType.EAGER)
	private Set<String> tags;

	@ElementCollection(fetch=FetchType.EAGER)
	private Map<String, String> properties;
	
	@Temporal(TemporalType.TIMESTAMP)
	private Date start;
	
	@Temporal(TemporalType.TIMESTAMP)
	private Date expiry;
	
	private boolean locked;
	
}
