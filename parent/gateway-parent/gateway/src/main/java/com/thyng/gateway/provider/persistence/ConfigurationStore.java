package com.thyng.gateway.provider.persistence;

import com.thyng.model.dto.GatewayConfigurationDTO;

public interface ConfigurationStore {

	GatewayConfigurationDTO save(GatewayConfigurationDTO dto) throws Exception;

	GatewayConfigurationDTO load() throws Exception;
	
}
