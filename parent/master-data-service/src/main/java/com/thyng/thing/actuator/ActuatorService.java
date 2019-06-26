package com.thyng.thing.actuator;

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
public class ActuatorService {

	@NonNull private final ActuatorRepository actuatorRepository;
	
	@PreAuthorize("hasPermission(null, 'ACTUATOR', 'LIST')")
	public List<Actuator> findByThingId(@NonNull @Positive final Long thingId){
		return actuatorRepository.findByThingId(thingId);
	}
	
	@PreAuthorize("hasPermission(#actuatorId, 'ACTUATOR', 'VIEW')")
	public Actuator findById(@NonNull @Positive final Long actuatorId) {
		return actuatorRepository.findById(actuatorId).orElseThrow(() -> new NotFoundException());
	}
	
	@PreAuthorize("hasPermission(null, 'ACTUATOR', 'CREATE')")
	public Actuator create(@NonNull @Valid final Actuator actuator) {
		if(null != actuator.getId() || 0 < actuator.getId()) throw new IllegalArgumentException("Id must be null");
		return actuatorRepository.save(actuator);
	}
	
	@Transactional
	@PreAuthorize("hasPermission(#actuator.id, 'ACTUATOR', 'UPDATE')")
	public Actuator update(@NonNull @Valid final Actuator actuator) {
		if(null == actuator.getId() || 0 >= actuator.getId()) throw new IllegalArgumentException("Id must not be null");
		return actuatorRepository.save(actuator);
	}
	
	@Transactional
	@PreAuthorize("hasPermission(#actuatorId, 'ACTUATOR', 'DELETE')")
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
