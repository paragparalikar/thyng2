package com.thyng.web;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.thyng.model.dto.UserDTO;
import com.thyng.model.entity.User;
import com.thyng.model.mapper.UserMapper;
import com.thyng.service.UserService;

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
	
		
}
