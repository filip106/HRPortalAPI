package com.portal.project.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.portal.project.model.LoginDetails;

@Repository
public interface LoginDetailsRepository extends JpaRepository<LoginDetails, Long>{
	public List<LoginDetails> findAllByUserId(Long id);
}
