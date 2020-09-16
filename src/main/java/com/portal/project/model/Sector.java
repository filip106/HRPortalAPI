package com.portal.project.model;

import java.util.Collection;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;

import com.fasterxml.jackson.annotation.JsonBackReference;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter @Setter @NoArgsConstructor @AllArgsConstructor
public class Sector {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	private String name;
	
	@JsonBackReference
	@OneToMany(mappedBy="sector")
	private Collection<ApplicationUser> listOfEmployees;
	
	@JsonBackReference
	@ManyToMany(mappedBy = "listOfManagingSectors") 
	private Collection<ApplicationUser> listOfManagers;
	
}
