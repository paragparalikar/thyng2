package com.thyng.repository.data.jpa;

import org.springframework.data.repository.CrudRepository;

import com.thyng.model.entity.Metric;

public interface MetricRepository extends CrudRepository<Metric, Long> {

}
