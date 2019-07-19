package com.thyng.metrics.endpoint;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.thyng.model.Metrics;
import com.thyng.service.MetricsService;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
public class HttpMetricsEndpoint {

	private final MetricsService metricsService;
	
	@PostMapping("/metrics")
	public void create(@RequestBody Metrics metrics) {
		metricsService.save(metrics);
	}

}
