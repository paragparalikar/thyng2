package com.thyng.repository.data.jpa;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.thyng.model.entity.Metric;

@Repository
public interface MetricRepository extends JpaRepository<Metric, Long> {
	
}
