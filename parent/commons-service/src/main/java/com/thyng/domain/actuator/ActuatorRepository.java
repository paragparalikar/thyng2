package com.thyng.domain.actuator;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ActuatorRepository extends JpaRepository<Actuator, Long> {

	List<Actuator> findByThingId(Long thingId);
	
	void deleteByThingId(Long thingId);
	
	void deleteByThingTenantId(Long tenantId);
	
	boolean existsByIdAndThingTenantId(Long id, Long tenantId);

	boolean existsByIdNotAndNameAndThingId(Long id, String name, Long thingId);
	
}
