package com.portal.project.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.portal.project.model.LoginDetails;
import com.portal.project.repository.LoginDetailsRepository;

@Service
public class LoginDetailsService {
	
	@Autowired
	private LoginDetailsRepository loginDetailsRepository;
	
	public LoginDetails saveLoginDetails(LoginDetails loginDetails) {
		return loginDetailsRepository.save(loginDetails);
	}
	
	public List<LoginDetails> findLoginDetails(Long id) {
		return loginDetailsRepository.findAllByUserId(id);
	}
}
