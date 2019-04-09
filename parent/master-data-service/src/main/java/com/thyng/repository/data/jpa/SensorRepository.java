package com.thyng.repository.data.jpa;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.thyng.model.entity.Sensor;

public interface SensorRepository extends JpaRepository<Sensor, Long> {

	List<Sensor> findByThingId(Long thingId);
	
}
