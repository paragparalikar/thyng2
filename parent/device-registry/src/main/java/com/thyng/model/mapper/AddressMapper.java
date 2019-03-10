package com.thyng.model.mapper;

import org.mapstruct.InheritInverseConfiguration;
import org.mapstruct.Mapper;

import com.thyng.model.dto.AddressDTO;
import com.thyng.model.entity.Address;

@Mapper(componentModel="spring")
public interface AddressMapper {

	AddressDTO toDTO(Address address);
	
	@InheritInverseConfiguration
	Address toEntity(AddressDTO dto);
}
