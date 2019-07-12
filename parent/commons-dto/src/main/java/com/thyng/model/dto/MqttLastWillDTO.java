package com.thyng.model.dto;

import java.io.Serializable;

import com.thyng.model.enumeration.MqttQoS;

import lombok.Data;

@Data
public class MqttLastWillDTO implements Serializable{
	private static final long serialVersionUID = -4693328968423937003L;

	private String topic;
	
	private String message;
	
	private boolean retain;
	
	private MqttQoS qos;
	
}
