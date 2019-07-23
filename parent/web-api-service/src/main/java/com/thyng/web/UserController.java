package com.thyng.web;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.thyng.domain.user.User;
import com.thyng.domain.user.UserDTO;
import com.thyng.domain.user.UserMapper;
import com.thyng.domain.user.UserService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/v1/users")
@RequiredArgsConstructor
public class UserController {

	private final UserMapper userMapper;
	private final UserService userService;
	
	@GetMapping("/{id}")
	public UserDTO findById(@PathVariable Long id){
		final User user = userService.findById(id);
		return userMapper.toDTO(user);
	}
	
	@GetMapping("/current")
	public UserDTO getCurrentUser(){
		final User user = (User)SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		return userMapper.toDTO(user);
	}
	
		
}
