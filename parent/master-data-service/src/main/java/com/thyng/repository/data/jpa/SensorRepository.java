package com.thyng.repository.data.jpa;

import org.springframework.data.jpa.repository.JpaRepository;

import com.thyng.model.entity.Sensor;

public interface SensorRepository extends JpaRepository<Sensor, Long> {

}
