package com.thyng.model.mapper;

import org.mapstruct.InheritInverseConfiguration;
import org.mapstruct.Mapper;

import com.thyng.model.dto.ThingDTO;
import com.thyng.model.entity.Thing;

@Mapper(componentModel="spring")
public interface ThingMapper {

	ThingDTO toDTO(Thing thing);
	
	@InheritInverseConfiguration
	Thing toEntity(ThingDTO dto);
	
}
