package com.portal.project.service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.portal.project.model.ApplicationUser;
import com.portal.project.model.Role;
import com.portal.project.repository.UserRepository;


@Service
public class UserService implements UserDetailsService{

	@Autowired
	private UserRepository userRepository;
	
	@Autowired
	private BCryptPasswordEncoder bCryptPasswordEncoder;
	
	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		ApplicationUser appUser = userRepository.findByUsername(username);
		if (appUser != null) {
			Collection<? extends GrantedAuthority> authorities = getAuthorities(appUser.getRoles());
			return new User(appUser.getUsername(),appUser.getPassword(),authorities);
		} else {
			throw new UsernameNotFoundException("User doesen't exist with the given username: " + username);
		}
	}
	
	public ApplicationUser registerUser(ApplicationUser appUser) {
		appUser.setPassword(bCryptPasswordEncoder.encode(appUser.getPassword()));
		return userRepository.save(appUser);
	}
	
	private Collection<? extends GrantedAuthority> getAuthorities(Collection<Role> roles) {
	    List<GrantedAuthority> authorities
	      = new ArrayList<>();
	    for (Role role: roles) {
	        authorities.add(new SimpleGrantedAuthority(role.getName()));
	    }
	    
	    return authorities;
	}

}
