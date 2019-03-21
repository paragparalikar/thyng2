package com.thyng.web;

import java.util.Collection;
import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.thyng.model.dto.TemplateDTO;
import com.thyng.model.entity.Template;
import com.thyng.model.mapper.TemplateMapper;
import com.thyng.service.TemplateService;

@RestController
@RequestMapping("/api/v1/templates")
public class TemplateController {
	private static final String MSG_UNIQUE_NAME = "Another template already exists with this name";
	
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
	
	@PostMapping
	@ResponseBody
	public TemplateDTO save(@RequestBody @Valid TemplateDTO dto){
		if(templateService.existsByIdNotAndNameIgnoreCase(null == dto.getId() ? 0 : dto.getId(), dto.getName())){
			throw new IllegalArgumentException(MSG_UNIQUE_NAME);
		}
		final Template template = templateMapper.toEntity(dto);
		final Template managedTemplate = templateService.save(template);
		return templateMapper.toDTO(managedTemplate);
	}

	@DeleteMapping("/{id}")
	public void deleteById(@PathVariable Long id){
		templateService.deleteById(id);
	}
	
	@GetMapping(params={"id","name"})
	public String existsByIdNotAndNameIgnoreCase(@RequestParam(defaultValue="0") Long id, @RequestParam String name){
		return templateService.existsByIdNotAndNameIgnoreCase(id, name) ? "[ \""+MSG_UNIQUE_NAME+"\" ]" : Boolean.TRUE.toString();
	}
	
}
