package com.thyng.thing.sensor;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

public interface SensorRepository extends JpaRepository<Sensor, Long> {

	List<Sensor> findByThingId(Long thingId);
	
}
