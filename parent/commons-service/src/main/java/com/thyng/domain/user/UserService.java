package com.thyng.domain.user;

import javax.validation.Valid;
import javax.validation.constraints.Positive;

import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import com.thyng.configuration.persistence.NotFoundException;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;

@Service 
@Validated
@RequiredArgsConstructor
public class UserService{

	private final UserRepository userRepository;
	
	public User findById(@NonNull @Positive final Long id){
		return userRepository.findById(id).orElseThrow(()-> new NotFoundException());
	}
	
	public User findByEmail(@NonNull final String email) {
		return userRepository.findByEmail(email).orElseThrow(() -> new NotFoundException());
	}
	
	public User save(@NonNull @Valid User user){
		return userRepository.save(user);
	}
	
	public void deleteById(@NonNull @Positive final Long id){
		userRepository.deleteById(id);
	}
	
	public void deleteByTenantId(@NonNull @Positive final Long tenantId){
		userRepository.deleteByTenantId(tenantId);
	}
	
	public boolean existsByIdAndTenantId(Long id, @NonNull @Positive final Long tenantId){
		return userRepository.existsByIdAndTenantId(id, tenantId);
	}

}
