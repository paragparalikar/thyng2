package com.thyng.repository.data.jpa;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.thyng.model.entity.Gateway;

@Repository
public interface GatewayRepository extends JpaRepository<Gateway, Long> {

	List<Gateway> findByTenantId(Long tenantId);
	
	boolean existsByIdAndTenantId(Long id, Long tenantId);
	
	boolean existsByIdNotAndNameAndTenantId(Long id, String name, Long tenantId);
	
	void deleteByTenantId(Long tenantId);
	
}
