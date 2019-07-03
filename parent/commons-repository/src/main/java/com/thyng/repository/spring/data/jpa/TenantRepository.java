package com.thyng.repository.spring.data.jpa;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.thyng.entity.Tenant;

@Repository
public interface TenantRepository extends JpaRepository<Tenant, Long> {

	boolean existsByIdNotAndName(Long id, String name);
	
}
