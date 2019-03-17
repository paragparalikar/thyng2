package com.thyng.repository.data.jpa;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.thyng.model.entity.Template;

@Repository
public interface TemplateRepository extends JpaRepository<Template, Long> {

	boolean existsByName(String name);
	
}
