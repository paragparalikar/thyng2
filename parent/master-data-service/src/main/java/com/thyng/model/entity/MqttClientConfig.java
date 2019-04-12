package com.thyng.model.entity;

import javax.persistence.Cacheable;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import javax.validation.constraints.Size;

import org.hibernate.envers.Audited;

import com.thyng.model.enumeration.MqttVersion;

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
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = false, of = { "id" })
public class MqttClientConfig {

	@Id
	@GeneratedValue
	private Long id;
	
	@NotBlank
	@Size(min=3, max=255)
	@Builder.Default
	private String host = "0.0.0.0";
	
	@Positive
	@Builder.Default
	private int port = 1883;

	@NotBlank
	@Size(min=3, max=255)
	@Builder.Default
	private String clientId = "thyng-gateway";
	
	@Positive
	@Builder.Default
	private Integer timeoutSeconds = 10;
	
	@NotNull
	@Builder.Default
	private MqttVersion protocolVersion = MqttVersion.MQTT_3_1;
	
	private String username;
	
	private String password;
	
	private Boolean cleanSession;
	
	@Valid
	@Embedded
	private MqttLastWill lastWill;

}
