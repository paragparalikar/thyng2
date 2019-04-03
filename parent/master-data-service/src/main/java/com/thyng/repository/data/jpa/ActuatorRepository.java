package com.thyng.repository.data.jpa;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.thyng.model.entity.Actuator;

@Repository
public interface ActuatorRepository extends JpaRepository<Actuator, Long> {

}
