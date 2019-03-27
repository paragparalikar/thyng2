package com.thyng.service;

import java.util.List;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import com.thyng.model.entity.Tenant;
import com.thyng.model.exception.NotFoundException;
import com.thyng.repository.data.jpa.TenantRepository;

import lombok.RequiredArgsConstructor;

@Service
@Validated
@RequiredArgsConstructor
public class TenantService {

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
		assert null == tenant.getId() : "Id must be null";
		return tenantRepository.save(tenant);
	}
	
	@PreAuthorize("hasPermission(#tenant.id, 'TENANT', 'UPDATE')")
	public Tenant update(@NotNull @Valid Tenant tenant){
		assert null != tenant.getId() : "Id must not be null";
		return tenantRepository.save(tenant);
	}
	
	@PreAuthorize("hasPermission(#id, 'TENANT', 'DELETE')")
	public void deleteById(@NotNull @Positive Long id){
		tenantRepository.deleteById(id);
	}
	
	public boolean existsByIdNotAndName(Long id, String name){
		return tenantRepository.existsByIdNotAndName(id, name);
	}
	
}
