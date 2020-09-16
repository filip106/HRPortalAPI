package com.portal.project.model;

import java.util.Collection;
import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.sun.istack.Nullable;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter @Setter @NoArgsConstructor @AllArgsConstructor
@Table(name="application_user", 
        uniqueConstraints= @UniqueConstraint(columnNames={"username"}))
@Builder
public class ApplicationUser {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	private String username;
	private String password;
	private String email;
	private String firstName;
	private String lastName;
	private Boolean isEnabled;
	@Nullable
	private String pictureURI;
	private String backgroundPictureURI;
	private String phoneNumber;
	private Date birthDate;
	private Date dateJoinedCompany;
	
	@JsonManagedReference
	@ManyToMany(fetch = FetchType.EAGER) 
    @JoinTable(name = "users_roles", joinColumns = @JoinColumn(name = "user_id", referencedColumnName = "id"), 
    	inverseJoinColumns = @JoinColumn(name = "role_id", referencedColumnName = "id")) 
    private Collection<Role> roles;
	
	@JsonManagedReference
	@ManyToOne
    @JoinColumn(name="sector_id", nullable=true)
	private Sector sector;
	
	@JsonManagedReference
	@ManyToMany
	@JoinTable(name = "sector_managed", joinColumns = @JoinColumn(name = "manager_id", referencedColumnName = "id"), 
	inverseJoinColumns = @JoinColumn(name = "sector_id", referencedColumnName = "id")) 
	private Collection<Sector> listOfManagingSectors;
	
	@JsonManagedReference
	@OneToMany(mappedBy="user")
	private Collection<LoginDetails> loginDetails;
}
