package com.thyng.model.entity;

import java.util.Map;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
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
@EqualsAndHashCode(callSuper = true, of={"id", "name", "organisation"})
public class Template extends AuditableEntity {

	@Id
	@GeneratedValue
	private Long id;
	
	@ManyToOne
	private Organisation organisation;

	private String name;

	private String description;
	
	@OneToMany(cascade=CascadeType.ALL, fetch=FetchType.EAGER, orphanRemoval=true, mappedBy="template")
	private Set<Metric> metrics;
	
	@OneToMany(cascade=CascadeType.ALL, fetch=FetchType.LAZY, orphanRemoval=true, mappedBy="template")
	private Set<Thing> things;
	
	private Integer inactivityPeriod; // seconds

	@ElementCollection(fetch=FetchType.EAGER)
	private Set<String> tags;

	@ElementCollection(fetch=FetchType.EAGER)
	private Map<String, String> properties;

}
