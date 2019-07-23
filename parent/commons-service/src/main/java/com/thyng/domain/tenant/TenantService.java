package com.thyng.domain.tenant;

import java.util.List;

import javax.transaction.Transactional;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;

import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import com.thyng.configuration.persistence.NotFoundException;
import com.thyng.domain.thing.ThingService;
import com.thyng.domain.user.UserService;

import lombok.RequiredArgsConstructor;

@Service
@Validated
@Transactional
@RequiredArgsConstructor
public class TenantService {

	private final UserService userService;
	private final ThingService thingService;
	private final TenantRepository tenantRepository;
	
	public List<Tenant> findAll(){
		return tenantRepository.findAll();
	}

	public Tenant findById(@NotNull @Positive Long id){
		return tenantRepository.findById(id).orElseThrow(() -> new NotFoundException());
	}
	
	public Tenant create(@NotNull @Valid Tenant tenant){
		if(null != tenant.getId() && 0 < tenant.getId()) throw new IllegalArgumentException("Id must be null");
		if(null != tenant.getId() && 0 >= tenant.getId()) tenant.setId(null);
		return tenantRepository.save(tenant);
	}
	
	public Tenant update(@NotNull @Valid Tenant tenant){
		if(null == tenant.getId() || 0 >= tenant.getId()) throw new IllegalArgumentException("Id must not be null");
		final Tenant managedTenant = findById(tenant.getId());
		tenant.setGateways(managedTenant.getGateways());
		return tenantRepository.save(tenant);
	}
	
	@Transactional
	public void deleteById(@NotNull @Positive Long id){
		userService.deleteByTenantId(id);
		thingService.deleteByTenantId(id);
		tenantRepository.deleteById(id);
	}
	
	public boolean existsByIdNotAndName(Long id, String name){
		return tenantRepository.existsByIdNotAndName(id, name);
	}
	
}
