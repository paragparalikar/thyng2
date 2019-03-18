package com.thyng.service;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.stereotype.Service;

import com.thyng.model.entity.Metric;
import com.thyng.repository.data.jpa.MetricRepository;

import lombok.RequiredArgsConstructor;

@Service
@Transactional
@RequiredArgsConstructor
public class MetricService {

	private final MetricRepository metricRepository;
	
	public List<Metric> findByTemplateId(Long templateId){
		return metricRepository.findByTemplateId(templateId);
	}
	
	@Transactional
	public void deleteById(Long id){
		metricRepository.findById(id).ifPresent(metric -> {
			metric.getTemplate().getMetrics().remove(metric);
		});
	}

}
