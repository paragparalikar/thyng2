package com.thyng.tenant;

import java.util.List;
import java.util.Set;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/tenants")
public class TenantController {

	private final TenantMapper tenantMapper;
	
	private final TenantService tenantService;
	
	@GetMapping
	public Set<TenantDTO> findAll(){
		final List<Tenant> tenants = tenantService.findAll();
		return tenantMapper.toDTO(tenants);
	}
	
	@GetMapping("/{id}")
	public TenantDTO findById(@PathVariable @NotNull Long id){
		final Tenant tenant = tenantService.findById(id);
		return tenantMapper.toDTO(tenant);
	}
	
	@PostMapping
	@ResponseBody
	public TenantDTO create(@RequestBody @NotNull @Valid TenantDTO dto){
		assert null == dto.getId() : "Id must be null";
		final Tenant tenant = tenantMapper.toEntity(dto);
		final Tenant managedTenant = tenantService.create(tenant);
		return tenantMapper.toDTO(managedTenant);
	}
	
	@PutMapping
	@ResponseBody
	public TenantDTO update(@RequestBody @NotNull @Valid TenantDTO dto){
		assert null != dto.getId() : "Id must not be null";
		final Tenant tenant = tenantMapper.toEntity(dto);
		final Tenant managedTenant = tenantService.update(tenant);
		return tenantMapper.toDTO(managedTenant);
	}
	
	@DeleteMapping("/{id}")
	public void deleteById(@PathVariable @NotNull Long id){
		tenantService.deleteById(id);
	}
	
	@GetMapping(params={"id", "name"})
	public String existsByName(@RequestParam(defaultValue="0") Long id, @RequestParam String name){
		return tenantService.existsByIdNotAndName(id, name) ? "[\"This name is already taken\"]" : Boolean.TRUE.toString();
	}

}
