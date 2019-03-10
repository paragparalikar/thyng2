package com.thyng.repository.data.jpa;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.thyng.model.entity.Organisation;

@Repository
public interface OrganisationRepository extends JpaRepository<Organisation, Long> {

}
