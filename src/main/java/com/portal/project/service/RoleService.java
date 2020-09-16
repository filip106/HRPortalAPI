package com.portal.project.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.portal.project.model.Role;
import com.portal.project.repository.RoleRepository;

@Service
public class RoleService {

	@Autowired
	private RoleRepository roleRepository;
	
	public Role findRoleByName(String name) {
		return roleRepository.findByName(name);
	}
	
	public Role findRoleById(Long id) {
		return roleRepository.findById(id).get();
	}
}
