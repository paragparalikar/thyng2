package com.thyng.model.mapper;

import java.util.Collection;
import java.util.Set;

import org.mapstruct.Mapper;

import com.thyng.model.dto.GatewayDTO;
import com.thyng.model.dto.GatewayDetailsDTO;
import com.thyng.model.entity.Gateway;

@Mapper(componentModel="spring")
public interface GatewayMapper {

	GatewayDetailsDTO dto(Gateway gateway);
	
	Set<GatewayDTO> dto(Collection<? extends Gateway> gateways);
	
	Gateway entity(GatewayDetailsDTO dto);
	
}
