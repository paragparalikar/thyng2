package com.thyng.repository.data.jpa;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.thyng.model.entity.User;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

}
