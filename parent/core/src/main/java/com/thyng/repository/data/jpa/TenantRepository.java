package com.thyng.repository.data.jpa;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.thyng.model.entity.Tenant;

@Repository
public interface TenantRepository extends JpaRepository<Tenant, Long> {

	boolean existsByIdNotAndName(Long id, String name);
	
}
