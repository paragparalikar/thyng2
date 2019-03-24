package com.thyng.repository.data.jpa;

import org.springframework.data.jpa.repository.JpaRepository;

import com.thyng.model.entity.Metric;

public interface MetricRepository extends JpaRepository<Metric, Long> {

}
