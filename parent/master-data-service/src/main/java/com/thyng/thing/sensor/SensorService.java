package com.thyng.thing.sensor;

import java.util.List;

import javax.transaction.Transactional;
import javax.validation.Valid;
import javax.validation.constraints.Positive;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import com.thyng.aspect.persistence.NotFoundException;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;

@Service
@Validated
@Transactional
@RequiredArgsConstructor
public class SensorService {

	@NonNull private final SensorRepository sensorRepository;
	
	@PreAuthorize("hasPermission(null, 'SENSOR', 'LIST')")
	public List<Sensor> findByThingId(@NonNull @Positive final Long thingId){
		return sensorRepository.findByThingId(thingId);
	}
	
	@PreAuthorize("hasPermission(#sensorId, 'SENSOR', 'VIEW')")
	public Sensor findById(@NonNull @Positive final Long sensorId) {
		return sensorRepository.findById(sensorId).orElseThrow(() -> new NotFoundException());
	}
	
	@PreAuthorize("hasPermission(null, 'SENSOR', 'CREATE')")
	public Sensor create(@NonNull @Valid final Sensor sensor) {
		if(null != sensor.getId() && 0 < sensor.getId()) throw new IllegalArgumentException("Id must be null");
		return sensorRepository.save(sensor);
	}
	
	@Transactional
	@PreAuthorize("hasPermission(#sensor.id, 'SENSOR', 'UPDATE')")
	public Sensor update(@NonNull @Valid final Sensor sensor) {
		if(null == sensor.getId() || 0 >= sensor.getId()) throw new IllegalArgumentException("Id must not be null");
		return sensorRepository.save(sensor);
	}
	
	@Transactional
	@PreAuthorize("hasPermission(#sensorId, 'SENSOR', 'DELETE')")
	public void delete(@NonNull  @Positive  final Long sensorId) {
		sensorRepository.deleteById(sensorId);
	}
	
	public boolean existsByIdAndTenantId(@NonNull final Long id, @NonNull final Long tenantId){
		return sensorRepository.existsByIdAndThingTenantId(id, tenantId);
	}
	
	public boolean existsByIdNotAndNameAndThingId(@NonNull final Long id, String name, 
			@NonNull final Long thingId){
		return sensorRepository.existsByIdNotAndNameAndThingId(id, name, thingId);
	}
	
}
