package com.portal.project.model.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter @AllArgsConstructor
public class UserDTORequest {

	private String username;
	private String password;
	private String email;
}
