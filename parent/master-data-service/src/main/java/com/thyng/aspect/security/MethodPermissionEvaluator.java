package com.thyng.aspect.security;

import java.io.Serializable;

import org.springframework.security.access.PermissionEvaluator;
import org.springframework.security.core.Authentication;

import com.thyng.gateway.GatewayService;
import com.thyng.thing.Thing;
import com.thyng.thing.ThingService;
import com.thyng.thing.actuator.Actuator;
import com.thyng.thing.actuator.ActuatorService;
import com.thyng.thing.sensor.Sensor;
import com.thyng.thing.sensor.SensorService;
import com.thyng.user.User;
import com.thyng.user.UserService;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class MethodPermissionEvaluator implements PermissionEvaluator {
	public static final String USER = "USER";
	public static final String THING = "THING";
	public static final String TENANT = "TENANT";
	public static final String GATEWAY = "GATEWAY";
	public static final String SENSOR = "SENSOR";
	public static final String ACTUATOR = "ACTUATOR";

	private final UserService userService;
	private final ThingService thingService;
	private final SensorService sensorService;
	private final ActuatorService actuatorService;
	private final GatewayService gatewayService;
	
	@Override
	public boolean hasPermission(Authentication authentication, Object targetDomainObject, Object permission) {
		if(targetDomainObject instanceof Thing){
			final Thing thing = (Thing)targetDomainObject;
			return hasPermission(authentication, thing.getId(), THING, permission);
		}else if(targetDomainObject instanceof User){
			final User user = (User)targetDomainObject;
			return hasPermission(authentication, user.getId(), USER, permission);
		}else if(targetDomainObject instanceof Sensor) {
			final Sensor sensor = (Sensor)targetDomainObject;
			return hasPermission(authentication, sensor.getId(), SENSOR, permission);
		}else if(targetDomainObject instanceof Actuator) {
			final Actuator actuator = (Actuator)targetDomainObject;
			return hasPermission(authentication, actuator.getId(), ACTUATOR, permission);
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
			case SENSOR : return sensorService.existsByIdAndTenantId((Long)targetId, user.getTenant().getId());
			case ACTUATOR : return actuatorService.existsByIdAndTenantId((Long)targetId, user.getTenant().getId());
			case TENANT : return true;
		}
		
		return false;
	}

}
