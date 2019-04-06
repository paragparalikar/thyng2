package com.thyng.service;

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
		gateway.getThings();
		gateway.setHost(host);
		gateway.setPort(port);
		return gatewayRepository.save(gateway);
	}
	
	public Gateway findById(@NotNull @Positive Long id){
		final Gateway gateway = gatewayRepository.findById(id)
				.orElseThrow(() -> new NotFoundException());
		gateway.getTags();
		gateway.getProperties();
		gateway.getMqttClientConfig();
		return gateway;
	}
	
	public Gateway findByIdIncludeThings(@NotNull @Positive Long id){
		final Gateway gateway = gatewayRepository.findById(id)
				.orElseThrow(() -> new NotFoundException());
		gateway.getTags();
		gateway.getThings();
		gateway.getProperties();
		gateway.getMqttClientConfig();
		return gateway;
	}

}
