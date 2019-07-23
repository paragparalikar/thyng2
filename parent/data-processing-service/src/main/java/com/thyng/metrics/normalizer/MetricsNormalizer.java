package com.thyng.metrics.normalizer;

import com.thyng.domain.metrics.Metrics;
import com.thyng.domain.sensor.SensorDTO;
import com.thyng.domain.thing.ThingDetailsDTO;

public interface MetricsNormalizer {

	void normalize(Metrics metrics, SensorDTO sensorDTO, ThingDetailsDTO thingDTO);
	
}
