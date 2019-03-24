package com.thyng.model.mapper;

import org.mapstruct.InheritInverseConfiguration;
import org.mapstruct.Mapper;

import com.thyng.model.dto.UserDTO;
import com.thyng.model.entity.User;

@Mapper(componentModel = "spring", uses={TenantMapper.class})
public interface UserMapper {

	UserDTO toDTO(User user);

	@InheritInverseConfiguration
	User toEntity(UserDTO dto);
	
	

}
