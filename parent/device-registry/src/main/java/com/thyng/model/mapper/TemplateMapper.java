package com.thyng.model.mapper;

import java.util.Collection;
import java.util.List;

import org.mapstruct.DecoratedWith;
import org.mapstruct.Mapper;

import com.thyng.model.dto.TemplateDTO;
import com.thyng.model.entity.Template;

@DecoratedWith(TemplateMapperDecorator.class)
@Mapper(componentModel="spring", uses=MetricMapper.class)
public interface TemplateMapper {

	TemplateDTO toDTO(Template template);
	
	List<TemplateDTO> toDTO(Collection<Template> templates);
	
	Template toEntity(TemplateDTO dto);
	
}
