package com.portal.project.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.portal.project.model.Role;

@Repository
public interface RoleRepository extends JpaRepository<Role, Long>{
	public Role findByName(String name);
}
