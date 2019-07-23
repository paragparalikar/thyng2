package com.thyng.domain.user;

import org.mapstruct.InheritInverseConfiguration;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import com.thyng.domain.tenant.TenantMapper;

@Mapper(componentModel = "spring", uses={TenantMapper.class})
public interface UserMapper {

	@Mapping(source = "name.prefix", target="prefix")
	@Mapping(source = "name.givenName", target="givenName")
	@Mapping(source = "name.middleName", target="middleName")
	@Mapping(source = "name.familyName", target="familyName")
	@Mapping(source = "name.suffix", target="suffix")
	@Mapping(source = "thyngAuthorities", target="authorities")
	UserDTO toDTO(User user);

	@InheritInverseConfiguration
	User toEntity(UserDTO dto);
	
}
