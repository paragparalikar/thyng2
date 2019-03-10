package com.thyng.model.mapper;

import org.mapstruct.InheritInverseConfiguration;
import org.mapstruct.Mapper;

import com.thyng.model.dto.PhoneDTO;
import com.thyng.model.entity.Phone;

@Mapper(componentModel="spring")
public interface PhoneMapper {

	PhoneDTO toDTO(Phone phone);
	
	@InheritInverseConfiguration
	Phone toEntity(PhoneDTO dto);
	
}
