package com.thyng.tenant;

import java.util.List;

import javax.transaction.Transactional;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import com.thyng.aspect.persistence.NotFoundException;
import com.thyng.thing.ThingService;
import com.thyng.user.UserService;

import lombok.RequiredArgsConstructor;

@Service
@Validated
@Transactional
@RequiredArgsConstructor
public class TenantService {

	private final UserService userService;
	private final ThingService thingService;
	private final TenantRepository tenantRepository;
	
	@PreAuthorize("hasPermission(null, 'TENANT', 'LIST')")
	public List<Tenant> findAll(){
		return tenantRepository.findAll();
	}

	@PreAuthorize("hasPermission(#id, 'TENANT', 'VIEW')")
	public Tenant findById(@NotNull @Positive Long id){
		return tenantRepository.findById(id).orElseThrow(() -> new NotFoundException());
	}
	
	@PreAuthorize("hasPermission(#tenant.id, 'TENANT', 'CREATE')")
	public Tenant create(@NotNull @Valid Tenant tenant){
		if(null != tenant.getId() && 0 < tenant.getId()) throw new IllegalArgumentException("Id must be null");
		if(null != tenant.getId() && 0 >= tenant.getId()) tenant.setId(null);
		return tenantRepository.save(tenant);
	}
	
	@PreAuthorize("hasPermission(#tenant.id, 'TENANT', 'UPDATE')")
	public Tenant update(@NotNull @Valid Tenant tenant){
		if(null == tenant.getId() || 0 >= tenant.getId()) throw new IllegalArgumentException("Id must not be null");
		return tenantRepository.save(tenant);
	}
	
	@Transactional
	@PreAuthorize("hasPermission(#id, 'TENANT', 'DELETE')")
	public void deleteById(@NotNull @Positive Long id){
		userService.deleteByTenantId(id);
		thingService.deleteByTenantId(id);
		tenantRepository.deleteById(id);
	}
	
	public boolean existsByIdNotAndName(Long id, String name){
		return tenantRepository.existsByIdNotAndName(id, name);
	}
	
}
