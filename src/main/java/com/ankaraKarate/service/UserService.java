package com.ankaraKarate.service;

import java.util.Optional;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.ankaraKarate.dto.CustomUserDetails;
import com.ankaraKarate.entity.UserEntity;
import com.ankaraKarate.repo.UserRespository;

@Service
public class UserService implements UserDetailsService {

	@Autowired
	private UserRespository repo;
	
	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
	    Optional<UserEntity> userEntityOptional = repo.findByUsername(username);
	    
	    if (userEntityOptional.isPresent()) {
	        CustomUserDetails userDetails = new CustomUserDetails(userEntityOptional.get());
	        return userDetails;
	    } else {
	        throw new UsernameNotFoundException("Kullanıcı bulunamadı: " + username);
	    }
	}


	public void save(@Valid UserEntity user) throws IllegalArgumentException{
		
	    Optional<UserEntity> existingUser = repo.findByEmail(user.getEmail());
	    
	    if(existingUser.isPresent()) {
	    	throw new IllegalArgumentException("Girilen email adresi ile daha önce sisteme kayıt yapılmıştır.");
	    }
		
		repo.save(user);
	}

}
