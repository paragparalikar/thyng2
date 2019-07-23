package com.thyng.domain.user;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

	@Query("from User u where u.email = :email and u.tenant.locked = false and u.tenant.start < CURRENT_TIMESTAMP and u.tenant.expiry > CURRENT_TIMESTAMP")
	Optional<User> findByEmail(@Param("email") String email);
	
	boolean existsByIdAndTenantId(Long id, Long tenantId);
	
	List<User> findByTenantId(Long tenantId);
	
	void deleteByTenantId(Long tenantId);
	
}
