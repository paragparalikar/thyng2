package com.thyng.model.entity;

import javax.persistence.Cacheable;
import javax.persistence.Embeddable;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import org.hibernate.envers.Audited;

import com.thyng.model.enumeration.MqttQoS;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@Audited
@Cacheable
@Embeddable
@NoArgsConstructor
@AllArgsConstructor
public class MqttLastWill {

	@NotBlank
	@Size(min=3, max=255)
	private String topic;
	
	private String message;
	
	private Boolean retain;
	
	@NotNull
	@Enumerated(EnumType.STRING)
	private MqttQoS qos;

}
