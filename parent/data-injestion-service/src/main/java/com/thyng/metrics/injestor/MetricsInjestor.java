package com.thyng.metrics.injestor;

import com.thyng.entity.Sensor;
import com.thyng.mapper.SensorMapper;
import com.thyng.mapper.ThingMapper;
import com.thyng.metrics.normalizer.MetricsNormalizer;
import com.thyng.metrics.normalizer.MetricsNormalizerFactory;
import com.thyng.model.Metrics;
import com.thyng.model.dto.SensorDTO;
import com.thyng.model.dto.ThingDetailsDTO;
import com.thyng.service.MetricsService;
import com.thyng.service.SensorService;
import com.thyng.util.StringUtil;

import lombok.Builder;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

@Builder
@RequiredArgsConstructor
public class MetricsInjestor {

	@NonNull private final ThingMapper thingMapper;
	@NonNull private final SensorMapper sensorMapper;
	@NonNull private final SensorService sensorService;
	@NonNull private final MetricsService metricsService;
	private final MetricsNormalizerFactory metricsNormalizerFactory = new MetricsNormalizerFactory();
	
	public void injest(Metrics metrics) {
		normalize(metrics);
		persist(metrics);
	}
	
	private void normalize(Metrics metrics) {
		final Sensor sensor = sensorService.findById(metrics.getSensorId());
		if(StringUtil.hasText(sensor.getNormalizer())) {
			final SensorDTO sensorDTO = sensorMapper.toDTO(sensor);
			final ThingDetailsDTO thingDTO = thingMapper.dto(sensor.getThing());
			final MetricsNormalizer metricsNormalizer = metricsNormalizerFactory.getMetricsNormalizer(sensor.getNormalizerLanguage());
			metricsNormalizer.normalize(metrics, sensorDTO, thingDTO);
		}
	}
	
	private void persist(Metrics metrics) {
		metricsService.save(metrics);
	}

}
