package com.thyng.service;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.access.PermissionEvaluator;
import org.springframework.security.access.expression.method.DefaultMethodSecurityExpressionHandler;
import org.springframework.security.access.expression.method.MethodSecurityExpressionHandler;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.method.configuration.GlobalMethodSecurityConfiguration;

import lombok.RequiredArgsConstructor;

@Configuration
@RequiredArgsConstructor
@EnableGlobalMethodSecurity(prePostEnabled=true)
public class MethodSecurityConfiguration extends GlobalMethodSecurityConfiguration{

	private final UserService userService;
	private final ThingService thingService;
	
	@Bean
	public PermissionEvaluator permissionEvaluator(){
		return new MethodPermissionEvaluator(userService, thingService);
	}
	
	@Override
	protected MethodSecurityExpressionHandler createExpressionHandler() {
		final DefaultMethodSecurityExpressionHandler expressionHandler = (DefaultMethodSecurityExpressionHandler)super.createExpressionHandler();
		expressionHandler.setPermissionEvaluator(permissionEvaluator());
		return expressionHandler;
	}

}
