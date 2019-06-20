package com.thyng.tenant;

import java.util.Collection;
import java.util.Set;

import org.mapstruct.InheritInverseConfiguration;
import org.mapstruct.Mapper;

@Mapper(componentModel="spring")
public interface TenantMapper {
	
	Set<TenantDTO> toDTO(Collection<? extends Tenant> tenants);

	TenantDTO toDTO(Tenant tenant);
	
	@InheritInverseConfiguration
	Tenant toEntity(TenantDTO dto);
}
 