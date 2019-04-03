package com.thyng.model.entity;

import java.util.Map;
import java.util.Set;

import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.validation.Valid;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import org.hibernate.annotations.Cascade;
import org.hibernate.annotations.CascadeType;

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
public class Gateway {

	@Id
	@GeneratedValue
	private Long id;
	
	@NotBlank
	@Size(min=3, max=255)
	private String name;
	
	@Size(max=255)
	private String description;
	
	@Valid
	@ManyToOne(optional=false, fetch=FetchType.LAZY)
	@JoinColumn(updatable=false, nullable=false)
	private Tenant tenant;
	
	@ElementCollection
	private Set<@NotBlank String> tags;

	@ElementCollection
	private Map<@NotBlank String,@NotBlank  String> properties;
	
	private Boolean alive;
	
	@Min(60)
	private Integer inactivityPeriod;
	
	@Min(10)
	private Integer heartbeatInterval;
	
	@Cascade({CascadeType.ALL})
	@OneToMany(orphanRemoval=true, mappedBy="gateway")
	private Set<@Valid @NotNull Thing> things;
	
	@Valid
	@Cascade({CascadeType.ALL})
	@OneToOne(fetch=FetchType.LAZY)
	private MqttClientConfig mqttClientConfig;
	
	@Valid
	@Cascade({CascadeType.ALL})
	@OneToOne(fetch=FetchType.LAZY, mappedBy="gateway")
	private GatewayRegistration registration;
}
