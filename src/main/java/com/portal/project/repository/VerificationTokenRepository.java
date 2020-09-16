package com.portal.project.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.portal.project.model.ApplicationUser;
import com.portal.project.model.VerificationToken;

@Repository
public interface VerificationTokenRepository extends JpaRepository<VerificationToken, Long>{
	 
	VerificationToken findByToken(String token);
	VerificationToken findByUser(ApplicationUser user);

}
