package com.thyng.model.entity;

import java.util.Map;
import java.util.Set;
import java.util.UUID;

import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;
import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import org.hibernate.annotations.Cascade;
import org.hibernate.annotations.CascadeType;

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
	
	@NotBlank
	@Size(min=3, max=255)
	private String name;
	
	@Size(max=255)
	private String description;
	
	@Valid
	@ManyToOne(optional=false, fetch=FetchType.LAZY)
	@JoinColumn(updatable=false, nullable=false)
	private Tenant tenant;
	
	@Cascade({CascadeType.ALL})
	@Access(AccessType.PROPERTY)
	@OneToMany(orphanRemoval=true, mappedBy="thing")
	private Set<@NotNull @Valid Metric> metrics;
	
	@ElementCollection
	private Set<@NotBlank String> tags;

	@ElementCollection
	private Map<@NotBlank String,@NotBlank  String> properties;
	
	private Boolean alive;
	
	@PreUpdate
	@PrePersist
	public void prePersist(){
		key = null == key ? UUID.randomUUID().toString() : key;
	}
	
	public void setKey(String key){
		throw new UnsupportedOperationException();
	}
	
}
