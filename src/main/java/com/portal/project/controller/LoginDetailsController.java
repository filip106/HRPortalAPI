package com.portal.project.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import com.portal.project.model.LoginDetails;
import com.portal.project.service.LoginDetailsService;

@RestController
public class LoginDetailsController {

	@Autowired
	private LoginDetailsService loginDetailsService;
	
	@GetMapping("/login-details/{id}")
	public ResponseEntity<?> findAllByUserId(@PathVariable("id") Long id) {
		List<LoginDetails> listOfLoginDetails = loginDetailsService.findLoginDetails(id);
		return new ResponseEntity<List<LoginDetails>>(listOfLoginDetails,HttpStatus.OK);
	}
}
