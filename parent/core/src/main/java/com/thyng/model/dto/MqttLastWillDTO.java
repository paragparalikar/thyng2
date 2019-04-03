package com.thyng.model.dto;

import com.thyng.model.enumeration.MqttQoS;

import lombok.Data;

@Data
public class MqttLastWillDTO {

	private String topic;
	
	private String message;
	
	private boolean retain;
	
	private MqttQoS qos;
	
}
