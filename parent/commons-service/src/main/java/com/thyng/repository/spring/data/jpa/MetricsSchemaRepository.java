package com.thyng.repository.spring.data.jpa;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.thyng.entity.MetricsSchema;

@Repository
public interface MetricsSchemaRepository extends JpaRepository<MetricsSchema, Long> {

}
