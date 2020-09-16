package com.portal.project.controller;

import java.io.File;
import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.time.Instant;
import java.util.Arrays;
import java.util.Date;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;

import com.maxmind.geoip2.DatabaseReader;
import com.maxmind.geoip2.exception.GeoIp2Exception;
import com.maxmind.geoip2.model.CityResponse;
import com.portal.project.config.TokenUtil;
import com.portal.project.model.ApplicationUser;
import com.portal.project.model.LoginDetails;
import com.portal.project.model.Role;
import com.portal.project.model.Sector;
import com.portal.project.model.dto.TokenDTO;
import com.portal.project.model.dto.UserDTORequest;
import com.portal.project.service.LoginDetailsService;
import com.portal.project.service.RoleService;
import com.portal.project.service.SectorService;
import com.portal.project.service.UserService;
import com.portal.project.utils.OnRegistrationCompleteEvent;

@RestController
public class AuthorizationController {

	@Autowired
	private AuthenticationManager authenticationManager;
	
	@Autowired
	private LoginDetailsService loginDetailsService;
	
	@Autowired
	private TokenUtil tokenUtil;
	
	@Autowired
	private UserService userService;
	
	@Autowired
	private RoleService roleService;
	
	@Autowired
	ApplicationEventPublisher eventPublisher;
	
	@Autowired 
	private SectorService sectorService;
	
	@PostMapping(value = "/validate")
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
	
	@PostMapping(value = "/login")
	public ResponseEntity<?> login(@RequestBody UserDTORequest userDTO, @RequestHeader(value="Origin", required = false) String originURL,
			@RequestHeader(value = "User-Agent") String userAgent,
			HttpServletRequest request) throws Exception {
		
		if(userAgent != null) {
			if(userAgent.contains("(")) {
				userAgent = userAgent.substring(userAgent.indexOf("(")+1, userAgent.indexOf(";"));
			}
		}
		try {
			authenticationManager.authenticate(
					new UsernamePasswordAuthenticationToken(
							userDTO.getUsername(), userDTO.getPassword()));
			
		} catch (BadCredentialsException e) {
			return new ResponseEntity<String>("Invalid Credentials", HttpStatus.CONFLICT);
		}
		
		final UserDetails userDetails = userService.loadUserByUsername(userDTO.getUsername());
		//if user is disabled
		if(userDetails == null) {
			return new ResponseEntity<String>("User is still not registered", HttpStatus.BAD_REQUEST);
		}
		
		final String token = tokenUtil.generateToken(userDetails);
		ApplicationUser user = userService.findUserByUsername(userDTO.getUsername());
		
		File database = new File("src/main/resources/assets/GeoLite2-City.mmdb");
		DatabaseReader dbReader = new DatabaseReader.Builder(database).build();
		
		try {
			InetAddress ipAddress = InetAddress.getByName(originURL);
		    CityResponse response = dbReader.city(ipAddress);
		    
		    String countryName = response.getCountry().getName();
		    String cityName = response.getCity().getName();
		    loginDetailsService.saveLoginDetails(new LoginDetails(null, userAgent, cityName+", "+countryName, Instant.now(), user));
		} catch (UnknownHostException e) {
			loginDetailsService.saveLoginDetails(new LoginDetails(null, userAgent, "N/A", Instant.now(), user));
		} catch (IOException e) {
			loginDetailsService.saveLoginDetails(new LoginDetails(null, userAgent, "N/A", Instant.now(), user));
		} catch (GeoIp2Exception e) {
			loginDetailsService.saveLoginDetails(new LoginDetails(null, userAgent, "N/A", Instant.now(), user));
		}
		return new ResponseEntity<TokenDTO>(new TokenDTO(token), HttpStatus.OK);
	}
	
	@PostMapping(value = "/manager/register")
	public ResponseEntity<?> register(@RequestBody UserDTORequest userDTO, HttpServletRequest request) throws Exception{
		try {
			Role role = roleService.findRoleById(userDTO.getRoleId());
			Sector sector = sectorService.findSectorById(userDTO.getSectorId());
			ApplicationUser user = ApplicationUser.builder().username(userDTO.getUsername()).firstName(userDTO.getFirstName()).lastName(userDTO.getLastName())
					.email(userDTO.getEmail()).roles(Arrays.asList(role)).sector(sector).dateJoinedCompany(new Date()).isEnabled(false).build();
			userService.saveUser(user, true);
			String appUrl = request.getContextPath();
			
			eventPublisher.publishEvent(new OnRegistrationCompleteEvent(user, request.getLocale(), appUrl));
			return new ResponseEntity<String>("User registered successfuly!",HttpStatus.OK);
		} catch (Exception e) {
			return new ResponseEntity<String>("There is an account with that username",HttpStatus.CONFLICT);
		}	
	}
}
