package com.thyng.repository.data.jpa;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.thyng.model.entity.User;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

	@Query("from User u where u.email = :email and u.tenant.locked = false and u.tenant.start < CURRENT_TIMESTAMP and u.tenant.expiry > CURRENT_TIMESTAMP")
	Optional<User> findByEmail(@Param("email") String email);
	
	List<User> findByTenantId(Long tenantId);
	
}
