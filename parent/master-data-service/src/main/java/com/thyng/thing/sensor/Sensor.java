package com.thyng.thing.sensor;

import javax.persistence.Cacheable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import javax.validation.constraints.Size;

import org.hibernate.annotations.DynamicUpdate;
import org.hibernate.envers.Audited;

import com.thyng.aspect.persistence.AuditableEntity;
import com.thyng.model.enumeration.DataType;
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
	
	private Boolean active;
	
	@Min(10)
	@Builder.Default
	private Integer inactivityPeriod = 10;
	
	@Lob
	private String normalizer;
	
	@NotNull
	@Positive
	@Builder.Default
	private Integer batchSize = 60;
	
}