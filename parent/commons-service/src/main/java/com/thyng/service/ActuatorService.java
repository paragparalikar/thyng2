package com.thyng.service;

import java.util.List;

import javax.transaction.Transactional;
import javax.validation.Valid;
import javax.validation.constraints.Positive;

import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import com.thyng.entity.Actuator;
import com.thyng.model.exception.NotFoundException;
import com.thyng.repository.spring.data.jpa.ActuatorRepository;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;

@Service
@Validated
@Transactional
@RequiredArgsConstructor
public class ActuatorService {

	@NonNull private final ActuatorRepository actuatorRepository;
	
	public List<Actuator> findByThingId(@NonNull @Positive final Long thingId){
		return actuatorRepository.findByThingId(thingId);
	}
	
	public Actuator findById(@NonNull @Positive final Long actuatorId) {
		return actuatorRepository.findById(actuatorId).orElseThrow(() -> new NotFoundException());
	}
	
	public Actuator create(@NonNull @Valid final Actuator actuator) {
		if(null != actuator.getId() || 0 < actuator.getId()) throw new IllegalArgumentException("Id must be null");
		return actuatorRepository.save(actuator);
	}
	
	@Transactional
	public Actuator update(@NonNull @Valid final Actuator actuator) {
		if(null == actuator.getId() || 0 >= actuator.getId()) throw new IllegalArgumentException("Id must not be null");
		return actuatorRepository.save(actuator);
	}
	
	@Transactional
	public void delete(@NonNull  @Positive  final Long actuatorId) {
		actuatorRepository.deleteById(actuatorId);
	}
	
	public boolean existsByIdAndTenantId(@NonNull final Long id, @NonNull final Long tenantId){
		return actuatorRepository.existsByIdAndThingTenantId(id, tenantId);
	}
	
	public boolean existsByIdNotAndNameAndThingId(@NonNull final Long id, String name, 
			@NonNull final Long thingId){
		return actuatorRepository.existsByIdNotAndNameAndThingId(id, name, thingId);
	}

}
