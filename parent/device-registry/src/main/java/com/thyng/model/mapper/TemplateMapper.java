package com.thyng.model.mapper;

import java.util.Collection;
import java.util.List;

import org.mapstruct.InheritInverseConfiguration;
import org.mapstruct.Mapper;

import com.thyng.model.dto.TemplateDTO;
import com.thyng.model.entity.Template;

@Mapper(componentModel="spring")
public interface TemplateMapper {

	TemplateDTO toDTO(Template template);
	
	List<TemplateDTO> toDTO(Collection<Template> templates);
	
	@InheritInverseConfiguration
	Template toEntity(TemplateDTO dto);
	
}
