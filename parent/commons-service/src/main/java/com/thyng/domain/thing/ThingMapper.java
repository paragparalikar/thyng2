package com.thyng.domain.thing;

import java.util.Collection;
import java.util.Set;

import org.mapstruct.DecoratedWith;
import org.mapstruct.InheritInverseConfiguration;
import org.mapstruct.IterableMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import com.thyng.domain.sensor.SensorMapper;

@DecoratedWith(ThingMapperDecorator.class)
@Mapper(componentModel="spring", uses={SensorMapper.class})
public interface ThingMapper {

	@Mapping(target="gatewayId", source="gateway.id")
	@Mapping(target="gatewayName", source="gateway.name")
	ThingDetailsDTO dto(Thing thing);
	
	@Mapping(target="gatewayId", source="gateway.id")
	@Mapping(target="gatewayName", source="gateway.name")
	ThingDTO thinDto(Thing thing);
	
	@IterableMapping(elementTargetType = ThingDTO.class)
	Set<ThingDTO> dto(Collection<Thing> things);
	
	@InheritInverseConfiguration(name="dto")
	Thing entity(ThingDetailsDTO dto);
	
}
