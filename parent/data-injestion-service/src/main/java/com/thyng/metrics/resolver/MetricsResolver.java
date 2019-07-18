package com.thyng.metrics.resolver;

import com.thyng.entity.MetricsSchema;
import com.thyng.model.Metrics;

public interface MetricsResolver {

	void resolve(Metrics metrics, MetricsSchema schema);
	
}
