package com.thyng.model.mapper;

import java.util.Collection;
import java.util.Set;

import org.mapstruct.IterableMapping;
import org.mapstruct.Mapper;

import com.thyng.model.dto.GatewayDTO;
import com.thyng.model.dto.GatewayDetailsDTO;
import com.thyng.model.dto.GatewayExtendedDetailsDTO;
import com.thyng.model.entity.Gateway;

@Mapper(componentModel="spring", uses={ThingMapper.class})
public interface GatewayMapper {

	GatewayExtendedDetailsDTO toExtendedDTO(Gateway gateway);
	
	GatewayDetailsDTO toDetailsDTO(Gateway gateway);
	
	@IterableMapping(elementTargetType = GatewayDTO.class)
	Set<GatewayDTO> dto(Collection<? extends Gateway> gateways);
	
	Gateway entity(GatewayDetailsDTO dto);
	
}
