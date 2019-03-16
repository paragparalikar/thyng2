package com.thyng.web;

import java.util.List;
import java.util.Set;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.thyng.model.dto.MetricDTO;
import com.thyng.model.entity.Metric;
import com.thyng.model.mapper.MetricMapper;
import com.thyng.service.MetricService;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/templates/{templateId}/metrics")
public class MetricController {

	private final MetricMapper metricMapper;
	private final MetricService metricService;
	
	@GetMapping
	public Set<MetricDTO> findByTemplateId(@PathVariable Long templateId){
		final List<Metric> metrics = metricService.findByTemplateId(templateId);
		return metricMapper.toDTO(metrics);
	}
}
