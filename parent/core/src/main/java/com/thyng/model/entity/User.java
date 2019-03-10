package com.thyng.model.entity;

import java.util.Set;

import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.MapsId;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@Entity
@EqualsAndHashCode(callSuper=true)
public class User extends AuditableEntity{
	
	@Id 
	@GeneratedValue
	private Long id;

	@MapsId
	@ManyToOne
	private Organisation organisation;
	
	@Embedded
	private Name name;
	
	@MapsId
	@OneToOne
	private Address address;
	
	@OneToMany
	private Set<Email> emails;
	
	@OneToMany
	private Set<Phone> phones;
	
}
