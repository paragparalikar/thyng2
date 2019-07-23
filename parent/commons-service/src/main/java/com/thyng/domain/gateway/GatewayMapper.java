package com.thyng.domain.gateway;

import java.util.Collection;
import java.util.Set;

import org.mapstruct.IterableMapping;
import org.mapstruct.Mapper;

import com.thyng.domain.thing.ThingMapper;

@Mapper(componentModel="spring", uses={ThingMapper.class})
public interface GatewayMapper {

	GatewayConfigurationDTO toExtendedDTO(Gateway gateway);
	
	GatewayDetailsDTO toDetailsDTO(Gateway gateway);
	
	@IterableMapping(elementTargetType = GatewayDTO.class)
	Set<GatewayDTO> dto(Collection<? extends Gateway> gateways);
	
	@IterableMapping(elementTargetType = GatewayThinDTO.class)
	Set<GatewayThinDTO> thinDto(Collection<? extends Gateway> gateways);
	
	Gateway entity(GatewayDetailsDTO dto);
	
}
