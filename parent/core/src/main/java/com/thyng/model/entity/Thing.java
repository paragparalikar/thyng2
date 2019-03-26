package com.thyng.model.entity;

import java.util.Map;
import java.util.Set;
import java.util.UUID;

import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;

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
@EqualsAndHashCode(callSuper = false, of={"id", "name"})
public class Thing extends AuditableEntity {

	@Id 
	@GeneratedValue
	private Long id;
	
	@Setter(AccessLevel.NONE)
	@Column(nullable=false, unique=true, updatable=false)
	private String key;
	
	private String name;
	
	private String description;
	
	@ManyToOne(optional=false, fetch=FetchType.LAZY)
	private Tenant tenant;
	
	private Set<Metric> metrics;
	
	@ElementCollection
	private Set<String> tags;

	@ElementCollection
	private Map<String, String> properties;
	
	private Double altitude;
	private Double latitude;
	private Double longitude;
	
	private Boolean alive;
	
	private Boolean biDirectional;
	
	private Long lastEvent;
	
	private Long lastBeat;
	
	@PreUpdate
	@PrePersist
	public void prePersist(){
		key = null == key ? UUID.randomUUID().toString() : key;
	}
	
	public void setKey(String key){
		throw new UnsupportedOperationException();
	}
	
	public void setPath(String path){
		throw new UnsupportedOperationException();
	}
	
	@Access(AccessType.PROPERTY)
	@OneToMany(cascade=CascadeType.ALL, orphanRemoval=true, mappedBy="thing")
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
	
}
