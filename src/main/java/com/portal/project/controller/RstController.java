package com.portal.project.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;

import com.portal.project.config.TokenUtil;
import com.portal.project.model.ApplicationUser;
import com.portal.project.service.UserService;

@RestController
public class RstController {
	
	@Autowired
	private TokenUtil tokenUtil;
	
	@Autowired
	private UserService userService;
	
	@GetMapping("/employee/user-info")
	public ResponseEntity<ApplicationUser> getUserInfo(@RequestHeader(value = "Authorization") String token) {
		String username = tokenUtil.extractUsername(token.substring(7));
		ApplicationUser user = userService.findUserByUsername(username);
		return new ResponseEntity<ApplicationUser>(user, HttpStatus.OK);
	}
	
	@GetMapping("/admin/cao")
	public String caoadmine() {
		return "cao";
	}
}
