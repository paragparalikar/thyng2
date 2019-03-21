package com.thyng.service;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.transaction.Transactional;

import org.springframework.stereotype.Service;

import com.thyng.model.entity.Metric;
import com.thyng.model.entity.Template;
import com.thyng.model.exception.TemplateNotFoundException;
import com.thyng.repository.data.jpa.MetricRepository;
import com.thyng.repository.data.jpa.TemplateRepository;

import lombok.RequiredArgsConstructor;

@Service
@Transactional
@RequiredArgsConstructor
public class TemplateService {

	private final MetricRepository metricRepository;
	private final TemplateRepository templateRepository;
	
	public List<Template> findAll(){
		return templateRepository.findAll();
	}
	
	public Template findById(Long id){
		return templateRepository.findById(id).orElseThrow(() -> new TemplateNotFoundException());
	}
	
	@Transactional
	public Template save(Template template){
		final Set<Metric> metrics = new HashSet<>(template.getMetrics());
		template.setMetrics(null);
		final Template managedTemplate = templateRepository.save(template);
		managedTemplate.getMetrics().clear();
		for(Metric metric : metrics){
			metric.setTemplate(managedTemplate);
			final Metric managedMetric = metricRepository.save(metric);
			managedTemplate.getMetrics().add(managedMetric);
		}
		return managedTemplate;
	}
	
	public void deleteById(Long id){
		templateRepository.deleteById(id);
	}
	
	public boolean existsByIdNotAndNameIgnoreCase(Long id, String name){
		return templateRepository.existsByIdNotAndNameIgnoreCase(id, name);
	}

}
