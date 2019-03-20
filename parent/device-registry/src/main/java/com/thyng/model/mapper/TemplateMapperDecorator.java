package com.thyng.model.mapper;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

import com.thyng.model.dto.TemplateDTO;
import com.thyng.model.entity.Template;

public abstract class TemplateMapperDecorator implements TemplateMapper {

	@Autowired
    @Qualifier("delegate")
	private TemplateMapper delegate;
		
	@Override
	public Template toEntity(TemplateDTO dto) {
		final Template template = delegate.toEntity(dto);
		if(null != template.getId() && 0 >= template.getId()){
			template.setId(null);
		}
		return template;
	}
	
}
