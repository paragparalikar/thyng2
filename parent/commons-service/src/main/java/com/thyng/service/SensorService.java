package com.thyng.service;

import java.util.List;

import javax.transaction.Transactional;
import javax.validation.Valid;
import javax.validation.constraints.Positive;

import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import com.thyng.entity.Sensor;
import com.thyng.model.exception.NotFoundException;
import com.thyng.repository.spring.data.jpa.SensorRepository;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;

@Service
@Validated
@Transactional
@RequiredArgsConstructor
public class SensorService {

	@NonNull private final SensorRepository sensorRepository;
	
	public List<Sensor> findByThingId(@NonNull @Positive final Long thingId){
		return sensorRepository.findByThingId(thingId);
	}
	
	public Sensor findById(@NonNull @Positive final Long sensorId) {
		return sensorRepository.findById(sensorId).orElseThrow(() -> new NotFoundException());
	}
	
	public Sensor create(@NonNull @Valid final Sensor sensor) {
		if(null != sensor.getId() && 0 < sensor.getId()) throw new IllegalArgumentException("Id must be null");
		return sensorRepository.save(sensor);
	}
	
	@Transactional
	public Sensor update(@NonNull @Valid final Sensor sensor) {
		if(null == sensor.getId() || 0 >= sensor.getId()) throw new IllegalArgumentException("Id must not be null");
		return sensorRepository.save(sensor);
	}
	
	@Transactional
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
