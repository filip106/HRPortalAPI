package com.portal.project.model.dto;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.portal.project.model.ApplicationUser;
import com.portal.project.model.Role;
import com.portal.project.model.Sector;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter @Setter @NoArgsConstructor
public class UserDTOResponse {

	private Long id;
	private String username;
	private String email;
	private String firstName;
	private String lastName;
	private List<Role> roles;
	private Sector sector;
	private String pictureURI;
	private String backgroundPictureURI;
	private String phoneNumber;
	private Date birthDate;
	private Date dateJoinedCompany;
	
	public UserDTOResponse(ApplicationUser user) {
		this.id = user.getId();
		this.username = user.getUsername();
		this.email = user.getEmail();
		this.firstName = user.getFirstName();
		this.lastName = user.getLastName();
		this.roles = new ArrayList<>(user.getRoles());
		this.sector = user.getSector();
		this.pictureURI = user.getPictureURI();
		this.backgroundPictureURI = user.getBackgroundPictureURI();
		this.phoneNumber = user.getPhoneNumber();
		this.birthDate = user.getBirthDate();
		this.dateJoinedCompany = user.getDateJoinedCompany();
	}
}
