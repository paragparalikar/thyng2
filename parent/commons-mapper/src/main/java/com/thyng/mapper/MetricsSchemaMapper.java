package com.thyng.mapper;

import org.mapstruct.Mapper;

import com.thyng.entity.MetricsSchema;
import com.thyng.model.dto.MetricsSchemaDTO;

@Mapper(componentModel = "spring")
public interface MetricsSchemaMapper {

	MetricsSchemaDTO toDTO(MetricsSchema entiry);
	
	MetricsSchema toEntity(MetricsSchemaDTO dto);
	
}
