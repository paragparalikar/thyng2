package com.thyng.domain.gateway;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface GatewayRepository extends JpaRepository<Gateway, Long> {

	List<Gateway> findByTenantId(Long tenantId);
	
	@Query("select new com.thyng.domain.gateway.Gateway(g.id, g.name) from Gateway g where g.tenant.id = ?1")
	List<Gateway> findThinByTenantId(Long tenantId);
	
	boolean existsByIdAndTenantId(Long id, Long tenantId);
	
	boolean existsByIdNotAndNameAndTenantId(Long id, String name, Long tenantId);
	
	void deleteByTenantId(Long tenantId);
	
}
