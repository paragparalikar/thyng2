package com.thyng.model.mapper;

import java.util.Collection;
import java.util.Set;

import org.mapstruct.InheritInverseConfiguration;
import org.mapstruct.Mapper;

import com.thyng.model.dto.MetricDTO;
import com.thyng.model.entity.Metric;

@Mapper(componentModel="spring")
public interface MetricMapper {

	MetricDTO toDTO(Metric metric);
	
	Set<MetricDTO> toDTO(Collection<? extends Metric> metrics);
	
	@InheritInverseConfiguration
	Metric toEntity(MetricDTO dto);
}
