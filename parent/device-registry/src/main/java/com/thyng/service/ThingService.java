package com.thyng.service;

import java.util.List;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import com.thyng.model.entity.Thing;
import com.thyng.model.exception.NotFoundException;
import com.thyng.repository.data.jpa.ThingRepository;

import lombok.RequiredArgsConstructor;

@Service
@Validated
@RequiredArgsConstructor
public class ThingService {

	private final ThingRepository thingRepository;
	
	@PreAuthorize("hasPermission(null, 'THING', 'LIST')")
	public List<Thing> findByTenantId(@NotNull @Positive Long tenantId){
		return thingRepository.findByTenantId(tenantId);
	}
	
	@PreAuthorize("hasPermission(#id, 'THING', 'VIEW')")
	public Thing findById(@NotNull @Positive Long id){
		final Thing thing = thingRepository.findById(id).orElseThrow(() -> new NotFoundException());
		thing.getProperties();
		thing.getMetrics();
		thing.getTenant();
		thing.getTags();
		return thing;
	}
	
	public boolean existsByIdAndTenantId(@NotNull Long id, @NotNull Long tenantId){
		return thingRepository.existsByIdAndTenantId(id, tenantId);
	}
	
	@PreAuthorize("hasPermission(#thing.id, 'THING', 'CREATE')")
	public Thing create(@NotNull Thing thing){
		assert null == thing.getId() : "Id must be null";
		return thingRepository.save(thing);
	}
	
	@PreAuthorize("hasPermission(#thing.id, 'THING', 'UPDATE')")
	public Thing update(@NotNull Thing thing){
		assert null != thing.getId() : "Id must not be null";
		return thingRepository.save(thing);
	}
	
	@PreAuthorize("hasPermission(#id, 'THING', 'DELETE')")
	public void deleteById(@NotNull @Positive Long id){
		thingRepository.deleteById(id);
	}
	
	@PreAuthorize("hasPermission(#tenantId, 'TENANT', 'DELETE')")
	public void deleteByTenantId(Long tenantId){
		thingRepository.deleteByTenantId(tenantId);
	}
}
