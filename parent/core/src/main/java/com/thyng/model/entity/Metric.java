package com.thyng.model.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

import com.thyng.model.enumeration.DataType;

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
public class Metric extends AuditableEntity {

	@Id
	@GeneratedValue
	private Long id;
	
	@ManyToOne(optional=false)
	private Thing thing;

	@Column(nullable=false)
	private String name;
	
	private String description;
	
	@Column(nullable=false)
	private String abbreviation;
	
	@Column(nullable=false)
	private String unit;
	
	@Column(nullable=false)
	@Enumerated(EnumType.STRING)
	private DataType dataType; 
	
}
