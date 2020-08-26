package com.portal.project.config;

import java.util.ArrayList;
import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import com.portal.project.model.ApplicationUser;
import com.portal.project.model.Role;
import com.portal.project.repository.RoleRepository;
import com.portal.project.repository.UserRepository;

@Component
public class DataLoader implements ApplicationRunner {

	@Autowired
    private UserRepository userRepository;
	
	@Autowired
	private RoleRepository roleRepository;

    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    @Override
    public void run(ApplicationArguments args) {
    	Role employeeRole = new Role(3L, null, "EMPLOYEE");
    	roleRepository.save(new Role(1L, null, "ADMIN"));
    	roleRepository.save(new Role(2L, null, "MANAGER"));
    	roleRepository.save(employeeRole);
    	Collection<Role> roles = new ArrayList<>();
    	roles.add(employeeRole);
        userRepository.save(new ApplicationUser(1L, "filip", bCryptPasswordEncoder.encode("bot"), "filip@bot.com",roles));
    }
	
}