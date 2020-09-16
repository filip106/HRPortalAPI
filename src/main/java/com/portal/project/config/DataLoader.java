package com.portal.project.config;

import java.util.ArrayList;
import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;

import com.portal.project.model.ApplicationUser;
import com.portal.project.model.Role;
import com.portal.project.model.Sector;
import com.portal.project.repository.RoleRepository;
import com.portal.project.repository.SectorRepository;
import com.portal.project.repository.UserRepository;

@Component
public class DataLoader implements ApplicationRunner {

	@Autowired
    private UserRepository userRepository;
	
	@Autowired
	private RoleRepository roleRepository;

	@Autowired
	private BCryptPasswordEncoder bCryptPasswordEncoder;
	
	@Autowired
	private SectorRepository sectorRepository;
	
    @Override
    public void run(ApplicationArguments args) {
    	Role employeeRole = new Role(3L,null,"EMPLOYEE");
    	roleRepository.save(new Role(1L,null,"ADMIN"));
    	Role managerRole = roleRepository.save(new Role(2L,null,"MANAGER"));
    	roleRepository.save(employeeRole);
    	Sector sector = sectorRepository.save(new Sector(1L,"financies",null,null));
    	
    	Collection<Role> roles = new ArrayList<>();
    	roles.add(employeeRole);
    	
    	Collection<Role> managerRoles = new ArrayList<>();
    	managerRoles.add(managerRole);
    	
    	Collection<Sector> sectors = new ArrayList<>();
    	sectors.add(sector);
    	
        userRepository.save(new ApplicationUser(1L, "filip", bCryptPasswordEncoder.encode("bot"), "filip@bot.com","Filip","Djordjevic", true,null,null, null,null,null, roles, sector, null,null));
        userRepository.save(new ApplicationUser(2L, "manager", bCryptPasswordEncoder.encode("manager"), "manager@bot.com","Nikola","Djordjevic", true, null, null, null, null, null, managerRoles, null, sectors, null));
    }
	
}