package com.thyng.thing;

import java.util.List;
import java.util.Set;

import javax.transaction.Transactional;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import com.thyng.aspect.persistence.NotFoundException;
import com.thyng.gateway.Gateway;
import com.thyng.gateway.GatewayService;

import lombok.RequiredArgsConstructor;

@Service
@Validated
@Transactional
@RequiredArgsConstructor
public class ThingService {

	private final ThingRepository thingRepository;
	private final GatewayService gatewayService;
	
	@PreAuthorize("hasPermission(null, 'THING', 'LIST')")
	public List<Thing> findByTenantId(@NotNull @Positive Long tenantId){
		return thingRepository.findByTenantId(tenantId);
	}
	
	@PreAuthorize("hasPermission(null, 'THING', 'LIST')")
	public Set<Thing> findByGatewayId(@NotNull @Positive Long gatewayId){
		return thingRepository.findByGatewayId(gatewayId);
	}
	
	@PreAuthorize("hasPermission(#id, 'THING', 'VIEW')")
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
	
	@PreAuthorize("hasPermission(#thing.id, 'THING', 'CREATE')")
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
	@PreAuthorize("hasPermission(#thing.id, 'THING', 'UPDATE')")
	public Thing update(@NotNull Thing thing){
		if(null == thing.getId() || 0 >= thing.getId()) throw new IllegalArgumentException("Id must not be null");
		final Thing managedThing = findById(thing.getId());
		thing.setProperties(managedThing.getProperties());
		thing.setSensors(managedThing.getSensors());
		thing.setActuators(managedThing.getActuators());
		return thingRepository.save(thing);
	}
	
	@Transactional
	@PreAuthorize("hasPermission(#id, 'THING', 'DELETE')")
	public void deleteById(@NotNull @Positive Long id){
		thingRepository.deleteById(id);
	}
	
	@Transactional
	@PreAuthorize("hasPermission(#tenantId, 'TENANT', 'DELETE')")
	public void deleteByTenantId(Long tenantId){
		thingRepository.deleteByTenantId(tenantId);
	}
}
