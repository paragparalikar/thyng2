package com.thyng.gateway.provider.details;

import com.thyng.gateway.provider.client.ThyngClient;
import com.thyng.gateway.provider.persistence.PersistenceProvider;
import com.thyng.model.dto.GatewayExtendedDetailsDTO;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
public class DetailsProvider{

	private final ThyngClient client;
	private final PersistenceProvider persistenceProvider;
		
	public GatewayExtendedDetailsDTO get() throws Exception{
		try{
			return persistenceProvider.load();
		}catch(Exception e){
			log.error("Could not load details from persistence provider, fetching from server");
			return persistenceProvider.save(client.registerAndFetchDetails());
		}
	}
		
}