package com.thyng.service;

import java.io.Serializable;

import org.springframework.security.access.PermissionEvaluator;
import org.springframework.security.core.Authentication;

import com.thyng.model.entity.Thing;
import com.thyng.model.entity.User;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class MethodPermissionEvaluator implements PermissionEvaluator {
	private static final String USER = "USER";
	private static final String THING = "THING";
	private static final String TENANT = "TENANT";
	private static final String GATEWAY = "GATEWAY";

	private final UserService userService;
	private final ThingService thingService;
	private final GatewayService gatewayService;
	
	@Override
	public boolean hasPermission(Authentication authentication, Object targetDomainObject, Object permission) {
		if(targetDomainObject instanceof Thing){
			final Thing thing = (Thing)targetDomainObject;
			return hasPermission(authentication, thing.getId(), THING, permission);
		}else if(targetDomainObject instanceof User){
			final User user = (User)targetDomainObject;
			return hasPermission(authentication, user.getId(), USER, permission);
		}
		return false;
	}

	@Override
	public boolean hasPermission(Authentication authentication, Serializable targetId, String targetType, Object permission) {
		if(null == authentication || null == targetType || null == permission){
			return false;
		}
		
		final User user = (User)authentication.getPrincipal();
		if(!user.hasAuthority(targetType + "_" + permission)){
			return false;
		}
		
		if(null == targetId){
			return true;
		}
		
		switch(targetType){
			case THING : return thingService.existsByIdAndTenantId((Long)targetId, user.getTenant().getId());
			case USER : return userService.existsByIdAndTenantId((Long)targetId, user.getTenant().getId());
			case GATEWAY : return gatewayService.existsByIdAndTenantId((Long)targetId, user.getTenant().getId());
			case TENANT : return true;
		}
		
		return false;
	}

}
