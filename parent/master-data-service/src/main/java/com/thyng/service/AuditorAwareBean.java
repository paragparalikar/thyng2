package com.thyng.service;

import java.util.Optional;

import org.springframework.data.domain.AuditorAware;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import com.thyng.model.entity.User;

public class AuditorAwareBean implements AuditorAware<Long>{

	@Override
	public Optional<Long> getCurrentAuditor() {
		final Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		if(null != authentication && null != authentication.getPrincipal()){
			final User user = (User)authentication.getPrincipal();
			return Optional.of(user.getId());
		}
		return Optional.empty();
	}

	
	
}
