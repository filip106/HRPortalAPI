package com.portal.project.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.portal.project.model.ApplicationUser;

@Repository
public interface UserRepository extends JpaRepository<ApplicationUser, Long>{
	public ApplicationUser findByUsername(String username);
}
