package com.thyng.model.mapper;

import org.mapstruct.InheritInverseConfiguration;
import org.mapstruct.Mapper;

import com.thyng.model.dto.EmailDTO;
import com.thyng.model.entity.Email;

@Mapper(componentModel="spring")
public interface EmailMapper {

	EmailDTO toDTO(Email email);
	
	@InheritInverseConfiguration
	Email toEntity(EmailDTO dto);
	
}
