package com.thyng.model.mapper;

import java.util.Collection;
import java.util.Set;

import org.mapstruct.InheritInverseConfiguration;
import org.mapstruct.Mapper;

import com.thyng.model.dto.TenantDTO;
import com.thyng.model.entity.Tenant;

@Mapper(componentModel="spring")
public interface TenantMapper {
	
	Set<TenantDTO> toDTO(Collection<? extends Tenant> tenants);

	TenantDTO toDTO(Tenant tenant);
	
	@InheritInverseConfiguration
	Tenant toEntity(TenantDTO dto);
}
 