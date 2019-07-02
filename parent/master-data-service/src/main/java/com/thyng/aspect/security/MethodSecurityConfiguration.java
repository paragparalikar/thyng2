package com.thyng.aspect.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.access.PermissionEvaluator;
import org.springframework.security.access.expression.method.DefaultMethodSecurityExpressionHandler;
import org.springframework.security.access.expression.method.MethodSecurityExpressionHandler;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.method.configuration.GlobalMethodSecurityConfiguration;

import com.thyng.actuator.ActuatorService;
import com.thyng.gateway.GatewayService;
import com.thyng.sensor.SensorService;
import com.thyng.thing.ThingService;
import com.thyng.user.UserService;

import lombok.RequiredArgsConstructor;

@Configuration
@RequiredArgsConstructor
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class MethodSecurityConfiguration extends GlobalMethodSecurityConfiguration {

	private final UserService userService;
	private final ThingService thingService;
	private final SensorService sensorService;
	private final ActuatorService actuatorService;
	private final GatewayService gatewayService;

	@Bean
	public PermissionEvaluator permissionEvaluator() {
		return new MethodPermissionEvaluator(userService, thingService, sensorService, actuatorService, gatewayService);
	}

	@Override
	protected MethodSecurityExpressionHandler createExpressionHandler() {
		final DefaultMethodSecurityExpressionHandler expressionHandler = (DefaultMethodSecurityExpressionHandler) super.createExpressionHandler();
		expressionHandler.setPermissionEvaluator(permissionEvaluator());
		return expressionHandler;
	}

}
