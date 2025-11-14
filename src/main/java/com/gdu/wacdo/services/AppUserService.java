package com.gdu.wacdo.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.*;
import org.springframework.stereotype.Service;

import com.gdu.wacdo.models.AppUser;
import com.gdu.wacdo.repositories.AppUserRepository;

@Service
public class AppUserService implements UserDetailsService{

	@Autowired
	private AppUserRepository userRepository;
	
	@Override
	public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
		AppUser appUser = userRepository.findByEmail(email);
		
		if(appUser != null) {
			var springUser = User.withUsername(appUser.getEmail()).password(appUser.getPassword()).roles(appUser.getRole()).build();
			return springUser;
		}
		return null;
	}

}
