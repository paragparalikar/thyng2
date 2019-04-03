package com.thyng.model.mapper;

import java.util.Collection;
import java.util.Set;

import org.mapstruct.DecoratedWith;
import org.mapstruct.InheritInverseConfiguration;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import com.thyng.model.dto.ThingDTO;
import com.thyng.model.dto.ThingDetailsDTO;
import com.thyng.model.entity.Thing;

@DecoratedWith(ThingMapperDecorator.class)
@Mapper(componentModel="spring", uses={SensorMapper.class})
public interface ThingMapper {

	@Mapping(target="gatewayId", source="gateway.id")
	@Mapping(target="gatewayName", source="gateway.name")
	ThingDetailsDTO toDTO(Thing thing);
	
	Set<ThingDTO> toDTO(Collection<Thing> things);
	
	@InheritInverseConfiguration
	Thing toEntity(ThingDetailsDTO dto);
	
}