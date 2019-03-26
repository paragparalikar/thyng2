package com.thyng.repository.data.jpa;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.thyng.model.entity.Thing;

@Repository
public interface ThingRepository extends JpaRepository<Thing, Long> {

	Optional<Thing> findByKey(String key);
	
	List<Thing> findByTenantId(Long tenantId);
	
}
