package com.thyng.domain.mqtt;

import java.io.Serializable;

import lombok.Data;

@Data
public class MqttClientConfigDTO implements Serializable{
	private static final long serialVersionUID = -5511682534556865803L;

	private Long id;
	
	private String host = "0.0.0.0";
	
	private int port = 1883;

	private String clientId = "thyng-gateway";
	
	private int timeoutSeconds = 10;
	
	private MqttVersion protocolVersion = MqttVersion.MQTT_3_1;
	
	private String username;
	
	private String password;
	
	private boolean cleanSession;
	
	private MqttLastWillDTO lastWill;

}
