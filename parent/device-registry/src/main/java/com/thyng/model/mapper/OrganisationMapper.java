package com.thyng.model.mapper;

import org.mapstruct.InheritInverseConfiguration;
import org.mapstruct.Mapper;

import com.thyng.model.dto.OrganisationDTO;
import com.thyng.model.entity.Organisation;

@Mapper(componentModel="spring")
public interface OrganisationMapper {

	OrganisationDTO toDTO(Organisation organisation);
	
	@InheritInverseConfiguration
	Organisation toEntity(OrganisationDTO dto);
}
