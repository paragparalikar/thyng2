package com.thyng.model.entity;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import javax.persistence.Cacheable;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import org.hibernate.annotations.Cascade;
import org.hibernate.annotations.CascadeType;
import org.hibernate.annotations.DynamicUpdate;
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
@DynamicUpdate
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = false, of={"id", "name"})
public class Thing extends AuditableEntity implements Cloneable{

	@Id 
	@GeneratedValue
	private Long id;
	
	@NotBlank
	@Size(min=3, max=255)
	private String name;
	
	@Size(max=255)
	private String description;
	
	@Valid
	@ManyToOne(optional=false)
	@JoinColumn(updatable=false, nullable=false)
	private Tenant tenant;
	
	@Valid
	@ManyToOne
	private Gateway gateway;
	
	@Cascade({CascadeType.ALL})
	@OneToMany(orphanRemoval=true, mappedBy="thing")
	private Set<@NotNull @Valid Sensor> sensors;
	
	@Cascade({CascadeType.ALL})
	@OneToMany(orphanRemoval=true, mappedBy="thing")
	private Set<@NotNull @Valid Actuator> actuators;
	
	@ElementCollection
	private Map<@NotBlank String,@NotBlank  String> properties;
	
	private Boolean active;
	
	@Override
	protected Thing clone(){
		return Thing.builder()
				.id(getId())
				.name(getName())
				.description(getDescription())
				.tenant(getTenant())
				.gateway(getGateway())
				.active(getActive())
				.properties(new HashMap<>(getProperties()))
				.sensors(new HashSet<>(getSensors()))
				.actuators(new HashSet<>(getActuators()))
				.build();
	}
}
