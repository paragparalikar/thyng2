package com.thyng.model.mapper;

import java.util.Collection;
import java.util.Set;

import org.mapstruct.InheritInverseConfiguration;
import org.mapstruct.Mapper;

import com.thyng.model.dto.SensorDTO;
import com.thyng.model.entity.Sensor;

@Mapper(componentModel="spring")
public interface SensorMapper {

	SensorDTO toDTO(Sensor sensor);
	
	Set<SensorDTO> toDTO(Collection<? extends Sensor> sensors);
	
	@InheritInverseConfiguration
	Sensor toEntity(SensorDTO dto);
}
