package com.thyng.model.entity;

import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToMany;

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
	
	@OneToMany(cascade=CascadeType.ALL, orphanRemoval=true, mappedBy="tenant")
	private Set<User> users;
	
	@OneToMany(cascade=CascadeType.ALL, orphanRemoval=true, mappedBy="tenant")
	private Set<Thing> things;
	
}
