package com.thyng.gateway.provider.persistence;

import java.util.Optional;

import com.thyng.model.dto.GatewayConfigurationDTO;

public interface ConfigurationStore {

	GatewayConfigurationDTO save(GatewayConfigurationDTO dto) throws Exception;

	Optional<GatewayConfigurationDTO> load() throws Exception;
	
}
