package com.thyng.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.thyng.model.entity.Tenant;
import com.thyng.model.exception.NotFoundException;
import com.thyng.repository.data.jpa.TenantRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class TenantService {

	private final TenantRepository tenantRepository;
	
	public List<Tenant> findAll(){
		return tenantRepository.findAll();
	}

	public Tenant findById(Long id){
		return tenantRepository.findById(id).orElseThrow(() -> new NotFoundException());
	}
	
	public Tenant save(Tenant tenant){
		return tenantRepository.saveAndFlush(tenant);
	}
	
	public void deleteById(Long id){
		tenantRepository.deleteById(id);
	}
	
	public boolean existsByIdNotAndName(Long id, String name){
		return tenantRepository.existsByIdNotAndName(id, name);
	}
	
}
