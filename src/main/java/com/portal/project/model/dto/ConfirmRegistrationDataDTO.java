package com.portal.project.model.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor
public class ConfirmRegistrationDataDTO {
	
	private String sectorName;
	private String email;
	private String managerName;
}
