package com.thyng.gateway;

import java.util.List;

import javax.transaction.Transactional;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import com.thyng.aspect.persistence.NotFoundException;

import lombok.RequiredArgsConstructor;

@Service
@Validated
@Transactional
@RequiredArgsConstructor
public class GatewayService { 

	private final GatewayRepository gatewayRepository;
	
	@PreAuthorize("hasPermission(null, 'GATEWAY', 'LIST')")
	public List<Gateway> findByTenantId(@NotNull @Positive Long tenantId){
		return gatewayRepository.findByTenantId(tenantId);
	}
	
	public List<Gateway> findThinByTenantId(@NotNull @Positive Long tenantId){
		return gatewayRepository.findThinByTenantId(tenantId);
	}
	
	public Boolean existsByIdAndTenantId(@NotNull @Positive Long id, 
			@NotNull @Positive Long tenantId){
		return gatewayRepository.existsByIdAndTenantId(id, tenantId);
	}
	
	public Boolean existsByIdNotAndNameAndTenantId(Long id, String name, @NotNull @Positive Long tenantId){
		return gatewayRepository.existsByIdNotAndNameAndTenantId(id, name, tenantId);
	}
	
	@PreAuthorize("hasPermission(null, 'GATEWAY', 'CREATE')")
	public Gateway create(Gateway gateway){
		if(null == gateway.getId() || 0 >= gateway.getId()){
			gateway.setId(null);
			return gatewayRepository.save(gateway);
		}
		throw new IllegalArgumentException("Id must be null");
	}
	
	@PreAuthorize("hasPermission(#gateway.id, 'GATEWAY', 'UPDATE')")
	public Gateway update(Gateway gateway){
		if(null == gateway.getId() || 0 >= gateway.getId()){
			throw new IllegalArgumentException("Id must not be null");
		}
		final Gateway managedGateway = findByIdIncludeThings(gateway.getId());
		gateway.setThings(managedGateway.getThings());
		gateway.setMqttClientConfig(managedGateway.getMqttClientConfig());
		return gatewayRepository.save(gateway);
	}
	
	@PreAuthorize("hasPermission(#id, 'GATEWAY', 'DELETE')")
	public void deleteById(Long id){
		gatewayRepository.deleteById(id);
	}
	
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
	
	
	@PreAuthorize("hasPermission(#id, 'GATEWAY', 'VIEW')")
	public Gateway findById(@NotNull @Positive Long id){
		final Gateway gateway = gatewayRepository.findById(id)
				.orElseThrow(() -> new NotFoundException());
		gateway.getProperties();
		gateway.getMqttClientConfig();
		return gateway;
	}
	
	@PreAuthorize("hasPermission(#id, 'GATEWAY', 'VIEW')")
	public Gateway findByIdIncludeThings(@NotNull @Positive Long id){
		final Gateway gateway = gatewayRepository.findById(id)
				.orElseThrow(() -> new NotFoundException());
		gateway.getThings();
		gateway.getProperties();
		gateway.getMqttClientConfig();
		return gateway;
	}

}
