package com.thyng.domain.gateway;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import javax.persistence.Cacheable;
import javax.persistence.CascadeType;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import javax.validation.constraints.Size;

import org.hibernate.annotations.DynamicUpdate;
import org.hibernate.envers.Audited;

import com.thyng.configuration.persistence.AuditableEntity;
import com.thyng.domain.actuator.Protocol;
import com.thyng.domain.mqtt.MqttClientConfig;
import com.thyng.domain.tenant.Tenant;
import com.thyng.domain.thing.Thing;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.NonNull;

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
	
	@NotNull
	@NonNull
	@Builder.Default
	@Enumerated(EnumType.STRING)
	private Protocol protocol = Protocol.HTTP;
	
	@NotNull
	@NonNull
	@Builder.Default
	private Boolean ssl = Boolean.FALSE;
	
	// private byte[] keyStore;
	// private String keyStorePassword;
	
	@Size(max=255)
	private String host;
	
	@Positive
	private Integer port;
		
	@OneToMany(mappedBy="gateway", cascade = CascadeType.ALL, orphanRemoval = true)
	private Set<com.thyng.domain.thing.Thing> things;
	
	@Valid
	@OneToOne(fetch=FetchType.LAZY, cascade = CascadeType.ALL)
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
				.things(new HashSet<>(getThings().stream().map(Thing::clone).collect(Collectors.toSet())))
				.mqttClientConfig(getMqttClientConfig())
				.build();
	}
	
}
