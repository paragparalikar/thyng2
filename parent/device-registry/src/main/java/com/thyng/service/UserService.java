package com.thyng.service;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.thyng.model.entity.User;
import com.thyng.model.exception.NotFoundException;
import com.thyng.repository.data.jpa.UserRepository;

import lombok.RequiredArgsConstructor;

@Service 
@RequiredArgsConstructor
public class UserService implements UserDetailsService{

	private final UserRepository userRepository;
	
	public User findById(Long id){
		return userRepository.findById(id).orElseThrow(()-> new NotFoundException());
	}
	
	public User save(User user){
		return userRepository.save(user);
	}
	
	public void deleteById(Long id){
		userRepository.deleteById(id);
	}
	
	public boolean existsByIdAndTenantId(Long id, Long tenantId){
		return userRepository.existsByIdAndTenantId(id, tenantId);
	}

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		return userRepository.findByEmail(username).orElseThrow(() -> new UsernameNotFoundException(username));
	}
	
}
