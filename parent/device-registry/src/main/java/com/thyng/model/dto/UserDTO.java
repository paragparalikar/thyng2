package com.thyng.model.dto;

import java.util.Set;

import com.thyng.model.entity.Name;

import lombok.Data;

@Data 
public class UserDTO {

	private Long id;
	
	private Name name;
	
	private OrganisationDTO organisation;
	
	private AddressDTO address;
	
	private Set<EmailDTO> emails;
	
	private Set<PhoneDTO> phones;

}
