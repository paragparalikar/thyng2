package com.thyng.model.dto;

import com.thyng.model.enumeration.MqttVersion;

import lombok.Data;

@Data
public class MqttClientConfigDTO {

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
