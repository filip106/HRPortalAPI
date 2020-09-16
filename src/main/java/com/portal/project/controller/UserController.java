package com.portal.project.controller;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.portal.project.config.TokenUtil;
import com.portal.project.model.ApplicationUser;
import com.portal.project.model.Sector;
import com.portal.project.model.VerificationToken;
import com.portal.project.model.dto.ConfirmRegistrationDataDTO;
import com.portal.project.model.dto.EditProfileRequestDTO;
import com.portal.project.model.dto.EnableUserDTO;
import com.portal.project.model.dto.ResponseMessageStringDTO;
import com.portal.project.model.dto.UserDTOResponse;
import com.portal.project.service.SectorService;
import com.portal.project.service.UserService;


@RestController
public class UserController {
	
	@Autowired 
	private SectorService sectorService;
	
	@Autowired
	private TokenUtil tokenUtil;
	
	@Autowired
	private UserService userService;
	
	@GetMapping("/verify-registration-token/{token}")
	public ResponseEntity<?> verifyRegistrationToken(@PathVariable(value = "token") String token) {
		VerificationToken vt = userService.getVerificationToken(token);
		if(vt != null && vt.getUser()!=null) {
			Sector sector = sectorService.findSectorById(vt.getUser().getSector().getId());
			List<ApplicationUser> list = new ArrayList<>(sector.getListOfManagers());
			ConfirmRegistrationDataDTO crdDTO = new ConfirmRegistrationDataDTO(vt.getUser().getSector().getName(),vt.getUser().getEmail(),
					list.get(0).getFirstName()+" "+list.get(0).getLastName());
			
			return new ResponseEntity<ConfirmRegistrationDataDTO>(crdDTO,HttpStatus.OK);
		}
		
		return new ResponseEntity<String>("There is not token for that user",HttpStatus.BAD_REQUEST);
	}
	
	@PostMapping("/enable-user/{token}")
	public ResponseEntity<?> enableUser(@RequestBody EnableUserDTO userDTO,@PathVariable(value = "token") String token) {
		VerificationToken vt = userService.getVerificationToken(token);
		if(vt !=null && vt.getUser()!=null) {
			ApplicationUser user = vt.getUser();
			user.setPassword(userDTO.getPassword());
			user.setIsEnabled(true);
			userService.saveUser(user, true);
			return new ResponseEntity<ResponseMessageStringDTO>(new ResponseMessageStringDTO("Enabled!"),HttpStatus.OK);
		} else {
			return new ResponseEntity<ResponseMessageStringDTO>(new ResponseMessageStringDTO("Wrong token!"),HttpStatus.BAD_REQUEST);
		}
		
	}
	
	@GetMapping("/user")
	public ResponseEntity<?> getUser(@RequestHeader("Authorization") String token) {
		ApplicationUser user = userService.extractAndFindByUsername(token);
		if(user != null) {
			return new ResponseEntity<UserDTOResponse>(new UserDTOResponse(user), HttpStatus.OK);
		} else {
			return new ResponseEntity<ResponseMessageStringDTO>(new ResponseMessageStringDTO("User doesen't exist!"), HttpStatus.NOT_FOUND);
		}
	}
	
	@PutMapping("/user")
	public ResponseEntity<?> updateUser(@RequestHeader("Authorization") String token,@RequestBody EditProfileRequestDTO userDTO) {
		ApplicationUser user = userService.extractAndFindByUsername(token);
		if(user != null) {
			try {
				if(!user.getUsername().equals(userDTO.getUsername()))
					user.setUsername(userDTO.getUsername());
				user.setFirstName(userDTO.getFirstName());
				user.setLastName(userDTO.getLastName());
				user.setPhoneNumber(userDTO.getPhoneNumber());
				user.setBirthDate(userDTO.getBirthDate());
				user = userService.saveUser(user, false);
				return new ResponseEntity<UserDTOResponse>(new UserDTOResponse(user), HttpStatus.OK);
				
			} catch (Exception e) {
				return new ResponseEntity<ResponseMessageStringDTO>(new ResponseMessageStringDTO("Username already exists"), HttpStatus.CONFLICT);
			}
		} else {
			return new ResponseEntity<ResponseMessageStringDTO>(new ResponseMessageStringDTO("User doesen't exist!"), HttpStatus.NOT_FOUND);
		}
	}
	
	@PostMapping(value="/upload-profile-picture")
	public ResponseEntity<?> uploadProfilePicture(@RequestParam("picture") MultipartFile file, @RequestHeader("Authorization") String token) throws IOException {
		
		byte[] bytes = file.getBytes();
        Path path = Paths.get("C:/Users/Nikola/Documents/workspace-spring-tool-suite-4-4.6.1.RELEASE/HR-Portal/src/main/resources/assets/images/" + file.getOriginalFilename());
        Files.write(path, bytes);
        String username = tokenUtil.extractUsername(token.substring(7));
		ApplicationUser user = userService.findUserByUsername(username);
		if(user != null) {
			user.setPictureURI("/" + file.getOriginalFilename());
			user = userService.saveUser(user, false);
			return new ResponseEntity<UserDTOResponse>(new UserDTOResponse(user),HttpStatus.OK);
		}
			
		return new ResponseEntity<ResponseMessageStringDTO>(new ResponseMessageStringDTO("Picture not uploaded"),HttpStatus.BAD_REQUEST);
	}
	
	
}
