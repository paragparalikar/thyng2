package com.thyng.thing.sensor;

import java.util.List;

import javax.transaction.Transactional;
import javax.validation.constraints.Positive;

import org.springframework.context.ApplicationEventPublisher;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;

@Service
@Validated
@Transactional
@RequiredArgsConstructor
public class SensorService {

	@NonNull private final SensorRepository sensorRepository;
	@NonNull private final ApplicationEventPublisher eventPublisher; // Publish changes to notify gateway
	
	@PreAuthorize("hasPermission(#thingId, 'THING', 'VIEW')")
	public List<Sensor> findByThingId(@NonNull @Positive final Long thingId){
		return sensorRepository.findByThingId(thingId);
	}
	
	@PreAuthorize("hasPermission(#sensor.thing.id, 'THING', 'UPDATE')")
	public Sensor save(@NonNull final Sensor sensor) {
		return sensorRepository.save(sensor);
	}
	
	//@PreAuthorize("hasPermission(#sensor.thing.id, 'THING', 'UPDATE')") ???
	public void delete(@NonNull  @Positive  final Long sensorId) {
		sensorRepository.deleteById(sensorId);
	}
	
}
