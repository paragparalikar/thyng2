package com.thyng.model.entity;

import java.util.Map;
import java.util.Set;

import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

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
public class Thing extends AuditableEntity {

	@Id 
	@GeneratedValue
	private Long id;
	
	@ManyToOne(optional=false)
	private Template template;
	
	@ManyToOne(optional=false)
	private Organisation organisation;
	
	private String name;
	
	private String description;
	
	@ElementCollection(fetch=FetchType.EAGER)
	private Set<String> tags;

	@ElementCollection(fetch=FetchType.EAGER)
	private Map<String, String> properties;
	
	private Double altitude;
	private Double latitude;
	private Double longitude;
	
	private Boolean alive;
	
	private Boolean biDirectional;
	
	private Long lastEvent;
	
	private Long lastBeat;
	
}
