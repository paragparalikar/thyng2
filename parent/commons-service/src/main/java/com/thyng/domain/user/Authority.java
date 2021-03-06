package com.thyng.domain.user;

/**
 * In the form of {ENTITY}_{OPERATION}
 * ENTITY: {TENANT, GATEWAY, USER, THING, SENSOR, ACTUATOR}
 * OPERATION: {LIST, VIEW, UPDATE, CREATE, DELETE}
 */
public enum Authority{

	TENANT_LIST, TENANT_VIEW, TENANT_UPDATE, TENANT_CREATE, TENANT_DELETE,
	GATEWAY_LIST, GATEWAY_VIEW, GATEWAY_UPDATE, GATEWAY_CREATE, GATEWAY_DELETE,
	USER_LIST, USER_VIEW, USER_UPDATE, USER_CREATE, USER_DELETE,
	THING_LIST, THING_VIEW, THING_UPDATE, THING_CREATE, THING_DELETE,
	SENSOR_LIST, SENSOR_VIEW, SENSOR_UPDATE, SENSOR_CREATE, SENSOR_DELETE,
	ACTUATOR_LIST, ACTUATOR_VIEW, ACTUATOR_UPDATE, ACTUATOR_CREATE, ACTUATOR_DELETE;

}
