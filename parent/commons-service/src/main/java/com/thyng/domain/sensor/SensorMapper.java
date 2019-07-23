package com.thyng.domain.sensor;

import java.util.Collection;
import java.util.Set;

import org.mapstruct.InheritInverseConfiguration;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel="spring")
public interface SensorMapper {

	@Mapping(source = "thing.id", target = "thingId")
	SensorDTO toDTO(Sensor sensor);
	
	Set<SensorDTO> toDTO(Collection<? extends Sensor> sensors);
	
	@InheritInverseConfiguration
	Sensor toEntity(SensorDTO dto);
}
