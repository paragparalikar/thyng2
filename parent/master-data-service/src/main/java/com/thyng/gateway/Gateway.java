package com.thyng.gateway;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import javax.persistence.Cacheable;
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
import javax.validation.constraints.Positive;
import javax.validation.constraints.Size;

import org.hibernate.annotations.Cascade;
import org.hibernate.annotations.CascadeType;
import org.hibernate.annotations.DynamicUpdate;
import org.hibernate.envers.Audited;

import com.thyng.aspect.persistence.AuditableEntity;
import com.thyng.tenant.Tenant;
import com.thyng.thing.Thing;

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
@EqualsAndHashCode(callSuper=false, of={"id", "name"})
public class Gateway extends AuditableEntity implements Cloneable{

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
	private Map<@NotBlank String,@NotBlank  String> properties;
	
	@Size(max=255)
	private String host;
	
	@Positive
	private Integer port;
	
	private Boolean active;
	
	@Min(60)
	@Builder.Default
	private Integer inactivityPeriod = 60;
		
	@Cascade({CascadeType.ALL})
	@OneToMany(mappedBy="gateway")
	private Set<com.thyng.thing.Thing> things;
	
	@Valid
	@Cascade({CascadeType.ALL})
	@OneToOne(fetch=FetchType.LAZY)
	private MqttClientConfig mqttClientConfig;
	
	public Gateway(Long id, String name){
		this.id = id;
		this.name = name;
	}
	
	@Override
	public Gateway clone() {
		return Gateway.builder()
				.id(getId())
				.name(getName())
				.description(getDescription())
				.tenant(getTenant())
				.properties(new HashMap<>(getProperties()))
				.host(getHost())
				.port(getPort())
				.active(getActive())
				.inactivityPeriod(getInactivityPeriod())
				.things(new HashSet<>(getThings().stream().map(Thing::clone).collect(Collectors.toSet())))
				.mqttClientConfig(getMqttClientConfig())
				.build();
	}
	
}
