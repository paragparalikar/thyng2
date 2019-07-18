package com.thyng.service;

import javax.transaction.Transactional;

import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import com.thyng.entity.MetricsSchema;
import com.thyng.model.exception.NotFoundException;
import com.thyng.repository.spring.data.jpa.MetricsSchemaRepository;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;

@Service
@Validated
@Transactional
@RequiredArgsConstructor
public class MetricsSchemaService {

	private final MetricsSchemaRepository metricsSchemaRepository;
	
	public MetricsSchema findById(@NonNull final Long id){
		return metricsSchemaRepository.findById(id)
				.orElseThrow(() -> new NotFoundException("MetricsSchema not found for id "+id));
	}
	
}
