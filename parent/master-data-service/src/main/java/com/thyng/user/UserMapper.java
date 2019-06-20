package com.thyng.user;

import org.mapstruct.InheritInverseConfiguration;
import org.mapstruct.Mapper;

import com.thyng.tenant.TenantMapper;

@Mapper(componentModel = "spring", uses={TenantMapper.class})
public interface UserMapper {

	UserDTO toDTO(User user);

	@InheritInverseConfiguration
	User toEntity(UserDTO dto);
	
	

}
