package com.thyng.repository.data.jpa;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.thyng.model.entity.Actuator;

@Repository
public interface ActuatorRepository extends JpaRepository<Actuator, Long> {

	List<Actuator> findByThingId(Long thingId);
	
}
