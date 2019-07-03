package com.thyng.mapper;

import java.util.Collection;
import java.util.Set;

import org.mapstruct.InheritInverseConfiguration;
import org.mapstruct.Mapper;

import com.thyng.entity.Tenant;
import com.thyng.model.dto.TenantDTO;

@Mapper(componentModel="spring")
public interface TenantMapper {
	
	Set<TenantDTO> toDTO(Collection<? extends Tenant> tenants);

	TenantDTO toDTO(Tenant tenant);
	
	@InheritInverseConfiguration
	Tenant toEntity(TenantDTO dto);
}
 