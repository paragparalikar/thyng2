package com.thyng.model.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import com.thyng.model.enumeration.DataType;
import com.thyng.model.enumeration.Protocol;

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
@EqualsAndHashCode(callSuper = false, of={"id", "name"})
public class Sensor extends AuditableEntity {

	@Id
	@GeneratedValue
	private Long id;
	
	@ManyToOne(optional=false)
	private Thing thing;

	@NotBlank
	@Size(min=3, max=255)
	@Column(nullable=false)
	private String name;
	
	@Size(max=255)
	private String description;
	
	@NotBlank
	@Size(max=255)
	@Column(nullable=false)
	private String abbreviation;
	
	@NotBlank
	@Size(max=255)
	@Column(nullable=false)
	private String unit;
	
	@NotNull
	@Builder.Default
	@Column(nullable=false)
	@Enumerated(EnumType.STRING)
	private DataType dataType = DataType.NUMBER; 
	
	@Min(10)
	@Builder.Default
	private Integer inactivityPeriod = 10;
	
	@Min(60)
	@Builder.Default
	private Integer aggregationPeriod = 60;
	
	@NotNull
	@Builder.Default
	@Column(nullable=false)
	@Enumerated(EnumType.STRING)
	private Protocol protocol = Protocol.MQTT; 
	
	@Size(max=255)
	private String topic;		// mqtt topic
	
	@Size(max=255)
	private String host;		// coap, http, https IP address
	
	private Integer port;			// coap, http, https port
	
	@Size(max=255)
	private String path;		// http, https context path
	
}
