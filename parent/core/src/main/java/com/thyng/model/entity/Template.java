package com.thyng.model.entity;

import java.util.Map;
import java.util.Set;

import javax.persistence.Access;
import javax.persistence.AccessType;
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
	
	private Set<Metric> metrics;
	
	private Set<Thing> things;
	
	private Integer inactivityPeriod; // seconds

	@ElementCollection(fetch=FetchType.EAGER)
	private Set<String> tags;

	@ElementCollection(fetch=FetchType.EAGER)
	private Map<String, String> properties;
	
	@Access(AccessType.PROPERTY)
	@OneToMany(cascade=CascadeType.ALL, fetch=FetchType.EAGER, orphanRemoval=true, mappedBy="template")
	public Set<Metric> getMetrics(){
		return metrics;
	}
	
	public void setMetrics(Set<Metric> metrics){
		if(null == this.metrics){
			this.metrics = metrics;
		}else{
			this.metrics.clear();
			if(null != metrics){
				this.metrics.addAll(metrics);
			}
		}
	}

	@Access(AccessType.PROPERTY)
	@OneToMany(cascade=CascadeType.REMOVE, fetch=FetchType.LAZY, orphanRemoval=true, mappedBy="template")
	public Set<Thing> getThings(){
		return this.things;
	}
	
	public void setThings(Set<Thing> things){
		if(null == this.things){
			this.things = things;
		}else{
			this.things.clear();
			if(null != things){
				this.things.addAll(things);
			}
		}
	}
	
}
