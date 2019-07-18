package com.thyng.mapper;

import java.util.Collection;
import java.util.Set;

import org.mapstruct.InheritInverseConfiguration;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import com.thyng.entity.Sensor;
import com.thyng.model.dto.SensorDTO;

@Mapper(componentModel="spring", uses = MetricsSchemaMapper.class)
public interface SensorMapper {

	@Mapping(source = "thing.id", target = "thingId")
	@Mapping(source = "metricsSchema.id", target = "metricsSchemaId")
	@Mapping(source = "metricsSchema.name", target = "metricsSchemaName")
	SensorDTO toDTO(Sensor sensor);
	
	Set<SensorDTO> toDTO(Collection<? extends Sensor> sensors);
	
	@InheritInverseConfiguration
	Sensor toEntity(SensorDTO dto);
}
