package com.thyng.model.entity;

import java.util.Map;
import java.util.Set;
import java.util.UUID;

import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.PrePersist;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Data
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true, of={"id", "name"})
public class Thing extends AuditableEntity {

	@Id 
	@GeneratedValue
	private Long id;
	
	@Setter(AccessLevel.NONE)
	@Column(nullable=false, unique=true, updatable=false)
	private String key;
	
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
	
	@PrePersist
	public void prePersist(){
		key = UUID.randomUUID().toString();
	}
	
}
