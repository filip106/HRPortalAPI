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

import com.portal.project.config.TokenUtil;
import com.portal.project.model.ApplicationUser;
import com.portal.project.model.Role;
import com.portal.project.model.VerificationToken;
import com.portal.project.repository.UserRepository;
import com.portal.project.repository.VerificationTokenRepository;


@Service
public class UserService implements UserDetailsService{

	@Autowired
	private UserRepository userRepository;
	
	@Autowired
	private BCryptPasswordEncoder bCryptPasswordEncoder;
	
	@Autowired
    private VerificationTokenRepository tokenRepository;
	
	@Autowired
	private TokenUtil tokenUtil;
	
	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		ApplicationUser appUser = userRepository.findByUsername(username);
		if (appUser != null) {
			if(!appUser.getIsEnabled()) {
				return null;
			}
			Collection<? extends GrantedAuthority> authorities = getAuthorities(appUser.getRoles());
			return new User(appUser.getUsername(),appUser.getPassword(),authorities);
		} 
		else {
			throw new UsernameNotFoundException("User doesen't exist with the given username: " + username);
		}
	}
	
	public ApplicationUser saveUser(ApplicationUser appUser, Boolean changePassword) {
		if(appUser.getPassword() != null && changePassword) {
			appUser.setPassword(bCryptPasswordEncoder.encode(appUser.getPassword()));
		}
		return userRepository.save(appUser);
	}
	
	public ApplicationUser findUserByUsername(String username) {
		return userRepository.findByUsername(username);
	}
	
	public ApplicationUser extractAndFindByUsername(String token) {
		String username = tokenUtil.extractUsername(token.substring(7));
		return findUserByUsername(username);
	}
	
	private Collection<? extends GrantedAuthority> getAuthorities(Collection<Role> roles) {
	    List<GrantedAuthority> authorities
	      = new ArrayList<>();
	    for (Role role: roles) {
	        authorities.add(new SimpleGrantedAuthority(role.getName()));
	    }
	    
	    return authorities;
	}
	
	//methods to confirm registration
    public void createVerificationToken(ApplicationUser user, String token) {
        VerificationToken myToken = VerificationToken.builder().token(token).user(user).build();
        tokenRepository.save(myToken);
    }
    
    public VerificationToken getVerificationToken(String VerificationToken) {
        return tokenRepository.findByToken(VerificationToken);
    }

}
