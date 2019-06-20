package com.thyng.model.entity;

import javax.persistence.Cacheable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import org.hibernate.envers.Audited;

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
@Audited
@Cacheable
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = false, of={"id", "name"})
public class Actuator extends AuditableEntity {

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
	private String unit;
	
	@NotNull
	@Builder.Default
	@Column(nullable=false)
	@Enumerated(EnumType.STRING)
	private DataType dataType = DataType.NUMBER; 
	
	@NotNull
	@Builder.Default
	@Column(nullable=false)
	@Enumerated(EnumType.STRING)
	private Protocol protocol = Protocol.COAP; 
	
	@Size(max=255)
	private String topic;
	
	@Size(max=255)
	private String host;
	
	private Integer port;
	
	@Size(max=255)
	private String path;

}
