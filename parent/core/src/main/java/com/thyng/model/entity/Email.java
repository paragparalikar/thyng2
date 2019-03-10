package com.thyng.model.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@Entity
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper=true)
public class Email extends AuditableEntity{

	@Id 
	@GeneratedValue
	private Long id;
	
	private String value;
	
	private String type;
	
	@Column(name="PRIMARY_")
	private Boolean primary;
	
}
