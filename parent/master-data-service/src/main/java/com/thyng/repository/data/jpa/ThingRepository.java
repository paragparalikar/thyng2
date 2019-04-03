package com.thyng.repository.data.jpa;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.thyng.model.entity.Thing;

@Repository
public interface ThingRepository extends JpaRepository<Thing, Long> {

	List<Thing> findByTenantId(Long tenantId);
	
	List<Thing> findByGatewayId(Long gatewayId);
	
	boolean existsByIdAndTenantId(Long id, Long tenantId);
	
	boolean existsByIdNotAndNameAndTenantId(Long id, String name, Long tenantId);
	
	void deleteByTenantId(Long tenantId);
	
}
