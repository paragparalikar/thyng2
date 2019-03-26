package com.thyng.model.mapper;

import java.util.Collection;
import java.util.Set;

import org.mapstruct.Mapper;

import com.thyng.model.dto.ThingDTO;
import com.thyng.model.dto.ThingDetailsDTO;
import com.thyng.model.entity.Thing;

@Mapper(componentModel="spring")
public interface ThingMapper {

	ThingDetailsDTO toDTO(Thing thing);
	
	Set<ThingDTO> toDTO(Collection<Thing> things);
	
	Thing toEntity(ThingDetailsDTO dto);
	
}
