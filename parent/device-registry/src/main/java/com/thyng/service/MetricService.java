package com.thyng.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.thyng.model.entity.Metric;
import com.thyng.repository.data.jpa.MetricRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class MetricService {

	private final MetricRepository metricRepository;
	
	public List<Metric> findByTemplateId(Long templateId){
		return metricRepository.findByTemplateId(templateId);
	}

}
