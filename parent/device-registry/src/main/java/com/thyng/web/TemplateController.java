package com.thyng.web;

import java.util.Collection;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.thyng.model.dto.TemplateDTO;
import com.thyng.model.entity.Template;
import com.thyng.model.mapper.TemplateMapper;
import com.thyng.service.TemplateService;

@RestController
@RequestMapping("/templates")
public class TemplateController {
	
	@Autowired
	private TemplateMapper templateMapper;
	
	@Autowired
	private TemplateService templateService;

	@GetMapping
	public Collection<TemplateDTO> findAll(){
		final List<Template> templates = templateService.findAll();
		return templateMapper.toDTO(templates);
	}
	
	@GetMapping("/{id}")
	public TemplateDTO findById(@PathVariable Long id){
		final Template template = templateService.findById(id);
		return templateMapper.toDTO(template);
	}

}
