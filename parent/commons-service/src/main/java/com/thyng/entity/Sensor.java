package com.thyng.entity;

import java.util.Map;

import javax.persistence.Cacheable;
import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

import org.hibernate.annotations.DynamicUpdate;
import org.hibernate.envers.Audited;

import com.thyng.model.enumeration.Language;

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
	
	@ElementCollection
	private Map<@NotBlank String,@NotBlank  String> properties;
	
	@Min(value = 3)
	@Column(nullable=false)
	private Integer inactivityPeriod;
	
	@Lob
	private String normalizer;
	
	@Enumerated(EnumType.STRING)
	private Language normalizerLanguage;
	
	@ManyToOne
	private MetricsSchema metricsSchema;
	
}
