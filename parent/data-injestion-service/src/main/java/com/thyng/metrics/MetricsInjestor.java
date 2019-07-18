package com.thyng.metrics;

import com.thyng.entity.MetricsSchema;
import com.thyng.entity.Sensor;
import com.thyng.mapper.SensorMapper;
import com.thyng.mapper.ThingMapper;
import com.thyng.metrics.normalizer.MetricsNormalizer;
import com.thyng.metrics.normalizer.MetricsNormalizerFactory;
import com.thyng.metrics.resolver.MetricsResolver;
import com.thyng.metrics.resolver.MetricsResolverFactory;
import com.thyng.model.Metrics;
import com.thyng.model.dto.SensorDTO;
import com.thyng.model.dto.ThingDetailsDTO;
import com.thyng.model.enumeration.MetricsType;
import com.thyng.service.MetricsSchemaService;
import com.thyng.service.MetricsService;
import com.thyng.service.SensorService;

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
	@NonNull private final MetricsSchemaService metricsSchemaService;
	private final MetricsResolverFactory metricsResolverFactory = new MetricsResolverFactory();
	private final MetricsNormalizerFactory metricsNormalizerFactory = new MetricsNormalizerFactory();
	
	public void injest(Metrics metrics) {
		resolve(metrics);
		normalize(metrics);
		persist(metrics);
	}
	
	private void resolve(Metrics metrics) {
		final Long id = metrics.getMetricsSchemaId();
		final MetricsSchema metricsSchema = null == id || 0 == id ? null : metricsSchemaService.findById(id);
		final MetricsType type = null == metricsSchema ? null : metricsSchema.getType();
		final MetricsResolver metricsResolver = metricsResolverFactory.getMetricsResolver(type);
		metricsResolver.resolve(metrics, metricsSchema);
	}
	
	private void normalize(Metrics metrics) {
		metrics.getSensorIdValues().forEach((sensorId, values) -> {
			final Sensor sensor = sensorService.findById(sensorId);
			final SensorDTO sensorDTO = sensorMapper.toDTO(sensor);
			final ThingDetailsDTO thingDTO = thingMapper.dto(sensor.getThing());
			final MetricsNormalizer metricsNormalizer = metricsNormalizerFactory.getMetricsNormalizer(sensor.getNormalizerLanguage());
			metricsNormalizer.normalize(metrics, sensorDTO, thingDTO);
		});
	}
	
	private void persist(Metrics metrics) {
		metricsService.save(metrics);
	}

}
