package com.thyng.service;

import java.util.List;

import javax.transaction.Transactional;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;

import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import com.thyng.model.entity.Gateway;
import com.thyng.model.exception.NotFoundException;
import com.thyng.repository.data.jpa.GatewayRepository;

import lombok.RequiredArgsConstructor;

@Service
@Validated
@Transactional
@RequiredArgsConstructor
public class GatewayService {

	private final GatewayRepository gatewayRepository;
	
	@Transactional
	public Gateway register(@NotNull @Positive Long id, 
			@NotBlank String host, @NotNull @Positive Integer port){
		final Gateway gateway = gatewayRepository.findById(id)
				.orElseThrow(() -> new NotFoundException());
		gateway.setHost(host);
		gateway.setPort(port);
		if(null != gateway.getMqttClientConfig()){
			gateway.getMqttClientConfig().getHost();
			gateway.getMqttClientConfig().getLastWill();
		}
		final Gateway managedGateway = gatewayRepository.save(gateway);
		return managedGateway.clone();
	}
	
	public List<Gateway> findByTenantId(Long tenantId){
		return gatewayRepository.findByTenantId(tenantId);
	}
	
	public Gateway findById(@NotNull @Positive Long id){
		final Gateway gateway = gatewayRepository.findById(id)
				.orElseThrow(() -> new NotFoundException());
		gateway.getProperties();
		gateway.getMqttClientConfig();
		return gateway;
	}
	
	public Gateway findByIdIncludeThings(@NotNull @Positive Long id){
		final Gateway gateway = gatewayRepository.findById(id)
				.orElseThrow(() -> new NotFoundException());
		gateway.getThings();
		gateway.getProperties();
		gateway.getMqttClientConfig();
		return gateway;
	}

}
