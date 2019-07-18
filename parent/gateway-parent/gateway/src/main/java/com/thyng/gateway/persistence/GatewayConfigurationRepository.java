package com.thyng.gateway.persistence;

import java.util.Optional;

import com.thyng.model.dto.GatewayConfigurationDTO;

public interface GatewayConfigurationRepository {

	GatewayConfigurationDTO save(GatewayConfigurationDTO dto) throws Exception;

	Optional<GatewayConfigurationDTO> load() throws Exception;
	
}
