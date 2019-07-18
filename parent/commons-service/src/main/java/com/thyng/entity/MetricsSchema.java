package com.thyng.entity;

import javax.persistence.Cacheable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

import org.hibernate.annotations.DynamicUpdate;
import org.hibernate.envers.Audited;

import com.thyng.model.enumeration.MetricsType;

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
public class MetricsSchema extends AuditableEntity {
	
	@Id
	@GeneratedValue
	private Long id;

	@NotBlank
	@Size(min=3, max=255)
	@Column(nullable=false)
	private String name;
	
	@Size(max=255)
	private String description;

	@Enumerated(EnumType.STRING)
	private MetricsType type;

	@NotBlank
	@Size(max=255)
	@Column(nullable=false)
	private String sensorIdQualifier;
	
	@Size(max=255)
	private String timestampQualifier;
	
	@NotBlank
	@Size(max=255)
	@Column(nullable=false)
	private String valueQualifier;
	
}
