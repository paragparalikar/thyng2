package com.thyng.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.thyng.model.entity.Template;
import com.thyng.model.exception.TemplateNotFoundException;
import com.thyng.repository.data.jpa.TemplateRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class TemplateService {

	private final TemplateRepository templateRepository;
	
	public List<Template> findAll(){
		return templateRepository.findAll();
	}
	
	public Template findById(Long id){
		return templateRepository.findById(id).orElseThrow(() -> new TemplateNotFoundException());
	}
	

}
