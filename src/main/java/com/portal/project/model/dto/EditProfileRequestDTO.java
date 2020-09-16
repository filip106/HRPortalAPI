package com.portal.project.model.dto;

import java.util.Date;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter @AllArgsConstructor
public class EditProfileRequestDTO {
	private String username;
	private String firstName;
	private String lastName;
	private String phoneNumber;
	private Date birthDate;
}
