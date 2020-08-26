package com.portal.project.controller;

import java.util.Arrays;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.portal.project.config.TokenUtil;
import com.portal.project.constants.RoleConstants;
import com.portal.project.model.ApplicationUser;
import com.portal.project.model.Role;
import com.portal.project.model.dto.TokenDTO;
import com.portal.project.model.dto.UserDTORequest;
import com.portal.project.service.RoleService;
import com.portal.project.service.UserService;


@RestController
public class UserController {
	
	@Autowired
	private AuthenticationManager authenticationManager;
	
	@Autowired
	private RoleService roleService;
	
	@Autowired
	private TokenUtil tokenUtil;
	
	@Autowired
	private UserService userService;
	
	@RequestMapping(value = "/login", method=RequestMethod.POST)
	public ResponseEntity<?> login(@RequestBody UserDTORequest userDTO) throws Exception {
		try {
			authenticationManager.authenticate(
					new UsernamePasswordAuthenticationToken(
							userDTO.getUsername(), userDTO.getPassword()));
			
		} catch (BadCredentialsException e) {
			return new ResponseEntity<String>("Invalid Credentials", HttpStatus.CONFLICT);
		}
		
		final UserDetails userDetails = userService.loadUserByUsername(userDTO.getUsername());
		
		final String token = tokenUtil.generateToken(userDetails);
		return new ResponseEntity<TokenDTO>(new TokenDTO(token), HttpStatus.OK);
	}
	
	@RequestMapping(value = "/register", method=RequestMethod.POST)
	public ResponseEntity<?> register(@RequestBody UserDTORequest userDTO) throws Exception{
		try {
			Role role = roleService.findRoleByName(RoleConstants.EMPLOYEE_ROLE);
			userService.registerUser(ApplicationUser.builder().username(userDTO.getUsername()).password(userDTO.getPassword())
					.email(userDTO.getEmail()).roles(Arrays.asList(role)).build());
			return new ResponseEntity<String>("User registered successfuly!",HttpStatus.OK);
		} catch (Exception e) {
			return new ResponseEntity<String>("There is an account with that username",HttpStatus.CONFLICT);
		}	
	}
	
	@RequestMapping(value = "/validate", method=RequestMethod.POST)
	public ResponseEntity<?> validate(@RequestBody TokenDTO tokenObj) throws Exception{
		try {
			String username = tokenUtil.extractUsername(tokenObj.getToken());
			UserDetails userDetails = this.userService.loadUserByUsername(username);
			if(tokenUtil.validateToken(tokenObj.getToken(), userDetails)) {
				return ResponseEntity.ok(true);
			}
			return ResponseEntity.ok(false);
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.CONFLICT).body("Faulty token!");
		}
	}
	
}
