package com.thyng.service;

import java.util.List;
import java.util.Set;

import javax.transaction.Transactional;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;

import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import com.thyng.entity.Gateway;
import com.thyng.entity.Thing;
import com.thyng.model.exception.NotFoundException;
import com.thyng.repository.spring.data.jpa.ThingRepository;

import lombok.RequiredArgsConstructor;

@Service
@Validated
@Transactional
@RequiredArgsConstructor
public class ThingService {

	private final ThingRepository thingRepository;
	private final GatewayService gatewayService;
	
	public List<Thing> findByTenantId(@NotNull @Positive Long tenantId){
		return thingRepository.findByTenantId(tenantId);
	}
	
	public Set<Thing> findByGatewayId(@NotNull @Positive Long gatewayId){
		return thingRepository.findByGatewayId(gatewayId);
	}
	
	public Thing findById(@NotNull @Positive Long id){
		final Thing thing = thingRepository.findById(id).orElseThrow(() -> new NotFoundException());
		thing.getProperties();
		thing.getSensors();
		thing.getActuators();
		return thing;
	}
	
	public boolean existsByIdAndTenantId(@NotNull Long id, @NotNull Long tenantId){
		return thingRepository.existsByIdAndTenantId(id, tenantId);
	}
	
	public boolean existsByIdNotAndNameAndTenantId(Long id, String name, Long tenantId){
		return thingRepository.existsByIdNotAndNameAndTenantId(id, name, tenantId);
	}
	
	public Thing create(@NotNull Thing thing){
		if(null == thing.getId() || 0 >= thing.getId()){
			if(null != thing.getId() && 0 == thing.getId()) thing.setId(null);
			final Gateway managedGateway = gatewayService.findById(thing.getGateway().getId());
			thing.setGateway(managedGateway);
			return thingRepository.save(thing);
		}
		throw new IllegalArgumentException("Id must be null");
	}
	
	@Transactional
	public Thing update(@NotNull Thing thing){
		if(null == thing.getId() || 0 >= thing.getId()) throw new IllegalArgumentException("Id must not be null");
		final Thing managedThing = findById(thing.getId());
		thing.setProperties(managedThing.getProperties());
		thing.setSensors(managedThing.getSensors());
		thing.setActuators(managedThing.getActuators());
		return thingRepository.save(thing);
	}
	
	@Transactional
	public void deleteById(@NotNull @Positive Long id){
		thingRepository.deleteById(id);
	}
	
	@Transactional
	public void deleteByTenantId(Long tenantId){
		thingRepository.deleteByTenantId(tenantId);
	}
}
