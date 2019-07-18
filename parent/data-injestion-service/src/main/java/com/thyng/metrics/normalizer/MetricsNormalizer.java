package com.thyng.metrics.normalizer;

import com.thyng.model.Metrics;
import com.thyng.model.dto.SensorDTO;
import com.thyng.model.dto.ThingDetailsDTO;

public interface MetricsNormalizer {

	void normalize(Metrics metrics, SensorDTO sensorDTO, ThingDetailsDTO thingDTO);
	
}
