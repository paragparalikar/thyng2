package com.thyng.model.mapper;

import java.util.Collection;
import java.util.Set;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import com.thyng.model.dto.ThingDTO;
import com.thyng.model.entity.Thing;

@Mapper(componentModel="spring")
public interface ThingMapper {

	@Mapping(source="template.id", target="templateId")
	@Mapping(source="template.name", target="templateName")
	ThingDTO toDTO(Thing thing);
	
	Set<ThingDTO> toDTO(Collection<Thing> things);
	
	Thing toEntity(ThingDTO dto);
	
}
