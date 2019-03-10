package com.thyng.model.entity;

import javax.persistence.MappedSuperclass;

import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;

@MappedSuperclass
public abstract class AuditableEntity {

	@CreatedBy
	private Long createdBy;
	
	@CreatedDate
	private Long createdDate;

	@LastModifiedBy
	private Long lastModifiedBy;
	
	@LastModifiedDate
	private Long lastModifiedDate;
	
}
