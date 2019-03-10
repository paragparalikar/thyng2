package com.thyng.service;

import org.springframework.stereotype.Service;

import com.thyng.model.entity.User;
import com.thyng.model.exception.UserNotFoundException;
import com.thyng.repository.data.jpa.UserRepository;

import lombok.RequiredArgsConstructor;

@Service 
@RequiredArgsConstructor
public class UserService {

	private final UserRepository userRepository;
	
	public User findById(Long id){
		return userRepository.findById(id).orElseThrow(()-> new UserNotFoundException());
	}
	
	public User save(User user){
		return userRepository.save(user);
	}
	
	public void deleteById(Long id){
		userRepository.deleteById(id);
	}
	
}
